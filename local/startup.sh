# Stop and remove the existing container if it exists
docker rm -f product-api || true

# Build the image (Dockerfile one dir up)
docker build -t product-api:latest -f ../Dockerfile ../

# Run the container with the .env file
docker run -d \
  --name product-api \
  -e FLYWAY_DB_PASSWORD=product_api_pwd \
  -e SA_DB_PASSWORD=service_account_password \
  -p 6868:8080 \
  -p 6869:8081 \
  --network damo_network \
  product-api:latest