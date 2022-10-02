CREATE TABLE cars
(
    id    SERIAL PRIMARY KEY,
    brand TEXT NOT NULL,
    model TEXT,
    price NUMERIC(10, 2)
);

CREATE TABLE people
(
    id   SERIAL PRIMARY KEY,
    name_people TEXT NOT NULL,
    age  INTEGER CHECK ( age > 0 ),
    drivers_license BOOLEAN,
    car_id INTEGER REFERENCES cars(id)
);