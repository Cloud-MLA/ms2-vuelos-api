package com.example.ms2_vuelos_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class AerolineaRequest {

    @NotBlank
    @Pattern(regexp = "\\d{11}", message = "El RUC debe tener 11 dígitos")
    private String ruc;

    @NotBlank
    @Size(max = 100)
    private String nombre;

    @NotBlank
    @Pattern(regexp = "Star Alliance|SkyTeam|Oneworld", message = "Alianza inválida")
    private String alianza;

    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getAlianza() { return alianza; }
    public void setAlianza(String alianza) { this.alianza = alianza; }
}
