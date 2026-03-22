-- PostgreSQL Database Setup Script for AppEvent
-- Run this script in PostgreSQL as a superuser (postgres)

-- Create database
CREATE DATABASE iset_events_db;

-- Create user with password
CREATE USER iset_events_db WITH PASSWORD 'maram';

-- Grant privileges
GRANT ALL PRIVILEGES ON DATABASE iset_events_db TO iset_events_db;

-- Grant schema privileges
\c iset_events_db;
GRANT ALL ON SCHEMA public TO iset_events_db;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO iset_events_db;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO iset_events_db;

-- Set default privileges
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO iset_events_db;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO iset_events_db;
