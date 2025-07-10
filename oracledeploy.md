# 🚀 Ecommerce App - Oracle Cloud Free Tier Deployment Guide

A complete guide for deploying the Spring Boot + Angular ecommerce application to Oracle Cloud Free Tier using Docker containers.

## 📋 Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Local Development](#local-development)
- [Oracle Cloud Setup](#oracle-cloud-setup)
- [Deployment](#deployment)
- [Updates & Maintenance](#updates--maintenance)
- [Troubleshooting](#troubleshooting)

## 🎯 Overview

This guide shows you how to:
- Build Docker images locally
- Push images to Docker Hub
- Deploy to Oracle Cloud Free Tier VM
- Update the application when code changes

### **Tech Stack:**
- **Backend**: Spring Boot (Java 17) + MySQL 8.0
- **Frontend**: Angular 17
- **Database**: MySQL 8.0
- **Containerization**: Docker + Docker Compose
- **Cloud**: Oracle Cloud Free Tier

## 🏗️ Architecture

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Local Dev     │    │   Docker Hub    │    │ Oracle Cloud VM │
│                 │    │                 │    │                 │
│ ┌─────────────┐ │    │ ┌─────────────┐ │    │ ┌─────────────┐ │
│ │ Build Images│ │───▶│ │ Store Images│ │───▶│ │ Pull Images │ │
│ └─────────────┘ │    │ └─────────────┘ │    │ └─────────────┘ │
│ ┌─────────────┐ │    │                 │    │ ┌─────────────┐ │
│ │ Push Images │ │───▶│                 │    │ │ Run App     │ │
│ └─────────────┘ │    │                 │    │ └─────────────┘ │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### **Docker Images:**
- `jackwind9000/ecommerce-backend:latest`
- `jackwind9000/ecommerce-frontend:latest`

## ✅ Prerequisites

### **Local Development:**
- Docker Desktop
- Docker Compose
- Git
- Java 17 (for local backend development)
- Node.js 18+ (for local frontend development)

### **Oracle Cloud Account:**
- Oracle Cloud Free Tier account
- SSH key pair for VM access

## 💻 Local Development

### **1. Clone and Setup**
```bash
git clone <your-repo-url>
cd ecommerceapp-update-order
```

### **2. Local Development**
```bash
# Start local development environment
docker-compose -f docker-compose.build.yml up --build

# Access the application
# Frontend: http://localhost:4200
# Backend: http://localhost:8080
# Admin: admin@admin.com / admin123
```

### **3. Build and Push Images**
```bash
# Make scripts executable
chmod +x build-and-push.sh deploy.sh

# Build and push to Docker Hub
./build-and-push.sh
```

## ☁️ Oracle Cloud Setup

### **Step 1: Create VM Instance**

1. **Login to Oracle Cloud Console**
   - Go to [Oracle Cloud Console](https://cloud.oracle.com)
   - Sign in with your free tier account

2. **Launch VM Instance**
   - Navigate to **Compute** → **Instances**
   - Click **Create Instance**
   - Configure:
     ```
     Name: ecommerce-app-vm
     Image: Canonical Ubuntu 22.04
     Shape: VM.Standard.A1.Flex
     Memory: 4GB
     OCPUs: 2
     Public IP: Yes
     ```

3. **Configure Security Lists**
   - Go to **Networking** → **Virtual Cloud Networks**
   - Select your VCN → **Security Lists**
   - Edit default security list
   - Add ingress rules:

   | Source | Port | Description |
   |--------|------|-------------|
   | 0.0.0.0/0 | 22 | SSH |
   | 0.0.0.0/0 | 80 | HTTP (Frontend) |
   | 0.0.0.0/0 | 8080 | Backend API |

### **Step 2: Connect to VM**
```bash
# Connect via SSH (replace with your VM's public IP)
ssh ubuntu@YOUR_VM_PUBLIC_IP
```

### **Step 3: Install Docker and Setup**
```bash
# Download and run setup script
curl -O https://raw.githubusercontent.com/your-username/your-repo/main/setup-oracle-vm.sh
chmod +x setup-oracle-vm.sh
./setup-oracle-vm.sh

# Logout and login again for docker group changes
exit
ssh ubuntu@YOUR_VM_PUBLIC_IP

# Test Docker installation
docker --version
docker-compose --version
docker run hello-world
```

## 🚀 Deployment

### **Step 1: Download Deployment Files**
```bash
cd ~/ecommerce-app

# Download deployment files
curl -O https://raw.githubusercontent.com/your-username/your-repo/main/docker-compose.deploy.yml
curl -O https://raw.githubusercontent.com/your-username/your-repo/main/deploy.sh
chmod +x deploy.sh
```

### **Step 2: Deploy Application**
```bash
# Deploy the application
./deploy.sh
```

### **Step 3: Verify Deployment**
```bash
# Check running containers
docker ps

# Check application logs
docker-compose -f docker-compose.deploy.yml logs -f
```

### **Step 4: Access Your Application**
- **Frontend**: `http://YOUR_VM_PUBLIC_IP`
- **Backend API**: `http://YOUR_VM_PUBLIC_IP:8080`
- **Admin Login**: `admin@admin.com` / `admin123`

## 🔄 Updates & Maintenance

### **When Code Changes:**

#### **On Your Local Machine:**
```bash
# 1. Make code changes
# 2. Test locally
docker-compose -f docker-compose.build.yml up --build

# 3. Build and push new images
./build-and-push.sh
```

#### **On Oracle Cloud VM:**
```bash
# Pull latest images and restart
docker-compose -f docker-compose.deploy.yml pull
docker-compose -f docker-compose.deploy.yml up -d
```

### **Useful Commands:**

```bash
# View logs
docker-compose -f docker-compose.deploy.yml logs -f

# Check resource usage
docker stats

# Backup database
docker exec mysql_db mysqldump -u root -pYourStrong!Passw0rd ecommerce_app > backup.sql

# Restart services
docker-compose -f docker-compose.deploy.yml restart

# Stop all services
docker-compose -f docker-compose.deploy.yml down

# Update to specific version
docker-compose -f docker-compose.deploy.yml pull jackwind9000/ecommerce-backend:20241201_143022
docker-compose -f docker-compose.deploy.yml up -d
```

## 🔧 Troubleshooting

### **Common Issues:**

#### **1. Port 80 Not Accessible**
```bash
# Check if containers are running
docker ps

# Check security list rules in Oracle Cloud Console
# Ensure port 80 is open in security list
```

#### **2. Docker Permission Denied**
```bash
# Add user to docker group
sudo usermod -aG docker $USER

# Logout and login again
exit
ssh ubuntu@YOUR_VM_PUBLIC_IP
```

#### **3. Out of Memory**
```bash
# Check memory usage
free -h
docker stats

# Restart containers
docker-compose -f docker-compose.deploy.yml restart

# Or upgrade VM shape in Oracle Cloud Console
```

#### **4. Database Connection Issues**
```bash
# Check MySQL container logs
docker logs mysql_db

# Restart database
docker-compose -f docker-compose.deploy.yml restart db
```

#### **5. Application Not Starting**
```bash
# Check all container logs
docker-compose -f docker-compose.deploy.yml logs

# Check specific service logs
docker-compose -f docker-compose.deploy.yml logs backend
docker-compose -f docker-compose.deploy.yml logs frontend
```

### **Logs Location:**
```bash
# Application logs
docker-compose -f docker-compose.deploy.yml logs

# Docker system logs
sudo journalctl -u docker

# Container logs
docker logs <container-name>
```

## 📊 Monitoring

### **Resource Monitoring:**
- **Oracle Cloud Console** → **Monitoring** → **Metrics**
- **Docker Stats**: `docker stats`
- **System Resources**: `htop`, `free -h`, `df -h`

### **Application Monitoring:**
- **Health Check**: `http://YOUR_VM_PUBLIC_IP:8080/health`
- **Database**: `docker exec -it mysql_db mysql -u root -p`

## 💾 Backup & Recovery

### **Database Backup:**
```bash
# Create backup
docker exec mysql_db mysqldump -u root -pYourStrong!Passw0rd ecommerce_app > backup_$(date +%Y%m%d_%H%M%S).sql

# Restore backup
docker exec -i mysql_db mysql -u root -pYourStrong!Passw0rd ecommerce_app < backup.sql
```

### **Uploads Backup:**
```bash
# Backup uploads directory
tar -czf uploads_backup_$(date +%Y%m%d_%H%M%S).tar.gz uploads/

# Restore uploads
tar -xzf uploads_backup.tar.gz
```

## 🔒 Security Considerations

### **Production Security:**
1. **Change Default Passwords**
   - Update MySQL root password
   - Update application admin password
   - Use strong passwords

2. **Network Security**
   - Configure firewall rules
   - Use HTTPS (SSL/TLS)
   - Restrict SSH access

3. **Container Security**
   - Keep images updated
   - Scan for vulnerabilities
   - Use non-root users in containers

## 📈 Scaling Considerations

### **Oracle Cloud Free Tier Limits:**
- **Compute**: 2 AMD-based VMs or 4 ARM-based VMs
- **Memory**: Up to 24GB total
- **Storage**: 200GB total
- **Bandwidth**: 10TB/month

### **Optimization Tips:**
1. **Use ARM-based VMs** (more cost-effective)
2. **Monitor resource usage** regularly
3. **Optimize images** (multi-stage builds)
4. **Use volume mounts** for persistent data

## 📞 Support

### **Useful Resources:**
- [Oracle Cloud Documentation](https://docs.oracle.com/en-us/iaas/Content/home.htm)
- [Docker Documentation](https://docs.docker.com/)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Angular Documentation](https://angular.io/docs)

### **Community:**
- [Oracle Cloud Community](https://community.oracle.com/tech/developers/categories/cloud-infrastructure)
- [Docker Community](https://community.docker.com/)

---

## 🎉 Success!

Your ecommerce application is now deployed on Oracle Cloud Free Tier! 

**Next Steps:**
1. Customize the application for your needs
2. Set up monitoring and alerts
3. Implement backup strategies
4. Consider upgrading to paid tier for production use

**Happy Deploying! 🚀** 