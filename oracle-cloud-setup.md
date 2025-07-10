# Oracle Cloud Free Tier Setup Guide

## 1. Create VM Instance

### **Step 1: Launch VM Instance**
1. Log into Oracle Cloud Console
2. Navigate to **Compute** → **Instances**
3. Click **Create Instance**
4. Configure the instance:
   - **Name**: `ecommerce-app-vm`
   - **Image**: Canonical Ubuntu 22.04
   - **Shape**: VM.Standard.A1.Flex (ARM-based, 4GB RAM, 2 OCPUs)
   - **Memory**: 4GB
   - **OCPUs**: 2
   - **Networking**: Create new VCN or use existing
   - **Public IP**: Yes (assign public IP)

### **Step 2: Configure Security Lists**
1. Go to **Networking** → **Virtual Cloud Networks**
2. Select your VCN
3. Click **Security Lists**
4. Edit the default security list
5. Add these ingress rules:

| Source | Port | Description |
|--------|------|-------------|
| 0.0.0.0/0 | 22 | SSH |
| 0.0.0.0/0 | 80 | HTTP (Frontend) |
| 0.0.0.0/0 | 8080 | Backend API |

### **Step 3: Connect to VM**
```bash
# Connect via SSH (replace with your VM's public IP)
ssh ubuntu@YOUR_VM_PUBLIC_IP
```

## 2. Install Docker and Deploy

### **Step 1: Run Setup Script**
```bash
# Download and run the setup script
curl -O https://raw.githubusercontent.com/your-username/your-repo/main/setup-oracle-vm.sh
chmod +x setup-oracle-vm.sh
./setup-oracle-vm.sh
```

### **Step 2: Logout and Login Again**
```bash
# Logout to apply docker group changes
exit

# SSH back in
ssh ubuntu@YOUR_VM_PUBLIC_IP
```

### **Step 3: Test Docker Installation**
```bash
# Test Docker
docker --version
docker-compose --version

# Test Docker without sudo
docker run hello-world
```

## 3. Deploy the Application

### **Step 1: Download Deployment Files**
```bash
cd ~/ecommerce-app

# Download the deployment compose file
curl -O https://raw.githubusercontent.com/your-username/your-repo/main/docker-compose.deploy.yml

# Download the deploy script
curl -O https://raw.githubusercontent.com/your-username/your-repo/main/deploy.sh
chmod +x deploy.sh
```

### **Step 2: Deploy the App**
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

## 4. Access Your Application

- **Frontend**: `http://YOUR_VM_PUBLIC_IP`
- **Backend API**: `http://YOUR_VM_PUBLIC_IP:8080`
- **Admin Login**: `admin@admin.com` / `admin123`

## 5. Update Application (When Code Changes)

### **On Your Local Machine:**
```bash
# Build and push new images
./build-and-push.sh
```

### **On Oracle Cloud VM:**
```bash
# Pull latest images and restart
docker-compose -f docker-compose.deploy.yml pull
docker-compose -f docker-compose.deploy.yml up -d
```

## 6. Monitoring and Maintenance

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
```

### **Resource Monitoring:**
- Oracle Cloud Free Tier provides 2 AMD-based Compute VMs or 4 ARM-based Compute VMs
- Your app uses 1 ARM-based VM with 4GB RAM and 2 OCPUs
- Monitor usage in Oracle Cloud Console → **Monitoring** → **Metrics**

## 7. Troubleshooting

### **Common Issues:**

1. **Port 80 not accessible**: Check security list rules
2. **Docker permission denied**: Logout and login again after setup
3. **Out of memory**: Restart containers or upgrade VM shape
4. **Database connection issues**: Check MySQL container logs

### **Logs Location:**
```bash
# Application logs
docker-compose -f docker-compose.deploy.yml logs

# Docker system logs
sudo journalctl -u docker
``` 