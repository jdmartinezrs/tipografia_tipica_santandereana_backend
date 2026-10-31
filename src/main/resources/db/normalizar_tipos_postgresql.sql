-- Script independiente: ejecutar una vez sobre la base creada con el esquema original.
-- Conserva los valores y ajusta únicamente los tres tipos acordados.
ALTER TABLE descarga
    ALTER COLUMN ip TYPE VARCHAR(45)
    USING ip::text;

ALTER TABLE compra
    ALTER COLUMN moneda TYPE VARCHAR(3)
    USING rtrim(moneda)::varchar(3);

ALTER TABLE font_file
    ALTER COLUMN checksum_sha256 TYPE VARCHAR(64)
    USING rtrim(checksum_sha256)::varchar(64);
