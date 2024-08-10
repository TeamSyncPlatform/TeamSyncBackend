CREATE DATABASE team_sync_database;

\c team_sync_database;

CREATE USER team_sync_user WITH PASSWORD 't34m';

GRANT ALL PRIVILEGES ON DATABASE team_sync_database TO team_sync_user;

ALTER DATABASE team_sync_database OWNER TO team_sync_user;