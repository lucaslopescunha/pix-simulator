terraform {
  required_version = ">= 1.5.0"

  backend "s3" {
    bucket  = "pix-simulator-s3"
    key     = "pix-simulator/terraform.tfstate"
    region  = "us-east-1"
    encrypt = true
  }

  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = "us-east-1"
}

#################################################
# 1. VPC com subnets públicas para ALB
#    e privadas para ECS Fargate
#################################################

module "vpc" {
  source  = "terraform-aws-modules/vpc/aws"
  version = "5.0.0"

  name = "pix-simulator-vpc"
  cidr = "10.0.0.0/16"

  azs = [
    "us-east-1a",
    "us-east-1b"
  ]

  # Load Balancer público
  public_subnets = [
    "10.0.1.0/24",
    "10.0.2.0/24"
  ]

  # Tasks do ECS Fargate
  private_subnets = [
    "10.0.10.0/24",
    "10.0.11.0/24"
  ]

  enable_nat_gateway = true
  single_nat_gateway = true
  enable_vpn_gateway = false

  map_public_ip_on_launch = false
}

#################################################
# 2. ECS Cluster
#################################################

resource "aws_ecs_cluster" "main" {
  name = "pix-simulator-cluster"
}

#################################################
# 3. Security Group do ALB
#################################################

resource "aws_security_group" "alb_sg" {
  name        = "pix-simulator-alb-sg"
  description = "Security Group do ALB"
  vpc_id      = module.vpc.vpc_id

  ingress {
    description = "Public HTTP access"
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

#################################################
# 4. Security Group do ECS Service
#################################################

resource "aws_security_group" "ecs_sg" {
  name        = "pix-simulator-ecs-sg"
  description = "Security Group do ECS Fargate"
  vpc_id      = module.vpc.vpc_id

  ingress {
    description     = "Traffic from ALB"
    from_port       = 8080
    to_port         = 8080
    protocol        = "tcp"
    security_groups = [aws_security_group.alb_sg.id]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

#################################################
# 5. Application Load Balancer
#################################################

resource "aws_lb" "main" {
  name               = "pix-simulator-alb"
  internal           = false
  load_balancer_type = "application"

  security_groups = [
    aws_security_group.alb_sg.id
  ]

  subnets = module.vpc.public_subnets
}

resource "aws_lb_target_group" "main" {
  name        = "pix-simulator-tg"
  port        = 8080
  protocol    = "HTTP"
  target_type = "ip"
  vpc_id      = module.vpc.vpc_id

  health_check {
    path = "/actuator/health"
  }
}

resource "aws_lb_listener" "http" {
  load_balancer_arn = aws_lb.main.arn
  port              = 80
  protocol          = "HTTP"

  default_action {
    type = "forward"
    target_group_arn = aws_lb_target_group.main.arn
  }
}

#################################################
# 6. IAM Role para ECS Task Execution
#################################################

resource "aws_iam_role" "ecs_task_execution_role" {
  name = "pix-simulator-ecs-task-execution-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Action = "sts:AssumeRole"
        Effect = "Allow"
        Principal = {
          Service = "ecs-tasks.amazonaws.com"
        }
      }
    ]
  })
}

resource "aws_iam_role_policy_attachment" "ecs_task_execution" {
  role       = aws_iam_role.ecs_task_execution_role.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AmazonECSTaskExecutionRolePolicy"
}

#################################################
# 7. Task Definition
#################################################

resource "aws_ecs_task_definition" "app" {
  family                   = "pix-simulator-task"
  requires_compatibilities = ["FARGATE"]
  network_mode             = "awsvpc"

  cpu    = "512"
  memory = "1024"
  execution_role_arn = aws_iam_role.ecs_task_execution_role.arn

  container_definitions = jsonencode([
    {
      name  = "pix-simulator"
      image = "lucaslopescunha/pix-simulator:latest"

      essential = true

      portMappings = [
        {
          containerPort = 8080
          hostPort      = 8080
          protocol      = "tcp"
        }
      ]
    }
  ])
}

#################################################
# 8. ECS Service com Fargate
#################################################

resource "aws_ecs_service" "app" {
  name            = "pix-simulator-service"
  cluster         = aws_ecs_cluster.main.id
  task_definition = aws_ecs_task_definition.app.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  depends_on = [
    aws_lb_listener.http
  ]

  network_configuration {
    subnets = module.vpc.private_subnets

    security_groups = [
      aws_security_group.ecs_sg.id
    ]

    assign_public_ip = false
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.main.arn
    container_name   = "pix-simulator"
    container_port   = 8080
  }
}

#################################################
# Outputs
#################################################

output "ecs_cluster_name" {
  value = aws_ecs_cluster.main.name
}

output "alb_dns_name" {
  value = aws_lb.main.dns_name
}