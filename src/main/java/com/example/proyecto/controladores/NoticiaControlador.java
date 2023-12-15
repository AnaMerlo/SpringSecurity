/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.proyecto.controladores;

import com.example.proyecto.entidades.Noticia;
import com.example.proyecto.excepciones.MiException;
import com.example.proyecto.servicios.NoticiaServicio;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author aname
 */
@Controller
@RequestMapping("/noticia")
public class NoticiaControlador {
    
    @Autowired
    NoticiaServicio noticiaServicio;
    
    @GetMapping("/registrar")
    public String registrar(){
        
        return "noticia_form.html";
    }

    @PostMapping("/registro")
    public String registro(@RequestParam(required = false) String id, @RequestParam String titulo, @RequestParam String cuerpo, ModelMap modelo){
        try {
            noticiaServicio.crearNoticia(id, titulo, cuerpo);
            modelo.put("exito", "La noticia fue cargada con exito");  
            
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "noticia_form.html";
        } 
        return "index.html";
    }
 

    @GetMapping("/lista")
    public String listar(ModelMap modelo){
        
        List<Noticia> noticias = noticiaServicio.listarNoticias();
        modelo.addAttribute("noticias", noticias);
        return "noticia_list.html";
    }
    
    @GetMapping("/modificar/{id}")
    public String modificar(@PathVariable String id, ModelMap modelo){
        modelo.put("noticia", noticiaServicio.getOne(id));
        return "noticia_modificar.html";
    }
            
    @PostMapping("/modificar/{id}")
     public String modificar(@PathVariable String id, String titulo, String cuerpo, ModelMap modelo){
        try {
            noticiaServicio.modificarNoticia(id, titulo, cuerpo);
            return "redirect:../lista";
        } catch (MiException ex) {
           modelo.put("error", ex.getMessage());
           return "noticia_modificar.html";
        }     
    }
    //popup para elminar
     @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable String id, ModelMap modelo){
        modelo.put("noticia", noticiaServicio.getOne(id));
        return "noticia_eliminar.html";
    }
    @PostMapping("/eliminar/{id}")        
    public String eliminarNoticia(@PathVariable String id, ModelMap modelo) throws MiException{
        try {
            noticiaServicio.eliminar(id);
            return "redirect:../lista";
        } catch (MiException ex) {
            modelo.put("error", ex.getMessage());
            return "noticia_eliminar.html";
        }    
    }
    @GetMapping("/detalle/{id}")
    public String detalle(@PathVariable String id, ModelMap modelo){
        modelo.put("noticia", noticiaServicio.getOne(id));
        return "noticia_detalle.html"; 
    }
    @GetMapping("/admin")
    public String administrar(){
        return "admin.html";
    }
}





