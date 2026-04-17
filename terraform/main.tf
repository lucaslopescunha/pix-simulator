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

# 1. VPC configurada com Subnets Públicas e Privadas
module "vpc" {
  source  = "terraform-aws-modules/vpc/aws"
  version = "5.0.0"

  name = "pix-simulator-vpc"
  cidr = "10.0.0.0/16"

  azs             = ["us-east-1a", "us-east-1b"]
  public_subnets  = ["10.0.1.0/24", "10.0.2.0/24"]   # Onde ficará o LoadBalancer
  private_subnets = ["10.0.10.0/24", "10.0.11.0/24"] # Onde ficarão as máquinas (Nodes)

  # Desativamos o IP público automático nos nós
  map_public_ip_on_launch = false

  # NAT Gateway: Essencial para os nós na rede privada acessarem o Docker Hub
  enable_nat_gateway = true
  single_nat_gateway = true # Mantém o custo menor (apenas 1 NAT para o cluster)
}

# 2. Cluster EKS configurado para usar a rede privada
module "eks" {
  source  = "terraform-aws-modules/eks/aws"
  version = "~> 20.0"

  cluster_name    = "pix-simulator-cluster"
  cluster_version = "1.30"

  vpc_id     = module.vpc.vpc_id
  # Importante: Os nós ficam nas subnets PRIVADAS
  subnet_ids = module.vpc.private_subnets

  # O endpoint do cluster pode ser público para você gerenciar do seu terminal
  cluster_endpoint_public_access = true

  enable_cluster_creator_admin_permissions = true

  eks_managed_node_groups = {
    main = {
      instance_types = ["t3.medium"]
      min_size     = 1
      max_size     = 2
      desired_size = 1
    }
  }
}

output "cluster_name" {
  value = module.eks.cluster_name
}
