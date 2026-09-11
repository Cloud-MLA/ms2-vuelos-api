package com.example.ms2_vuelos_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "opera_tripulacion")
@IdClass(OperaTripulacionId.class)
public class OperaTripulacion {

    @Id
    @ManyToOne
    @JoinColumn(name = "vuelo_id", nullable = false)
    private Vuelo vuelo;

    @Id
    @ManyToOne
    @JoinColumn(name = "tripulacion_empleado_id", nullable = false)
    private Tripulacion tripulacion;

    public Vuelo getVuelo() { return vuelo; }
    public void setVuelo(Vuelo vuelo) { this.vuelo = vuelo; }
    public Tripulacion getTripulacion() { return tripulacion; }
    public void setTripulacion(Tripulacion tripulacion) { this.tripulacion = tripulacion; }
}
