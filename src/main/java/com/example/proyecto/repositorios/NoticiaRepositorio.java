/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.proyecto.repositorios;

import com.example.proyecto.entidades.Noticia;
import com.example.proyecto.entidades.Periodista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *
 * @author aname
 */
@Repository
public interface NoticiaRepositorio extends JpaRepository<Noticia, String> {
    
    @Query("SELECT n FROM Noticia n WHERE n.titulo = :titulo")
    public Noticia buscarPorTitulo(@Param("titulo") String titulo);
    List<Noticia> findByPeriodista(Periodista periodista);
    
}
