package com.example.ms2_vuelos_api.dto;

import java.time.OffsetDateTime;

public class VueloResponse {
    private Integer id;
    private String numero;
    private String origen;
    private String destino;
    private OffsetDateTime horaProgramada;
    private OffsetDateTime horaReal;
    private String estado;
    private String tipo;
    private String aerolineaRuc;
    private String aeronavePlaca;

    public VueloResponse(Integer id, String numero, String origen, String destino,
                         OffsetDateTime horaProgramada, OffsetDateTime horaReal,
                         String estado, String tipo, String aerolineaRuc, String aeronavePlaca) {
        this.id = id;
        this.numero = numero;
        this.origen = origen;
        this.destino = destino;
        this.horaProgramada = horaProgramada;
        this.horaReal = horaReal;
        this.estado = estado;
        this.tipo = tipo;
        this.aerolineaRuc = aerolineaRuc;
        this.aeronavePlaca = aeronavePlaca;
    }

    public Integer getId() { return id; }
    public String getNumero() { return numero; }
    public String getOrigen() { return origen; }
    public String getDestino() { return destino; }
    public OffsetDateTime getHoraProgramada() { return horaProgramada; }
    public OffsetDateTime getHoraReal() { return horaReal; }
    public String getEstado() { return estado; }
    public String getTipo() { return tipo; }
    public String getAerolineaRuc() { return aerolineaRuc; }
    public String getAeronavePlaca() { return aeronavePlaca; }
}
