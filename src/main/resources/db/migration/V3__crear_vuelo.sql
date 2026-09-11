CREATE TABLE vuelo (
                       id INT PRIMARY KEY,
                       numero VARCHAR(10) NOT NULL,
                       origen VARCHAR(50) NOT NULL,
                       destino VARCHAR(50) NOT NULL,
                       hora_programada TIMESTAMPTZ NOT NULL,
                       hora_real TIMESTAMPTZ,
                       estado VARCHAR(20) NOT NULL DEFAULT 'Programado'
                           CHECK (estado IN ('Programado', 'Embarcando', 'Despegado', 'Aterrizado', 'Retrasado', 'Cancelado')),
                       tipo VARCHAR(20) NOT NULL
                           CHECK (tipo IN ('Nacional', 'Internacional')),
                       aerolinea_ruc VARCHAR(11) NOT NULL,
                       aeronave_placa VARCHAR(10) NOT NULL,
                       CONSTRAINT fk_vuelo_aerolinea
                           FOREIGN KEY (aerolinea_ruc) REFERENCES aerolinea(ruc),
                       CONSTRAINT fk_vuelo_aeronave
                           FOREIGN KEY (aeronave_placa) REFERENCES aeronave(placa)
);
