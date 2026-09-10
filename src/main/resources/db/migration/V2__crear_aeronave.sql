CREATE TABLE aeronave (
                          placa VARCHAR(10) PRIMARY KEY,
                          modelo VARCHAR(50) NOT NULL,
                          fabricante VARCHAR(50) NOT NULL,
                          capacidad INT NOT NULL CHECK (capacidad > 0),
                          clase VARCHAR(1) NOT NULL
                              CHECK (clase IN ('A', 'B', 'C', 'D', 'E', 'F'))
);
