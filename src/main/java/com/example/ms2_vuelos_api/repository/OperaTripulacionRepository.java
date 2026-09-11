package com.example.ms2_vuelos_api.repository;

import com.example.ms2_vuelos_api.model.OperaTripulacion;
import com.example.ms2_vuelos_api.model.OperaTripulacionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperaTripulacionRepository extends JpaRepository<OperaTripulacion, OperaTripulacionId> {
    List<OperaTripulacion> findByVuelo_Id(Integer vueloId);
    boolean existsByVuelo_IdAndTripulacion_EmpleadoId(Integer vueloId, Integer empleadoId);
}
