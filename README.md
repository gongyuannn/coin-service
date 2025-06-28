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
  
### Build & Run Locally  
```bash
# 1. Package the fat JAR
mvn clean package

# 2. Run the server (with default config)
java -jar target/coin-service-1.0-SNAPSHOT.jar server config.yml

# API endpoint:
# POST http://localhost:8080/coins/min
