/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.proyecto.entidades;

import java.util.Date;
import javax.persistence.*;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;



/**
 *
 * @author aname
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Noticia {
    
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String titulo;
    @Column(length = 5000)
    private String cuerpo;
    
    @Temporal (TemporalType.DATE)
    private Date fecha;
    @ManyToOne
    @JoinColumn(name = "periodista_id")
    private Periodista periodista;
    @ManyToOne
    @JoinColumn(name = "administrador_id")
    private Admin admin;
    
    
}
