package com.example.ms2_vuelos_api.dto;

import jakarta.validation.constraints.*;
import java.time.OffsetDateTime;

public class VueloRequest {

    @NotBlank
    private String numero;

    @NotBlank @Size(max = 50)
    private String origen;

    @NotBlank @Size(max = 50)
    private String destino;

    @NotNull
    private OffsetDateTime horaProgramada;

    @NotBlank
    @Pattern(regexp = "Nacional|Internacional")
    private String tipo;

    @NotBlank
    private String aerolineaRuc;

    @NotBlank
    private String aeronavePlaca;

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    public String getOrigen() { return origen; }
    public void setOrigen(String origen) { this.origen = origen; }
    public String getDestino() { return destino; }
    public void setDestino(String destino) { this.destino = destino; }
    public OffsetDateTime getHoraProgramada() { return horaProgramada; }
    public void setHoraProgramada(OffsetDateTime horaProgramada) { this.horaProgramada = horaProgramada; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getAerolineaRuc() { return aerolineaRuc; }
    public void setAerolineaRuc(String aerolineaRuc) { this.aerolineaRuc = aerolineaRuc; }
    public String getAeronavePlaca() { return aeronavePlaca; }
    public void setAeronavePlaca(String aeronavePlaca) { this.aeronavePlaca = aeronavePlaca; }
}
