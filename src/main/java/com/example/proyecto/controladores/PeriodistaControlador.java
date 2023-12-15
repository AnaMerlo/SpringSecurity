package com.example.proyecto.controladores;

import com.example.proyecto.entidades.Noticia;

import com.example.proyecto.servicios.PeriodistaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.List;

@Controller
@RequestMapping("/admin")
public class PeriodistaControlador {
    @Autowired
    private PeriodistaServicio periodistaServicio;

    @GetMapping("/{id}/noticias")
    public String verNoticias(@PathVariable String id, ModelMap modelo) {

        List<Noticia> noticias = periodistaServicio.misNoticia(id);
        modelo.addAttribute("noticias", noticias);
        return "periodista.html";
    }
    @GetMapping("{id}/sueldo")
    public String verSueldo(@PathVariable String id, ModelMap modelo){
        Integer sueldo = periodistaServicio.sueldoPeriodista(id);
        modelo.addAttribute("sueldo", sueldo);
        return"periodista.html";
    }
}
