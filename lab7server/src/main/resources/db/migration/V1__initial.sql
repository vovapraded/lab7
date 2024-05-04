CREATE TABLE coordinates (
    id BIGSERIAL PRIMARY KEY,
    x DOUBLE PRECISION NOT NULL,
    y BIGINT NOT NULL CONSTRAINT y_check CHECK (y > -618)
);

-- Создание типа venue_type
CREATE TYPE venue_type AS ENUM ('BAR', 'THEATRE', 'CINEMA', 'STADIUM');

-- Создание таблицы venue
CREATE TABLE venue (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(128) NOT NULL CHECK (LENGTH(name) > 0),
    capacity BIGINT  CONSTRAINT capacity_check CHECK (capacity > 0),
    venue_type venue_type 
);

-- Создание таблицы "user"
CREATE TABLE "user" (
    login VARCHAR(128) PRIMARY KEY NOT NULL CHECK (LENGTH(login) > 0),
    password NUMERIC(160) NOT NULL,
    salt VARCHAR(20) NOT NULL
);

-- Создание типа ticket_type
CREATE TYPE ticket_type AS ENUM ('VIP', 'USUAL', 'BUDGETARY');

-- Создание таблицы ticket с внешними ключами и каскадным удалением
CREATE TABLE ticket (
    id BIGINT PRIMARY KEY CONSTRAINT id_check CHECK (id > 0),
    name VARCHAR(128) NOT NULL CHECK (LENGTH(name) > 0),
    coordinates_id BIGINT NOT NULL UNIQUE,
    creation_date DATE NOT NULL,
    price BIGINT NOT NULL CONSTRAINT price_check CHECK (price > 0),
    discount BIGINT CONSTRAINT discount_check CHECK (discount > 0),
    refundable BOOL ,
    ticket_type ticket_type NOT NULL,
    venue_id BIGINT NOT NULL UNIQUE,
    created_by VARCHAR(128) NOT NULL,
    CONSTRAINT fk_coordinates_id FOREIGN KEY (coordinates_id) REFERENCES coordinates(id),
    CONSTRAINT fk_venue_id FOREIGN KEY (venue_id) REFERENCES venue(id) 
);

CREATE OR REPLACE FUNCTION delete_related_records()
RETURNS TRIGGER AS $$
BEGIN
    -- Удаление связанной записи из таблицы coordinates
    DELETE FROM s409397.coordinates WHERE id = OLD.coordinates_id;
    
    -- Удаление связанной записи из таблицы venue
    DELETE FROM s409397.venue WHERE id = OLD.venue_id;
    
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER delete_related_records_trigger
AFTER DELETE ON ticket
FOR EACH ROW
EXECUTE FUNCTION delete_related_records();