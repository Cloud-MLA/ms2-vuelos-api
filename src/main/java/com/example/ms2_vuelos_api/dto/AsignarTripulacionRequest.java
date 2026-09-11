package com.example.ms2_vuelos_api.dto;

import jakarta.validation.constraints.NotNull;

public class AsignarTripulacionRequest {

    @NotNull
    private Integer empleadoId;

    public Integer getEmpleadoId() { return empleadoId; }
    public void setEmpleadoId(Integer empleadoId) { this.empleadoId = empleadoId; }
}
