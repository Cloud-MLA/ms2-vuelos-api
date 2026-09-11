package com.example.ms2_vuelos_api.dto;

public class AerolineaResponse {

    private String ruc;
    private String nombre;
    private String alianza;

    public AerolineaResponse(String ruc, String nombre, String alianza) {
        this.ruc = ruc;
        this.nombre = nombre;
        this.alianza = alianza;
    }

    public String getRuc() { return ruc; }
    public String getNombre() { return nombre; }
    public String getAlianza() { return alianza; }
}
