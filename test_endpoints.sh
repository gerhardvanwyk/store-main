#!/bin/bash

# Configuration
BASE_URL="http://localhost:8080"
CONTENT_TYPE="Content-Type: application/json"

echo "=== Testing Store Application Endpoints ==="

# --- Customer Endpoints ---
echo -e "\n--- Customer Endpoints ---"

echo "1. POST /customer - Create a new customer"
curl -X POST "$BASE_URL/customer" \
     -H "$CONTENT_TYPE" \
     -d '{"name": "Bash Test User"}'
echo -e "\n"

echo "2. GET /customer - Get all customers"
curl -X GET "$BASE_URL/customer"
echo -e "\n"

echo "3. GET /customer/{name} - Get customer by name (or substring match)"
curl -X GET "$BASE_URL/customer/Bash"
echo -e "\n"

echo "3.1 GET /customer/{name} - Get customer by full name"
curl -X GET "$BASE_URL/customer/Bash%20Test%20User"
echo -e "\n"

# --- Product Endpoints ---
echo -e "\n--- Product Endpoints ---"

echo "4. POST /products - Create a new product"
# We capture the output to potentially get an ID, though for this script we just show result
curl -X POST "$BASE_URL/products" \
     -H "$CONTENT_TYPE" \
     -d '{"description": "Bash Test Product"}'
echo -e "\n"

echo "5. GET /products - Get all products"
curl -X GET "$BASE_URL/products"
echo -e "\n"

echo "6. GET /products/1 - Get product by ID (assuming ID 1 exists)"
curl -X GET "$BASE_URL/products/1"
echo -e "\n"

# --- Order Endpoints ---
echo -e "\n--- Order Endpoints ---"

echo "7. POST /order - Create a new order"
curl -X POST "$BASE_URL/order" \
     -H "$CONTENT_TYPE" \
     -d '{"description": "Bash Test Order"}'
echo -e "\n"

echo "8. GET /order - Get all orders"
curl -X GET "$BASE_URL/order"
echo -e "\n"

echo "9. GET /order/1 - Get order by ID (assuming ID 1 exists)"
curl -X GET "$BASE_URL/order/1"
echo -e "\n"

echo "=== End of Tests ==="
