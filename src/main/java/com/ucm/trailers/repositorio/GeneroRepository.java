package com.ucm.trailers.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ucm.trailers.modelo.Genero;

@Repository
public interface GeneroRepository extends JpaRepository<Genero, Integer>{

}
