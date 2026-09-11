package com.example.ms2_vuelos_api.model;

import jakarta.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Table(name = "vuelo")
public class Vuelo {
    @Id
    private Integer id;

    @Column(nullable = false, length = 10)
    private String numero;

    @Column(nullable = false, length = 50)
    private String origen;

    @Column(nullable = false, length = 50)
    private String destino;

    @Column(name = "hora_programada", nullable = false)
    private OffsetDateTime horaProgramada;

    @Column(name = "hora_real")
    private OffsetDateTime horaReal;

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(nullable = false, length = 20)
    private String tipo;

    @ManyToOne
    @JoinColumn(name = "aerolinea_ruc", nullable = false)
    private Aerolinea aerolinea;

    @ManyToOne
    @JoinColumn(name = "aeronave_placa", nullable = false)
    private Aeronave aeronave;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }
    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }
    public OffsetDateTime getHoraProgramada() { return horaProgramada; }
    public void setHoraProgramada(OffsetDateTime horaProgramada) { this.horaProgramada = horaProgramada; }
    public OffsetDateTime getHoraReal() { return horaReal; }
    public void setHoraReal(OffsetDateTime horaReal) { this.horaReal = horaReal; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Aerolinea getAerolinea() { return aerolinea; }
    public void setAerolinea(Aerolinea aerolinea) { this.aerolinea = aerolinea; }
    public Aeronave getAeronave() { return aeronave; }
    public void setAeronave(Aeronave aeronave) { this.aeronave = aeronave; }
}
