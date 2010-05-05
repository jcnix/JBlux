CREATE TABLE maps (
    id serial UNIQUE,
    name smallint,
    left_of smallint,
    right_of smallint,
    above smallint,
    below smallint
);
