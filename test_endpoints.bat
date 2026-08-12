@echo off
set BASE_URL=http://localhost:8080
set CONTENT_TYPE=Content-Type: application/json

echo === Testing Store Application Endpoints ===

:: --- Customer Endpoints ---
echo.
echo --- Customer Endpoints ---

echo 1. POST /customer - Create a new customer
curl -X POST "%BASE_URL%/customer" ^
     -H "%CONTENT_TYPE%" ^
     -d "{\"name\": \"Windows Test User\"}"
echo.
echo.

echo 2. GET /customer - Get all customers
curl -X GET "%BASE_URL%/customer"
echo.
echo.

echo 3. GET /customer/{name} - Get customer by name (or substring match)
curl -X GET "%BASE_URL%/customer/Windows"
echo.
echo.

echo 3.1 GET /customer/{name} - Get customer by full name
curl -X GET "%BASE_URL%/customer/Windows%%20Test%%20User"
echo.
echo.

:: --- Product Endpoints ---
echo.
echo --- Product Endpoints ---

echo 4. POST /products - Create a new product
curl -X POST "%BASE_URL%/products" ^
     -H "%CONTENT_TYPE%" ^
     -d "{\"description\": \"Windows Test Product\"}"
echo.
echo.

echo 5. GET /products - Get all products
curl -X GET "%BASE_URL%/products"
echo.
echo.

echo 6. GET /products/1 - Get product by ID (assuming ID 1 exists)
curl -X GET "%BASE_URL%/products/1"
echo.
echo.

:: --- Order Endpoints ---
echo.
echo --- Order Endpoints ---

echo 7. POST /order - Create a new order
curl -X POST "%BASE_URL%/order" ^
     -H "%CONTENT_TYPE%" ^
     -d "{\"description\": \"Windows Test Order\"}"
echo.
echo.

echo 8. GET /order - Get all orders
curl -X GET "%BASE_URL%/order"
echo.
echo.

echo 9. GET /order/1 - Get order by ID (assuming ID 1 exists)
curl -X GET "%BASE_URL%/order/1"
echo.
echo.

echo === End of Tests ===
