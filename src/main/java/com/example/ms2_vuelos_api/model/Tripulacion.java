package com.example.ms2_vuelos_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tripulacion")
public class Tripulacion {

    @Id
    @Column(name = "empleado_id")
    private Integer empleadoId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "empleado_id")
    private Empleado empleado;

    @Column(name = "num_licencia", nullable = false, length = 20, unique = true)
    private String numLicencia;

    public Integer getEmpleadoId() { return empleadoId; }
    public void setEmpleadoId(Integer empleadoId) { this.empleadoId = empleadoId; }
    public Empleado getEmpleado() { return empleado; }
    public void setEmpleado(Empleado empleado) { this.empleado = empleado; }
    public String getNumLicencia() { return numLicencia; }
    public void setNumLicencia(String numLicencia) { this.numLicencia = numLicencia; }
}
