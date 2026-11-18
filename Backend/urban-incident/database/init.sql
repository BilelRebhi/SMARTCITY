-- Database initialization script for urban_incident microservice

-- Create database
CREATE DATABASE IF NOT EXISTS urban_incident_db;

-- Create user
CREATE USER IF NOT EXISTS urban_user WITH PASSWORD 'urban_pass';

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE urban_incident_db TO urban_user;
