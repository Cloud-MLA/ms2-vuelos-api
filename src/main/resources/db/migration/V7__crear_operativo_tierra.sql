CREATE TABLE operativo_tierra (
                                  empleado_id INT PRIMARY KEY,
                                  area_operativa VARCHAR(20) NOT NULL
                                      CHECK (area_operativa IN ('Rampa', 'Equipajes', 'Seguridad', 'Mantenimiento', 'Plataforma')),
                                  CONSTRAINT fk_operativo_empleado
                                      FOREIGN KEY (empleado_id) REFERENCES empleado(id)
);
