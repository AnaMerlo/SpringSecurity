package com.example.proyecto.repositorios;


import com.example.proyecto.entidades.Periodista;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PeriodistaRepositorio extends JpaRepository<Periodista, String> {
    @Query("SELECT p FROM Periodista p WHERE p.email= :email")
    public Periodista buscarEmail(@Param("email") String email);
}
