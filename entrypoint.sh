#!/bin/bash
set -e

# Start PostgreSQL in the background
docker-entrypoint.sh postgres -c wal_level=logical -p 5433 &

# Wait for PostgreSQL to start
echo "Waiting for PostgreSQL to start..."
until pg_isready -p 5433 -U admin -d store; do
  sleep 1
done
echo "PostgreSQL started"

# Start the application
java -jar app.jar
