package com.example.ms2_vuelos_api.model;

import jakarta.persistence.*;

@Entity
@Table(name = "operativo_tierra")
public class OperativoTierra {

    @Id
    @Column(name = "empleado_id")
    private Integer empleadoId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "empleado_id")
    private Empleado empleado;

    @Column(name = "area_operativa", nullable = false, length = 20)
    private String areaOperativa;

    public Integer getEmpleadoId() { return empleadoId; }
    public void setEmpleadoId(Integer empleadoId) { this.empleadoId = empleadoId; }
    public Empleado getEmpleado() { return empleado; }
    public void setEmpleado(Empleado empleado) { this.empleado = empleado; }
    public String getAreaOperativa() { return areaOperativa; }
    public void setAreaOperativa(String areaOperativa) { this.areaOperativa = areaOperativa; }
}
