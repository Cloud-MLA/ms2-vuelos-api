package com.example.ms2_vuelos_api.repository;

import com.example.ms2_vuelos_api.model.Asiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsientoRepository extends JpaRepository<Asiento, Integer> {
    List<Asiento> findByAeronave_Placa(String placa);
}
