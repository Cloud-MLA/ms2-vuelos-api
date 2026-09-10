CREATE TABLE opera_tripulacion (
                                   vuelo_id INT NOT NULL,
                                   tripulacion_empleado_id INT NOT NULL,
                                   PRIMARY KEY (vuelo_id, tripulacion_empleado_id),
                                   CONSTRAINT fk_opera_vuelo
                                       FOREIGN KEY (vuelo_id) REFERENCES vuelo(id),
                                   CONSTRAINT fk_opera_tripulacion
                                       FOREIGN KEY (tripulacion_empleado_id) REFERENCES tripulacion(empleado_id)
);
