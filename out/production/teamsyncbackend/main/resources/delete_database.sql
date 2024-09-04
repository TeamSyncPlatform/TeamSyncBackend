SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'team_sync_database' AND pid <> pg_backend_pid();

DROP DATABASE IF EXISTS team_sync_database;



SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.usename = 'team_sync_user' AND pid <> pg_backend_pid();

DROP USER IF EXISTS team_sync_user;