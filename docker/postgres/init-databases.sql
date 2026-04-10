-- Create multiple databases for EcoSync components
CREATE DATABASE ecosync_zone_db;
CREATE DATABASE ecosync_crop_db;

-- Grant permissions if necessary (Postgres container usually handles this for the main user)
-- Note: 'postgres' is the default user, but these will be created on startup
