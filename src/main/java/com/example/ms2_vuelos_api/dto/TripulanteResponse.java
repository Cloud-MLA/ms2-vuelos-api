package com.example.ms2_vuelos_api.dto;

public class TripulanteResponse {
    private Integer empleadoId;
    private String nombre;
    private String apellido;
    private String numLicencia;

    public TripulanteResponse(Integer empleadoId, String nombre, String apellido, String numLicencia) {
        this.empleadoId = empleadoId;
        this.nombre = nombre;
        this.apellido = apellido;
        this.numLicencia = numLicencia;
    }

    public Integer getEmpleadoId() { return empleadoId; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public String getNumLicencia() { return numLicencia; }
}
