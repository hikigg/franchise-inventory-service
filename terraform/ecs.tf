resource "aws_ecs_cluster" "main" {
  name = "franchise-inventory-cluster"
}

resource "aws_ecs_task_definition" "app" {
  family                   = "franchise-inventory-task"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = "256"
  memory                   = "512"
  execution_role_arn       = aws_iam_role.ecs_task_execution.arn

  container_definitions = jsonencode([
    {
      name      = "franchise-inventory-service"
      image     = "${aws_ecr_repository.app.repository_url}:latest"
      essential = true
      portMappings = [
        {
          containerPort = 8080
          hostPort      = 8080
        }
      ],
      logConfiguration = {
        logDriver = "awslogs",
        options = {
          "awslogs-group"         = "/ecs/franchise-inventory-service",
          "awslogs-region"        = "us-east-1",
          "awslogs-stream-prefix" = "ecs"
        }
      },
      environment = [
        {
          name  = "SPRING_PROFILES_ACTIVE"
          value = "prod"
        },
        # Flyway needs the blocking JDBC URL
        {
          name  = "SPRING_FLYWAY_URL"
          value = "jdbc:postgresql://${aws_db_instance.default.address}:${aws_db_instance.default.port}/${aws_db_instance.default.db_name}?sslmode=require"        },
        {
          name = "SPRING_FLYWAY_USER"
          value = aws_db_instance.default.username
        },
        {
          name = "SPRING_FLYWAY_PASSWORD"
          value = aws_db_instance.default.password
        },
        # R2DBC uses the reactive URL
        {
          name = "SPRING_R2DBC_URL"
          value = "r2dbc:postgresql://${aws_db_instance.default.address}:${aws_db_instance.default.port}/${aws_db_instance.default.db_name}?sslmode=require"
        },
        {
          name = "SPRING_R2DBC_USERNAME"
          value = aws_db_instance.default.username
        },
        {
          name = "SPRING_R2DBC_PASSWORD"
          value = aws_db_instance.default.password
        }
      ]
    }
  ])
}

resource "aws_ecs_service" "app" {
  name            = "franchise-inventory-service"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.app.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  network_configuration {
    subnets         = [aws_subnet.public_a.id, aws_subnet.public_b.id]
    security_groups = [aws_security_group.ecs_tasks.id]
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.app.arn
    container_name   = "franchise-inventory-service"
    container_port   = 8080
  }

  depends_on = [aws_lb_listener.http]
}

resource "aws_iam_role" "ecs_task_execution" {
  name = "ecs_task_execution_role"

  assume_role_policy = jsonencode({
    Version   = "2012-10-17"
    Statement = [
      {
        Action    = "sts:AssumeRole"
        Effect    = "Allow"
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        }
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "ecs_task_execution" {
  role       = aws_iam_role.ecs_task_execution.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

resource "aws_iam_role_policy_attachment" "ecs_ecr_read_access" {
  role       = aws_iam_role.ecs_task_execution.name
  policy_arn = "arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryReadOnly"
}