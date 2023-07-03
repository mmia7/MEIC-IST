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
  region     = "us-east-1"
  access_key = "ASIA2TCAWQB6IFJLU4MI"
  secret_key = "ZZ9N2r1bpCqyLQxPI6vx0Io6deNYDQpyVkoMwqfk"
  token      = "FwoGZXIvYXdzEHwaDEFqHuSTV+v33/dp6SLSAR/tPaWG1Gl0gJ8egs1OTlt6PAFpoUYPbfHs27oG73nnO8kC5wmDy5gTEUWESg59scs3iZ5roGpctbRuhO0Ih3VjxGmveu5IpNcTPdes0Xq1fsqc+4Deib3hssli35kDkhxgO6Sq1m40eyPQtFn9nVKY4CL05M39NjG8fCiVhip6qTmFL0+zYqyDUs8oFSoK0m3IdXbLKENScqUudwvy0LZJobke4Jus+VCysf95Nua/BWc0vBBUKYLiCSVv/wszUK/5kYnYG0LIfvOhlS1pkNtuCijonKSjBjItUo1S0eCBfoUvQhGsdQoL9jewGPfNqMdGbnCf4N9nATede7FpWW8xdpd3FyOo"
}

variable "nBroker" {
  description = "number of brokers"
  type        = number
  default     = 3
}

resource "aws_instance" "projectCluster" {
  ami                    = "ami-0b5eea76982371e91"
  instance_type          = "t2.small"
  count                  = var.nBroker
  vpc_security_group_ids = [aws_security_group.instance.id]
  key_name               = "EI2023Sprint1"
  user_data              = base64encode(templatefile("creation.sh", { idBroker = "${count.index}", totalBrokers = var.nBroker }))
  user_data_replace_on_change = true

  tags = {
    Name = "terraform-example-kafka.${count.index}"
  }
}

output "publicdnslist" {
  value = formatlist("%v", aws_instance.projectCluster.*.public_dns)
}

resource "aws_security_group" "instance" {
  name = var.security_group_name

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 2181
    to_port     = 2181
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 2888
    to_port     = 2888
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 3888
    to_port     = 3888
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 9092
    to_port     = 9092
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port       = 0
    to_port         = 0
    protocol        = "-1"
    cidr_blocks     = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
  }
}

variable "security_group_name" {
  description = "The name of the security group"
  type        = string
  default     = "terraform-example-instance"
}
