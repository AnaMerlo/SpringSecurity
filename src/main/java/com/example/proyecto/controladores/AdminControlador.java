/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.proyecto.controladores;

import com.example.proyecto.entidades.Admin;
import com.example.proyecto.entidades.Noticia;
import com.example.proyecto.entidades.Periodista;
import com.example.proyecto.entidades.Usuario;
import com.example.proyecto.excepciones.MiException;
import com.example.proyecto.servicios.NoticiaServicio;
import java.util.List;

import com.example.proyecto.servicios.UsuarioServicio;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author aname
 */
@Controller
@RequestMapping("/admin")
public class AdminControlador {
    
    @Autowired
    private NoticiaServicio noticiaServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;
    @GetMapping("/dashboard")
    public String panelAdministrativo(ModelMap modelo){
        List<Noticia> noticias = noticiaServicio.listarNoticias();
        modelo.addAttribute("noticias", noticias);
        return "panel.html";
    }
                                                    //usuario servicio
   @GetMapping("/usuarios")
    public String listar(ModelMap modelo){
        List<Usuario> usuarios = usuarioServicio.usuarioList();
        modelo.addAttribute("usuarios", usuarios);
        return "usuario_list.html";
   }
   @GetMapping("/modificarRol/{id}")
    public String cambiarRol(@PathVariable String id){
        usuarioServicio.cambiarRol(id);
        return "redirect:/admin/usuarios";
   }
   @GetMapping("/activo/{id}")
    public String isActive(@PathVariable String id){
        usuarioServicio.changeActive(id);

        return "panel.html";
   }
   @PostMapping("/asignarSueldo/{id}")
   public String asignarSueldo(@PathVariable String id, @RequestParam Integer nuevoSueldo)throws MiException{
       usuarioServicio.asignarSueldo(id, nuevoSueldo);
       return "redirect:/admin/panel"; // Redirigir a donde sea apropiado
   }

}
