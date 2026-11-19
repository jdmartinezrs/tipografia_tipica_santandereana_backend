-- Ejecutar manualmente una vez sobre tipografia_tipica_santander.
-- Índices únicos insensibles a mayúsculas (ya incluidos en el esquema base; IF NOT EXISTS los hace seguros).
CREATE UNIQUE INDEX IF NOT EXISTS ux_usuario_email ON usuario (lower(email));
CREATE UNIQUE INDEX IF NOT EXISTS ux_usuario_username ON usuario (lower(username));

CREATE TABLE IF NOT EXISTS refresh_token (
    refresh_token_id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES usuario(usuario_id) ON DELETE CASCADE,
    token_hash VARCHAR(64) NOT NULL UNIQUE,
    fecha_expiracion TIMESTAMPTZ NOT NULL,
    revocado BOOLEAN NOT NULL DEFAULT FALSE,
    fecha_creacion TIMESTAMPTZ NOT NULL DEFAULT now()
);
CREATE INDEX IF NOT EXISTS ix_refresh_token_usuario ON refresh_token(usuario_id);
CREATE INDEX IF NOT EXISTS ix_refresh_token_expiracion ON refresh_token(fecha_expiracion);
