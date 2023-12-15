/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.proyecto.controladores;
import com.example.proyecto.entidades.Usuario;
import com.example.proyecto.entidades.Noticia;
import com.example.proyecto.excepciones.MiException;
import com.example.proyecto.servicios.NoticiaServicio;
import java.util.List;

import com.example.proyecto.servicios.UsuarioServicio;
import javax.servlet.http.HttpSession;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author aname
 */

@Controller
@RequestMapping("/")
public class PortalControlador {
    @Autowired
    private NoticiaServicio noticiaServicio;
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @GetMapping("/")
    public String index(ModelMap modelo){
        
        return "index.html";
    }
    
    /*debe estar logeado previamente para acceder al inicio*/
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_PERIODISTA')")
    @GetMapping("/inicio")
    public String inicio(HttpSession session, ModelMap modelo){
        List<Noticia> noticias = noticiaServicio.listarNoticias();
        modelo.addAttribute("noticias", noticias);
        Usuario logueado = (Usuario) session.getAttribute("usuariosession");
        
        if (logueado.getRol().toString().equals("ADMIN") || logueado.getRol().toString().equals("PERIODISTA")) {
            return "redirect:/admin/dashboard";
        }
        
        return "inicio.html";  
    }


    @GetMapping("/registrar")
    public String registrar(){
        return "registro.html";
    }
    @PostMapping("/registro")
    public String registro( @RequestParam String nombre, @RequestParam String email,
                           @RequestParam String password, String password2, ModelMap modelo, MultipartFile archivo){
        try {
            usuarioServicio.registrar(archivo, nombre,email,password,password2);
            modelo.put("exito", "Se registro exitosamente");
            return "index.html";
        }catch (MiException ex){
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            return "registro.html";
        }

    }
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, ModelMap modelo){
        if(error != null){
            modelo.put("error", "email o contraseña invalida");
        }

        return "login.html";
    }

    //perfil de un usuario registrado
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_PERIODISTA')")
    @GetMapping("/perfil")
    public String perfil(ModelMap modelo, HttpSession session){
        Usuario usuario = (Usuario) session.getAttribute("usuariosession");
        System.out.println("CLASEEEE"+usuario.getClass());
        modelo.put("usuario", usuario);
        return "usuario_modificar.html";
    }
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_PERIODISTA')")
    @PostMapping("/perfil/{id}")
    public String actualizar(MultipartFile archivo, @PathVariable String id,  @RequestParam String nombre, @RequestParam String email, @RequestParam String password, @RequestParam String password2,ModelMap modelo) throws MiException{
        try {
            usuarioServicio.actualizar(archivo, id, nombre, email, password, password2);
            modelo.put("exito", "Usuario actualizado correctamente");
            return "inicio.html";
        }catch (MiException ex){
            modelo.put("error", ex.getMessage());
            modelo.put("nombre", nombre);
            modelo.put("email", email);
            return "usuario_modificar.html";
        }
    }

    

}
