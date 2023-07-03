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
  region      = "us-east-1"
  access_key  = "ASIAV3COPWEH6UX37AMV"
  secret_key  = "OCQqxCAbuwjheTh5w9LfB4+k+OaEmSnJZkizwgED"
  token = "FwoGZXIvYXdzEPf//////////wEaDCjsJfjRwe5x6WyMtSLSAZVQg1jKGnLdOwbTrUswQ105RP2mTHNN6jTN29JheOAfq4ylpwSlTfWzkpL9V6+E7djh7pQ/Tx5GlTgEwt7ZHgDwmf1fKuFZIAN8WhTmmtOzmdsGD3Pmwre5Q54JY2WlihOqKrdo2fnJfVSqVdncmgZ7L4arnMBrkinnR+L1plGg4dk5tt/0rtUb/uTQvtJV2CX4L5x17E61n6bgLt7z5cA3nDoBUEg/rGnFLzuyq/9wqrQ44pIM5TfX2bqEMBDCgoaSk7s/z5HGiM5myBSbJBU5FCj2x/ejBjIt2s8jMVUgv9Fg29BC7ULnsEp687gtRLZRRtcaufQBSoWRIE9pXoscvcjw8qKR"
}

resource "aws_instance" "projectInstallCamundaEngine" {
  ami                     = "ami-0b5eea76982371e91"
  instance_type           = "t2.small"
  vpc_security_group_ids  = [aws_security_group.instance.id]
  key_name                = "EI2023Tutorial"

  user_data = "${file("deploy.sh")}"

  user_data_replace_on_change = true
  
  tags = {
    Name = "terraform-Camunda"
  }
}

resource "aws_security_group" "instance" {
  name = var.security_group_name
  ingress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
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
  description = "Camunda security group"
  type        = string
  default     = "terraform-Camunda-instance2"
}

output "address" {
  value       = aws_instance.projectInstallCamundaEngine.public_dns
  description = "Connect to the database at this endpoint"
}
