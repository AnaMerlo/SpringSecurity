package com.example.proyecto.entidades;

import lombok.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import java.util.List;
import javax.persistence.DiscriminatorValue;

@Entity
@DiscriminatorValue("PERIODISTA")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Periodista extends Usuario{
    @OneToMany(mappedBy = "periodista", cascade = CascadeType.ALL)
    private List<Noticia> misNoticias;
    private Integer sueldoMensual;
}
