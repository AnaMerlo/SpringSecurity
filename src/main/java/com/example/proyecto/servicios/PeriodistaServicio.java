package com.example.proyecto.servicios;

import com.example.proyecto.entidades.Noticia;
import com.example.proyecto.entidades.Periodista;

import com.example.proyecto.repositorios.NoticiaRepositorio;
import com.example.proyecto.repositorios.PeriodistaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PeriodistaServicio {
    @Autowired
    private PeriodistaRepositorio periodistaRepositorio;
    @Autowired
    private NoticiaRepositorio noticiaRepositorio;


   /*public Periodista getOne(String idPeriodista){
        return periodistaRepositorio.getOne(idPeriodista);

    }*/
    @Transactional
    public List<Noticia> misNoticia(String id){
        List<Noticia> misNoticias = new ArrayList<>();
        // Obtener el periodista por su ID
        Optional<Periodista> respuesta = periodistaRepositorio.findById(id);

        if (respuesta.isPresent()) {
            // Si el periodista existe, obtener sus noticias
            Periodista periodista = respuesta.get();
            misNoticias = noticiaRepositorio.findByPeriodista(periodista);
        }
        return misNoticias;

    }

    @Transactional
    public Integer sueldoPeriodista(String id){
        Optional<Periodista> respuesta = periodistaRepositorio.findById(id);
        if(respuesta.isPresent()){
            Periodista periodista = respuesta.get();
            return periodista.getSueldoMensual();
        }else{
            return null;
        }

    }
}
