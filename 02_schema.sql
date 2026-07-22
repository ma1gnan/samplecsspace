-- =====================================================================
-- Real-Estate Office Database — Schema (PostgreSQL)
-- =====================================================================
-- Run as: psql -d realestate -f 02_schema.sql
-- =====================================================================

DROP TABLE IF EXISTS sale CASCADE;
DROP TABLE IF EXISTS property_owner CASCADE;
DROP TABLE IF EXISTS property CASCADE;
DROP TABLE IF EXISTS school_district CASCADE;
DROP TABLE IF EXISTS seller CASCADE;
DROP TABLE IF EXISTS buyer CASCADE;
DROP TABLE IF EXISTS agent CASCADE;

-- ---------------------------------------------------------------------
-- AGENT
-- ---------------------------------------------------------------------
CREATE TABLE agent (
    agent_id        SERIAL PRIMARY KEY,
    first_name      VARCHAR(50)  NOT NULL,
    last_name       VARCHAR(50)  NOT NULL,
    phone           VARCHAR(20)  NOT NULL,
    email           VARCHAR(100) UNIQUE,
    license_number  VARCHAR(20)  NOT NULL UNIQUE,
    hire_date       DATE         NOT NULL
);

-- ---------------------------------------------------------------------
-- BUYER
-- ---------------------------------------------------------------------
CREATE TABLE buyer (
    buyer_id        SERIAL PRIMARY KEY,
    first_name      VARCHAR(50)  NOT NULL,
    last_name       VARCHAR(50)  NOT NULL,
    phone           VARCHAR(20),
    email           VARCHAR(100),
    mailing_address VARCHAR(150)
);

-- ---------------------------------------------------------------------
-- SELLER
-- ---------------------------------------------------------------------
CREATE TABLE seller (
    seller_id       SERIAL PRIMARY KEY,
    first_name      VARCHAR(50)  NOT NULL,
    last_name       VARCHAR(50)  NOT NULL,
    phone           VARCHAR(20),
    email           VARCHAR(100),
    mailing_address VARCHAR(150)
);

-- ---------------------------------------------------------------------
-- SCHOOL DISTRICT (lookup)
-- ---------------------------------------------------------------------
CREATE TABLE school_district (
    district_id     SERIAL PRIMARY KEY,
    district_name   VARCHAR(100) NOT NULL UNIQUE
);

-- ---------------------------------------------------------------------
-- PROPERTY
-- ---------------------------------------------------------------------
CREATE TABLE property (
    property_id      SERIAL PRIMARY KEY,
    street_address    VARCHAR(100) NOT NULL,
    city              VARCHAR(50)  NOT NULL,
    state             CHAR(2)      NOT NULL DEFAULT 'PA',
    zip_code          VARCHAR(10)  NOT NULL,
    district_id       INTEGER      NOT NULL REFERENCES school_district(district_id),
    bedrooms          SMALLINT     NOT NULL CHECK (bedrooms >= 0),
    bathrooms         NUMERIC(3,1) NOT NULL CHECK (bathrooms >= 0),
    square_feet       INTEGER      CHECK (square_feet > 0),
    year_built        SMALLINT,
    has_pool          BOOLEAN      NOT NULL DEFAULT FALSE,
    lot_size_acres    NUMERIC(5,2),
    list_price        NUMERIC(12,2) NOT NULL CHECK (list_price > 0),
    listing_date      DATE         NOT NULL,
    status            VARCHAR(10)  NOT NULL DEFAULT 'FOR SALE'
                          CHECK (status IN ('FOR SALE','PENDING','SOLD','WITHDRAWN')),
    description       TEXT,
    photo             OID,                       -- PostgreSQL large object (BLOB); NULL for most rows
    listing_agent_id  INTEGER      NOT NULL REFERENCES agent(agent_id)
);

CREATE INDEX idx_property_city         ON property(city);
CREATE INDEX idx_property_district     ON property(district_id);
CREATE INDEX idx_property_price        ON property(list_price);
CREATE INDEX idx_property_status       ON property(status);
CREATE INDEX idx_property_listing_agt  ON property(listing_agent_id);

-- ---------------------------------------------------------------------
-- PROPERTY_OWNER (bridge: many sellers can jointly own a property)
-- ---------------------------------------------------------------------
CREATE TABLE property_owner (
    property_id  INTEGER NOT NULL REFERENCES property(property_id) ON DELETE CASCADE,
    seller_id    INTEGER NOT NULL REFERENCES seller(seller_id),
    PRIMARY KEY (property_id, seller_id)
);

-- ---------------------------------------------------------------------
-- SALE
-- ---------------------------------------------------------------------
CREATE TABLE sale (
    sale_id           SERIAL PRIMARY KEY,
    property_id       INTEGER NOT NULL UNIQUE REFERENCES property(property_id),
    buyer_id          INTEGER NOT NULL REFERENCES buyer(buyer_id),
    selling_agent_id  INTEGER NOT NULL REFERENCES agent(agent_id),
    buyer_agent_id    INTEGER REFERENCES agent(agent_id),   -- nullable: buyer may be unrepresented
    sale_price        NUMERIC(12,2) NOT NULL CHECK (sale_price > 0),
    sale_date         DATE NOT NULL
);

CREATE INDEX idx_sale_date          ON sale(sale_date);
CREATE INDEX idx_sale_selling_agt   ON sale(selling_agent_id);
CREATE INDEX idx_sale_buyer_agt     ON sale(buyer_agent_id);
CREATE INDEX idx_sale_buyer         ON sale(buyer_id);

-- ---------------------------------------------------------------------
-- BUSINESS RULE TRIGGERS
--   1) A property can only be sold while it is FOR SALE (or PENDING).
--   2) A sale date can't precede the property's listing date.
--   3) Recording a sale automatically flips the property's status to SOLD.
-- ---------------------------------------------------------------------
CREATE OR REPLACE FUNCTION fn_record_sale() RETURNS TRIGGER AS $$
DECLARE
    v_status  VARCHAR(10);
    v_listed  DATE;
BEGIN
    SELECT status, listing_date INTO v_status, v_listed
    FROM property WHERE property_id = NEW.property_id;

    IF v_status NOT IN ('FOR SALE','PENDING') THEN
        RAISE EXCEPTION 'Property % is not available for sale (status = %)',
            NEW.property_id, v_status;
    END IF;

    IF NEW.sale_date < v_listed THEN
        RAISE EXCEPTION 'Sale date (%) cannot precede listing date (%)',
            NEW.sale_date, v_listed;
    END IF;

    UPDATE property SET status = 'SOLD' WHERE property_id = NEW.property_id;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_record_sale
    BEFORE INSERT ON sale
    FOR EACH ROW EXECUTE FUNCTION fn_record_sale();
