package com.example.ms2_vuelos_api.dto;

public class AeronaveResponse {
    private String placa;
    private String modelo;
    private String fabricante;
    private Integer capacidad;
    private String clase;

    public AeronaveResponse(String placa, String modelo, String fabricante, Integer capacidad, String clase) {
        this.placa = placa;
        this.modelo = modelo;
        this.fabricante = fabricante;
        this.capacidad = capacidad;
        this.clase = clase;
    }

    public String getPlaca() { return placa; }
    public String getModelo() { return modelo; }
    public String getFabricante() { return fabricante; }
    public Integer getCapacidad() { return capacidad; }
    public String getClase() { return clase; }
}
