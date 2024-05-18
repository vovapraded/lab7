ALTER TABLE s409397.ticket
    ALTER COLUMN creation_date TYPE TIMESTAMP USING (creation_date)::timestamp;
UPDATE flyway_schema_history
SET installed_on = CURRENT_TIMESTAMP
WHERE installed_on IS NULL;