USE AVaaS;
-- Create tables

-- Table: user
CREATE TABLE user (
  id BIGINT PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

-- Table: employee
CREATE TABLE employee (
  id BIGINT PRIMARY KEY,
  user_id BIGINT,
  FOREIGN KEY (user_id) REFERENCES user(id)
);

-- Table: apilotDeveloper
CREATE TABLE apilotDeveloper (
  id BIGINT PRIMARY KEY
);

-- Table: APilot
CREATE TABLE APilot (
  id BIGINT PRIMARY KEY,
  developer_id BIGINT,
  version INTEGER,
  FOREIGN KEY (developer_id) REFERENCES apilotDeveloper(id)
);

-- Table carManufacturer
CREATE TABLE carManufacturer (
    brand VARCHAR(255) NOT NULL PRIMARY KEY
);

-- Table Provider
CREATE TABLE provider (
  id VARCHAR(255) NOT NULL PRIMARY KEY
);

-- Table Service
CREATE TABLE service (
  id BIGINT PRIMARY KEY,
  provider_id VARCHAR(255),
  name VARCHAR(255) NOT NULL,
  FOREIGN KEY (provider_id) REFERENCES provider(id)
);

-- Table AV
CREATE TABLE AV (
  id BIGINT PRIMARY KEY,
  brand VARCHAR(255) NOT NULL,
  user_id BIGINT,
  service_id BIGINT,
  price INT,
  FOREIGN KEY (brand) REFERENCES carManufacturer(brand),
  FOREIGN KEY (user_id) REFERENCES user(id),
  FOREIGN KEY (service_id) REFERENCES service(id)
);

-- Table Transactions
CREATE TABLE transactions (
  id BIGINT PRIMARY KEY,
  user_id BIGINT,
  av_id BIGINT,
  apilot_id BIGINT,
  description VARCHAR(255) NOT NULL,
  FOREIGN KEY (user_id) REFERENCES user(id),
  FOREIGN KEY (av_id) REFERENCES AV(id),
  FOREIGN KEY (apilot_id) REFERENCES APilot(id)
);
-- Populate tables

-- Insert data into user table
INSERT INTO user (id, name) VALUES (1, 'John'), (2, 'Jane');

-- Insert data into employee table
INSERT INTO employee (id, user_id) VALUES (1, 1), (2, 2);

-- Insert data into apilotDeveloper table
INSERT INTO apilotDeveloper (id) VALUES (1), (2);

-- Insert data into APilot table
INSERT INTO APilot (id, developer_id, version) VALUES (1, 1, 1), (2, 2, 2);

-- Insert data into carManufacturer table
INSERT INTO carManufacturer (brand) VALUES ('Nissan'), ('Rover');

-- Insert data into provider table
INSERT INTO provider (id) VALUES ('P1'),('P2');

-- Insert data into service table
INSERT INTO service (id, provider_id, name) VALUES (1, 'P1', 'Service 1'), (2, 'P2', 'Service 2');

-- Insert data into AV table
INSERT INTO AV (id, brand, user_id, service_id, price) VALUES (1, 'Nissan', 1, 1, 100), (2, 'Rover', 2, 2, 200);

-- Insert data into transactions table
INSERT INTO transactions (id, user_id, av_id, apilot_id, description) VALUES (1, 1, 1, 1, 'buy'), (2, 2, 2, 1, 'select');
