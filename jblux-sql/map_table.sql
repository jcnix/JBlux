CREATE TABLE maps (
    id serial UNIQUE,
    name text, 
    map_left smallint,
    map_right smallint,
    map_above smallint,
    map_below smallint
);
