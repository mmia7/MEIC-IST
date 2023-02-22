DROP DATABASE IF EXISTS airports_dw;

CREATE DATABASE airports_dw;

USE airports_dw;

CREATE TABLE dim_airport_origin (
    airport_id INT,
    city varchar(50),
    country varchar(50),
    airport_name VARCHAR(255),
    PRIMARY KEY (airport_id)
);

CREATE TABLE dim_airport_destination (
    airport_id INT,
    city varchar(50),
    country varchar(50),
    airport_name VARCHAR(255),
    PRIMARY KEY (airport_id)
);

CREATE TABLE dim_departure_date (
    departure_id DATETIME,
    year_id INT,
    month_id INT,
    month_name VARCHAR(255),
    departure_day INT,
    PRIMARY KEY (departure_id)
);

CREATE TABLE dim_arrival_date (
    arrival_id DATETIME,
    year_id INT,
    month_id INT,
    month_name VARCHAR(255),
    arrival_day INT,
    PRIMARY KEY (arrival_id)
);

CREATE TABLE dim_airline (
    airline_id INT,
    airline_name VARCHAR(255),
    PRIMARY KEY (airline_id)

);
CREATE TABLE dim_airplane (
    airplane_id INT,
    type_id INT,
    PRIMARY KEY (airplane_id)
);

CREATE TABLE fact_flight (
    flight_id INT,
    from_airport INT,
    to_airport INT,
    departure_id DATETIME,
    arrival_id DATETIME,
    airline_id INT,
    airplane_id INT,
    total_passengers INT,
    revenue DOUBLE,
    PRIMARY KEY (flight_id),
    FOREIGN KEY (from_airport) REFERENCES dim_airport_origin (airport_id),
    FOREIGN KEY (to_airport) REFERENCES dim_airport_destination (airport_id),
    FOREIGN KEY (departure_id) REFERENCES dim_departure_date (departure_id),
    FOREIGN KEY (arrival_id) REFERENCES dim_arrival_date (arrival_id),
    FOREIGN KEY (airline_id) REFERENCES dim_airline (airline_id),
    FOREIGN KEY (airplane_id) REFERENCES dim_airplane (airplane_id)
);
