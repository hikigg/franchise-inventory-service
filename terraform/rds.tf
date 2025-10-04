# rds.tf

# Subnet group for the RDS instance
resource "aws_db_subnet_group" "default" {
  name       = "franchise-inventory-db-subnet-group"
  subnet_ids = [aws_subnet.public_a.id, aws_subnet.public_b.id]

  tags = {
    Name = "Franchise Inventory DB Subnet Group"
  }
}

# Security group for the RDS instance to allow traffic from the app
resource "aws_security_group" "rds" {
  name        = "franchise-inventory-rds-sg"
  description = "Allow postgres traffic from ECS tasks"
  vpc_id      = aws_vpc.main.id

  # Allow inbound traffic from the application's security group on the PostgreSQL port
  ingress {
    from_port       = 5432
    to_port         = 5432
    protocol        = "tcp"
    security_groups = [aws_security_group.ecs_tasks.id] # This is the key part!
  }

  # Allow all outbound traffic
  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# RDS PostgreSQL Instance
resource "aws_db_instance" "default" {
  allocated_storage    = 10
  engine               = "postgres"
  engine_version       = "16"
  instance_class       = "db.t3.micro"
  db_name              = "franchise_inventory"
  username             = "dbuser"
  password             = "admin123" # For a real project, use AWS Secrets Manager
  db_subnet_group_name = aws_db_subnet_group.default.name
  vpc_security_group_ids = [aws_security_group.rds.id]
  skip_final_snapshot  = true
  publicly_accessible  = false # For simplicity in this test
}