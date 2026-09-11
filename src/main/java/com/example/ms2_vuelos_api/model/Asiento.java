package com.example.ms2_vuelos_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "asiento")
public class Asiento {
    @Id
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "aeronave_placa", nullable = false)
    private Aeronave aeronave;

    @Column(nullable = false, length = 5)
    private String codigo;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Aeronave getAeronave() { return aeronave; }
    public void setAeronave(Aeronave aeronave) { this.aeronave = aeronave; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
}
