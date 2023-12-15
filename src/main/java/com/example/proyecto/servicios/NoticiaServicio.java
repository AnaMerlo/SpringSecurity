/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.proyecto.servicios;

import com.example.proyecto.entidades.Admin;
import com.example.proyecto.entidades.Noticia;
import com.example.proyecto.entidades.Periodista;
import com.example.proyecto.entidades.Usuario;
import com.example.proyecto.enumeraciones.Rol;
import com.example.proyecto.excepciones.MiException;
import com.example.proyecto.repositorios.NoticiaRepositorio;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.proyecto.repositorios.PeriodistaRepositorio;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 *
 * @author aname
 */
@Service
public class NoticiaServicio {
    @Autowired
    private NoticiaRepositorio noticiaRepositorio;
    @Autowired
    private PeriodistaRepositorio periodistaRepositorio;
    @Autowired
    private UsuarioServicio usuarioServicio;

    @PreAuthorize("hasAnyRole('ADMIN', 'PERIODISTA')")
   @Transactional
    public void crearNoticia(String id, String titulo, String cuerpo) throws MiException{

        validar(titulo, cuerpo);


        /*ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");


        Periodista periodista = (Periodista) usuario;
        Admin admin = (Admin) usuario;*/
        String nombreUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioServicio.obtenerUsuarioPorEmail(nombreUsuario);

        Noticia noticia = new Noticia();
        
        noticia.setId(id);
        noticia.setTitulo(titulo);
        noticia.setCuerpo(cuerpo);
        
        noticia.setFecha(new Date());
        if(usuario.getRol() == Rol.PERIODISTA){
            noticia.setPeriodista((Periodista) usuario);
        }else if(usuario.getRol() == Rol.ADMIN){
            noticia.setAdmin((Admin) usuario);
        }

        noticiaRepositorio.save(noticia);
    }
    @Transactional
    public List<Noticia> listarNoticias(){
        
        List<Noticia> noticias = new ArrayList();
        
        noticias = noticiaRepositorio.findAll().stream().sorted(Comparator.comparing(Noticia::getFecha).reversed()).collect(Collectors.toList());
        
        return noticias;
    }
    @Transactional
    public void modificarNoticia(String id, String titulo, String cuerpo)throws MiException{
        
        validar(titulo, cuerpo);
        String nombreUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = usuarioServicio.obtenerUsuarioPorEmail(nombreUsuario);

        Optional <Noticia> respuesta = noticiaRepositorio.findById(id);
        
        if(respuesta.isPresent()){
            Noticia noticia = respuesta.get();

            if(usuario instanceof Periodista && noticia.getPeriodista() != null && noticia.getPeriodista().equals(usuario)){
                noticia.setTitulo(titulo);
                noticia.setCuerpo(cuerpo);
                noticia.setPeriodista((Periodista) usuario);
            }else if (usuario instanceof Admin) {
                // Si es un administrador, puede modificar cualquier noticia
                noticia.setTitulo(titulo);
                noticia.setCuerpo(cuerpo);

                // Puedes implementar lógica específica para asignar el periodista si es necesario
            }

            noticiaRepositorio.save(noticia);
        }
        
    }
    
    @Transactional
    public void eliminar(String id) throws MiException{
  
         Noticia noticia = noticiaRepositorio.getById(id);
         noticiaRepositorio.delete(noticia);
    }
    
    public Noticia getOne(String id){
        return noticiaRepositorio.getOne(id);
    }
    public void validar(String titulo, String cuerpo) throws MiException{
        if(titulo == null || titulo.isEmpty()){
            throw new MiException("El titulo no puede ser nulo ni estar vacio");
        }
        if(cuerpo == null || cuerpo.isEmpty()){
            throw new MiException("El cuerpo del texto no puede ser nulo ni estar vacio");
        }
    }
    
    
}
