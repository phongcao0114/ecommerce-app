#!/bin/bash

# =============================
# Oracle Cloud Free Tier VM Setup Script
# =============================
#
# Run this script on your Oracle Cloud VM after connecting via SSH
# This script installs Docker, Docker Compose, and sets up the deployment
#

set -e  # Exit on any error

echo "🚀 Setting up Oracle Cloud VM for ecommerce app deployment..."

# Update system
echo "📦 Updating system packages..."
sudo apt-get update
sudo apt-get upgrade -y

# Install required packages
echo "📦 Installing required packages..."
sudo apt-get install -y \
    apt-transport-https \
    ca-certificates \
    curl \
    gnupg \
    lsb-release \
    git

# Add Docker's official GPG key
echo "🔑 Adding Docker GPG key..."
curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /usr/share/keyrings/docker-archive-keyring.gpg

# Add Docker repository
echo "📦 Adding Docker repository..."
echo \
  "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.gpg] https://download.docker.com/linux/ubuntu \
  $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null

# Install Docker
echo "🐳 Installing Docker..."
sudo apt-get update
sudo apt-get install -y docker-ce docker-ce-cli containerd.io

# Add current user to docker group
echo "👤 Adding user to docker group..."
sudo usermod -aG docker $USER

# Install Docker Compose
echo "📦 Installing Docker Compose..."
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# Create application directory
echo "📁 Creating application directory..."
mkdir -p ~/ecommerce-app
cd ~/ecommerce-app

# Download deployment files
echo "📥 Downloading deployment files..."
curl -O https://raw.githubusercontent.com/your-username/your-repo/main/docker-compose.deploy.yml
curl -O https://raw.githubusercontent.com/your-username/your-repo/main/deploy.sh
chmod +x deploy.sh

# Create uploads directory for persistent storage
echo "📁 Creating uploads directory..."
mkdir -p uploads

echo "✅ Oracle Cloud VM setup completed!"
echo ""
echo "🎯 Next steps:"
echo "   1. Logout and login again for docker group changes to take effect"
echo "   2. Test Docker: docker --version"
echo "   3. Test Docker Compose: docker-compose --version"
echo "   4. Deploy the app: ./deploy.sh"
echo ""
echo "📋 Useful commands:"
echo "   View logs: docker-compose -f docker-compose.deploy.yml logs -f"
echo "   Stop app: docker-compose -f docker-compose.deploy.yml down"
echo "   Update app: docker-compose -f docker-compose.deploy.yml pull && docker-compose -f docker-compose.deploy.yml up -d" 