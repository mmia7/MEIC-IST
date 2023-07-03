terraform {
  required_version = ">= 1.0.0, < 2.0.0"
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 4.0"
    }
  }
}

provider "aws" {
  region = "us-east-1"
  access_key  = "ASIAV3COPWEHSGYKX4AQ"
  secret_key  = "Y1ieewet3UN7/iMEKhzGhNxqUflRDbZtKmGGnxuv"
  token = "FwoGZXIvYXdzEEYaDChYt4uXNRtqZs/YbyLSAcJC3WU7HNLqqdUG/BLQ9Y06K/yV3UNQcMKmJDl2nLO5IZXWfPTo+6avCW6h+LOHzNYjNINF7hp6L+Uaq/UWsrDvGdiZSnLLZe2FdHyERm38+mxYW71mM2ydStlawFebMSnwmbOlg3EVmTvihHULZCmcec1FxmdQFpOUJwJZhRDcqWmBka31H/4agGCLOVk0lQeK73QG2UthJsF4v1pV5iKLzMp1itQvHC4xtWVxbE1gdrIlw8qMvCDQV6uAo9BDTj5jOwgvGDGjr0LUXgXDFTU/1CiCxJijBjItljQz/Kf7zzgQhGPHAsyCajvA9z87uUasNHkCPjIXghIFUUQUrjHRplyNLorP"
}

variable "db_username" {
  description = "The username for the database"
  type        = string
  sensitive   = true
  default     = "user"
}

variable "db_password" {
  description = "The password for the database"
  type        = string
  sensitive   = true
  default     = "password"
}

variable "db_name" {
  description = "The name to use for the database"
  type        = string
  default     = "AVaaS"
}

resource "aws_db_instance" "example" {
  identifier_prefix   = "terraform-up-and-running"
  engine              = "mysql"
  allocated_storage   = 10
  instance_class      = "db.t2.micro"
  skip_final_snapshot = true
  publicly_accessible = true
  vpc_security_group_ids  = [aws_security_group.rds.id]
  db_name             = var.db_name
  username = var.db_username
  password = var.db_password
}

output "address" {
  value       = aws_db_instance.example.address
  description = "Connect to the database at this endpoint"
}

output "port" {
  value       = aws_db_instance.example.port
  description = "The port the database is listening on"
}

resource "aws_security_group" "rds" {
  name = var.security_group_name
  ingress {
    from_port   = 3306
    to_port     = 3306
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  egress {
    from_port        = 0
    to_port          = 0
    protocol         = "-1"
    cidr_blocks      = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
  }
}

variable "security_group_name" {
  description = "The name of the security group"
  type        = string
  default     = "terraform-rds-instance"
}