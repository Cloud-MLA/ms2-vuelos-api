package com.example.ms2_vuelos_api.dto;

public class VueloExistsResponse {

    private boolean exists;
    private String estado;

    public VueloExistsResponse(boolean exists, String estado) {
        this.exists = exists;
        this.estado = estado;
    }

    public boolean isExists() { return exists; }
    public String getEstado() { return estado; }
}
