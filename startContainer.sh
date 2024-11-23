# Load the .env file into the shell to make the variables available
export $(cat .env | xargs)

# Stop and remove the existing container if it exists
docker rm -f product-api || true

# Build the image (if needed, assuming Dockerfile is present)
docker build -t product-api:latest .

# Run the container with the .env file
docker run -d \
  --name product-api \
  -e FLYWAY_DB_PASSWORD=$PRODUCT_DB_ADMIN_PASSWORD \
  -e SA_DB_PASSWORD=$PRODUCT_DB_SA_PASSWORD \
  -p 6868:8080 \
  -p 6869:8081 \
  --network product-api_product-api-network \
  product-api:latest