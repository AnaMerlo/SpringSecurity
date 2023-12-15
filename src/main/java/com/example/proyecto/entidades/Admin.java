package com.example.proyecto.entidades;

import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("ADMIN")

public class Admin extends Usuario{
}
