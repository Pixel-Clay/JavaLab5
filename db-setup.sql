CREATE TABLE users (
    id SERIAL,
    login TEXT NOT NULL,
    pass_hash TEXT NOT NULL
);

CREATE TABLE vehicles (
    id integer not null unique,
    name text not null,
    x double not null,
    y double not null,
    creation_date timestamp with time zone,
    engine_power numeric not null,
    distance_travelled double precision not null,
    type text,
    fuel_type text not null,
    user_id integer not null
);

CREATE SEQUENCE veh_id
    START 1
    INCREMENT 1;