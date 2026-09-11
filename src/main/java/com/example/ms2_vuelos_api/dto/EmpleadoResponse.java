package com.example.ms2_vuelos_api.dto;

import java.time.LocalDate;

public class EmpleadoResponse {
    private Integer id;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private String tipo; // "Tripulacion" o "OperativoTierra"

    public EmpleadoResponse(Integer id, String nombre, String apellido, LocalDate fechaNacimiento, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.tipo = tipo;
    }

    public Integer getId() { return id; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public String getTipo() { return tipo; }
}
