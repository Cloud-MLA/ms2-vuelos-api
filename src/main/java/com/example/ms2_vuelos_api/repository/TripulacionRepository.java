package com.example.ms2_vuelos_api.repository;

import com.example.ms2_vuelos_api.model.Tripulacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TripulacionRepository extends JpaRepository<Tripulacion, Integer> {
}
