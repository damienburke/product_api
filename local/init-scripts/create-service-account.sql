DO $$
BEGIN

    CREATE ROLE service_account WITH LOGIN PASSWORD 'service_account_password';

    -- Grant permissions on all tables in the public schema
    GRANT CONNECT ON DATABASE product_db TO service_account;
    GRANT USAGE ON SCHEMA public TO service_account;
    GRANT SELECT, INSERT, UPDATE ON ALL TABLES IN SCHEMA public TO service_account;
    GRANT USAGE, SELECT, UPDATE ON ALL SEQUENCES IN SCHEMA public TO service_account;

    -- Ensure the account gets permissions on tables created in the future
    ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT, INSERT, UPDATE ON TABLES TO service_account;
    ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT USAGE, SELECT, UPDATE ON SEQUENCES TO service_account;


END $$;