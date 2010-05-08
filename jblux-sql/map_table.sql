CREATE TABLE maps (
    id serial UNIQUE,
    name smallint,
    map_left smallint,
    map_right smallint,
    map_above smallint,
    map_below smallint
);
