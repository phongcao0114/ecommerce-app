#!/bin/bash

# =============================
# Deploy Script
# =============================
#
# This script deploys the application using prebuilt images from Docker Hub
# Run this on your production server
#

set -e  # Exit on any error

echo "🚀 Starting deployment process..."

# Pull latest images from Docker Hub
echo "📥 Pulling latest images from Docker Hub..."
docker-compose -f docker-compose.deploy.yml pull

# Stop existing containers
echo "🛑 Stopping existing containers..."
docker-compose -f docker-compose.deploy.yml down

# Start containers with new images
echo "▶️  Starting containers with latest images..."
docker-compose -f docker-compose.deploy.yml up -d

# Wait for services to be ready
echo "⏳ Waiting for services to be ready..."
sleep 10

# Check service status
echo "🔍 Checking service status..."
docker-compose -f docker-compose.deploy.yml ps

echo "✅ Deployment completed successfully!"
echo "🌐 Your application should be available at:"
echo "   Frontend: http://your-server-ip"
echo "   Backend API: http://your-server-ip:8080"
echo ""
echo "📋 Useful commands:"
echo "   View logs: docker-compose -f docker-compose.deploy.yml logs -f"
echo "   Stop services: docker-compose -f docker-compose.deploy.yml down"
echo "   Restart services: docker-compose -f docker-compose.deploy.yml restart" 