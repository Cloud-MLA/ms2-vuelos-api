package com.example.ms2_vuelos_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CambioEstadoRequest {

    @NotBlank
    @Pattern(regexp = "Programado|Embarcando|Despegado|Aterrizado|Retrasado|Cancelado")
    private String estado;

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
