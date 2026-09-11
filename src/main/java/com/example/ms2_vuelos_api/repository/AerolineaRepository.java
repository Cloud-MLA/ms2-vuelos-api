package com.example.ms2_vuelos_api.repository;

import com.example.ms2_vuelos_api.model.Aerolinea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AerolineaRepository extends JpaRepository<Aerolinea, String> {
    // JpaRepository ya da gratis: save, findById, findAll, deleteById, etc.
    // El segundo parámetro (String) es el tipo de la PK (ruc es String)
}
