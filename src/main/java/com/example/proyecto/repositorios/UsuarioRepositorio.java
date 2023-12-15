/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.proyecto.repositorios;

import com.example.proyecto.entidades.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.proyecto.entidades.Usuario;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author aname
 */
@Repository
public interface UsuarioRepositorio extends JpaRepository<Usuario, String>{
    @Query("SELECT u FROM Usuario u WHERE u.email= :email")
    public Usuario buscarEmail(@Param("email") String email);
  //  @Modifying
  //  @Query("UPDATE Usuario u SET u.dtype = :dtype WHERE u.id = :id")
 //   void actualizarDtypeUsuario(@Param("id") String id, @Param("dtype") String dtype);




}
