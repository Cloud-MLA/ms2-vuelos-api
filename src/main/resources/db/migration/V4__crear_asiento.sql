CREATE TABLE asiento (
                         id INT PRIMARY KEY,
                         aeronave_placa VARCHAR(10) NOT NULL,
                         codigo VARCHAR(5) NOT NULL,
                         CONSTRAINT fk_asiento_aeronave
                             FOREIGN KEY (aeronave_placa) REFERENCES aeronave(placa),
                         CONSTRAINT uq_asiento_aeronave_codigo
                             UNIQUE (aeronave_placa, codigo)
);
