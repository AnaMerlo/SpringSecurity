package com.example.proyecto.servicios;

import com.example.proyecto.entidades.Imagen;
import com.example.proyecto.excepciones.MiException;
import com.example.proyecto.repositorios.ImagenRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class ImagenServicio {
    @Autowired
    private ImagenRepositorio imagenRepositorio;
    public Imagen guardar(MultipartFile archivo) throws MiException {
        if(archivo != null){
            try {
                Imagen imagen = new Imagen();
                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());

                return imagenRepositorio.save(imagen);
            }catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }
        return null;
    }
    public Imagen actualizar(MultipartFile archivo, String idImagen ) throws MiException{
        if(archivo != null){
            try {
                Imagen imagen = new Imagen();
                if(idImagen != null){
                    Optional<Imagen> respuesta = imagenRepositorio.findById(idImagen);
                    if(respuesta.isPresent()){
                        imagen = respuesta.get();

                    }
                }

                imagen.setMime(archivo.getContentType());
                imagen.setNombre(archivo.getName());
                imagen.setContenido(archivo.getBytes());
                return imagenRepositorio.save(imagen);
            }catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }
        return null;
    }


}
