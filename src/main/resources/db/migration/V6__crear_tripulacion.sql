CREATE TABLE tripulacion (
                             empleado_id INT PRIMARY KEY,
                             num_licencia VARCHAR(20) NOT NULL UNIQUE,
                             CONSTRAINT fk_tripulacion_empleado
                                 FOREIGN KEY (empleado_id) REFERENCES empleado(id)
);
