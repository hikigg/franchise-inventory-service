resource "aws_ecr_repository" "app" {
  name         = "franchise-inventory-service"
  force_delete = true
}