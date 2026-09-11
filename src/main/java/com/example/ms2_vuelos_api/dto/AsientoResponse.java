package com.example.ms2_vuelos_api.dto;

public class AsientoResponse {
    private Integer id;
    private String codigo;

    public AsientoResponse(Integer id, String codigo) {
        this.id = id;
        this.codigo = codigo;
    }

    public Integer getId() { return id; }
    public String getCodigo() { return codigo; }
}
