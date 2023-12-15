/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.proyecto.entidades;

import com.example.proyecto.enumeraciones.Rol;
import java.util.Date;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author aname
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("USER")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Usuario {
    @Id
    @GeneratedValue(generator = "uuid")
    private String id;
    private String nombre;
    private String email;
    
    private String password;

    @Temporal (TemporalType.DATE)
    private Date fechaAlta;
    @OneToOne
    private Imagen imagen;
    @Enumerated(EnumType.STRING)
    private Rol rol;
    private Boolean activo;
    
}
