package com.example.ms2_vuelos_api.dto;

import jakarta.validation.constraints.*;

public class AeronaveRequest {

    @NotBlank
    private String placa;

    @NotBlank @Size(max = 50)
    private String modelo;

    @NotBlank @Size(max = 50)
    private String fabricante;

    @NotNull @Positive
    private Integer capacidad;

    @NotBlank
    @Pattern(regexp = "A|B|C|D|E|F", message = "Clase debe ser A, B, C, D, E o F")
    private String clase;

    public String getPlaca() { return placa; }
    public void setPlaca(String placa) { this.placa = placa; }
    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }
    public String getFabricante() { return fabricante; }
    public void setFabricante(String fabricante) { this.fabricante = fabricante; }
    public Integer getCapacidad() { return capacidad; }
    public void setCapacidad(Integer capacidad) { this.capacidad = capacidad; }
    public String getClase() { return clase; }
    public void setClase(String clase) { this.clase = clase; }
}
