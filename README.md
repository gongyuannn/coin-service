# Coin Service
A Dropwizard-based REST API that computes the minimum number of coins needed to make up a target amount.

## Overview  
Given a **target amount** (0–10,000.00 dollars inclusive) and a list of coin denominations, this service returns the smallest list of coins that sums exactly to the target. If no combination is possible, an error is returned.

## Notes & Assumptions  
- **Amount range**: 0 through 10,000.00 inclusive (dollars) 
- **Test cases**:  
  - Input of 0
  - Out-of-range input 
  - Amount cannot be formed with given denominations
- **Algorithm**: Greedy approach (dynamic programming was considered but not implemented due to constraints)
- Dockerfile is also included as "Dockerfile"
  
### Build & Run Locally  
```bash
# 1. Package the fat JAR
mvn clean package

# 2. Run the server (with default config)
java -jar target/coin-service-1.0-SNAPSHOT.jar server config.yml

# 3. Test endpoint
curl http://localhost:8080/coins/min \
  -H "Content-Type: application/json" \
  -d '{"targetAmount":103,"denominations":[1,2,50]}'
# → [1,2,50,50]
```

### Docker Build 
```bash
# Note: Update 'youruser' to your own username
# 1. Clone this repo and CD into it
git clone git@github.com:youruser/coin-service.git (note that this is SSH way)
cd coin-service

# 2. Standard build (builds for your local platform)
docker build -t youruser/coin-service:latest .

# If your machine arch differs from target, use:
docker buildx create --use --name builder
docker buildx inspect --bootstrap
docker buildx build \
  --platform linux/amd64 \
  -t youruser/coin-service:latest \
  --push \
  .

# 3. Run in detached mode, restart on reboot, map to port 8080
docker run -d \
  --name coin-service \
  --restart unless-stopped \
  -p 8080:8080 \
  youruser/coin-service:latest

```
