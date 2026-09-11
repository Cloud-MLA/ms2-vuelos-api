package com.example.ms2_vuelos_api.model;

import java.io.Serializable;
import java.util.Objects;

public class OperaTripulacionId implements Serializable {

    private Integer vuelo;
    private Integer tripulacion;

    public OperaTripulacionId() {}

    public OperaTripulacionId(Integer vuelo, Integer tripulacion) {
        this.vuelo = vuelo;
        this.tripulacion = tripulacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OperaTripulacionId)) return false;
        OperaTripulacionId that = (OperaTripulacionId) o;
        return Objects.equals(vuelo, that.vuelo) && Objects.equals(tripulacion, that.tripulacion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vuelo, tripulacion);
    }
}
