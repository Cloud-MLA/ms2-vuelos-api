package com.example.ms2_vuelos_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "aeronave")
public class Aeronave {
    @Id
    @Column(length = 10)
    private String placa;

    @Column(nullable = false, length = 50)
    private String modelo;

    @Column(nullable = false, length = 50)
    private String fabricante;

    @Column(nullable = false)
    private Integer capacidad;

    @Column(nullable = false, length = 1)
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
