package com.example.ms2_vuelos_api.repository;

import com.example.ms2_vuelos_api.model.Vuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VueloRepository extends JpaRepository<Vuelo, Integer>, JpaSpecificationExecutor<Vuelo> {

    @Query("SELECT COALESCE(MAX(v.id), 0) FROM Vuelo v")
    Integer obtenerMaxId();
}
