package com.example.ms2_vuelos_api.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "aerolinea")
public class Aerolinea {
    @Id
    @Column(length = 11)
    private String ruc;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 20)
    private String alianza;

    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getAlianza() { return alianza; }
    public void setAlianza(String alianza) { this.alianza = alianza; }
}
