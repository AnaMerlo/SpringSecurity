package com.example.proyecto.servicios;

import com.example.proyecto.entidades.Admin;
import com.example.proyecto.entidades.Imagen;
import com.example.proyecto.entidades.Periodista;
import com.example.proyecto.entidades.Usuario;
import com.example.proyecto.enumeraciones.Rol;
import com.example.proyecto.excepciones.MiException;
import com.example.proyecto.repositorios.PeriodistaRepositorio;
import com.example.proyecto.repositorios.UsuarioRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UsuarioServicio implements UserDetailsService {
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;
    @Autowired
    private ImagenServicio imagenServicio;
    @Autowired
    private PeriodistaServicio periodistaServicio;
    @Autowired
    private PeriodistaRepositorio periodistaRepositorio;
    @Transactional
    public void registrar(MultipartFile archivo, String nombre, String email, String password, String password2 ) throws MiException{

        validar(nombre, email, password, password2);
        Usuario usuario = new Usuario();

        usuario.setNombre(nombre);
        usuario.setEmail(email);

        usuario.setPassword(new BCryptPasswordEncoder().encode(password));
        usuario.setFechaAlta(new Date());
        usuario.setRol(Rol.USER);
        usuario.setActivo(true);
        Imagen imagen = imagenServicio.guardar(archivo);
        usuario.setImagen(imagen);
        usuarioRepositorio.save(usuario);
    }
    @Transactional
    public void actualizar(MultipartFile archivo, String idUsuario, String nombre, String email, String password, String password2) throws MiException {

        validar(nombre, email, password, password2);

        Optional<Usuario> respuesta = usuarioRepositorio.findById(idUsuario);
        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();
            usuario.setNombre(nombre);
            usuario.setEmail(email);

            usuario.setPassword(new BCryptPasswordEncoder().encode(password));

            usuario.setRol(Rol.USER);

            String idImagen = null;

            if (usuario.getImagen() != null) {
                idImagen = usuario.getImagen().getIdImagen();
            }

            Imagen imagen = imagenServicio.actualizar(archivo, idImagen);

            usuario.setImagen(imagen);

            usuarioRepositorio.save(usuario);
        }

    }
    public Usuario getOne(String id){
        return usuarioRepositorio.getOne(id);
    }
    @Transactional (readOnly = true)
    public List<Usuario> usuarioList(){

        List<Usuario> usuarios = new ArrayList<>();

        usuarios = usuarioRepositorio.findAll();

        return usuarios;
    }

    @Transactional
    public void cambiarRol(String id){
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if(respuesta.isPresent()){
            Usuario usuario = respuesta.get();

            if(usuario.getRol().equals(Rol.USER)){
                usuario.setRol(Rol.PERIODISTA);
              //  usuarioRepositorio.actualizarDtypeUsuario(id, "PERIODISTA");
            } else if (usuario.getRol().equals(Rol.PERIODISTA)) {
                usuario.setRol(Rol.ADMIN);
              //  usuarioRepositorio.actualizarDtypeUsuario(id, "ADMIN");
            } else if (usuario.getRol().equals(Rol.ADMIN)) {
                usuario.setRol(Rol.USER);
               // usuarioRepositorio.actualizarDtypeUsuario(id, "USER");
            }
            usuarioRepositorio.save(usuario);

        }

    }
    @Transactional
    public void changeActive(String id){
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if(respuesta.isPresent()){

            Usuario usuario = respuesta.get();
            usuario.setActivo(!usuario.getActivo());
            usuarioRepositorio.save(usuario);
        }
    }
    public Boolean isUserActive(String email){
        Usuario usuario = usuarioRepositorio.buscarEmail(email);
        return usuario != null && usuario.getActivo();
    }

    public void asignarSueldo(String idPeriodista, Integer nuevoSueldo)throws MiException{
        Optional <Periodista> respuesta = periodistaRepositorio.findById(idPeriodista);
        
        if(respuesta.isPresent()){
            Periodista periodista = respuesta.get();
            periodista.setSueldoMensual(nuevoSueldo);

            periodistaRepositorio.save(periodista);
     
        }else {
        // Manejo de caso en el que el Periodista no se encuentra por el ID proporcionado
        throw new MiException("No se encontró un periodista con el ID: " + idPeriodista);
        }
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if(!isUserActive(email)){
            return null;
        }

        Usuario usuario = usuarioRepositorio.buscarEmail(email);
        //ACA HABIA AGREGADO USUARIO.GETaCTIVE() != FALSE. ESTE ERA UNA LOGICA MAS SENCILLA PERO NO PERMITIA VER CON CLARIDAD LAS DISTINTAS RESPONSABILIODADES, POR ELLOS SE AGREGO CODIGO NUEVO
        if (usuario != null) {

            List<GrantedAuthority> permisos = new ArrayList<>();

            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_"+ usuario.getRol().toString());

            permisos.add(p);
            //recuperacion de datos
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            

            HttpSession session = attr.getRequest().getSession(true);

            session.setAttribute("usuariosession", usuario);

            return new User(usuario.getEmail(), usuario.getPassword(),permisos);
        }else{
            return null;
        }
    }
    /*public Periodista obtenerPeriodistaPorEmail(String email) {
        Usuario usuario = usuarioRepositorio.buscarEmail(email);

        if (usuario instanceof Periodista) {
            return (Periodista) usuario;
        } else {
            throw new EntityNotFoundException("No se encontró un periodista con el email: " + email);
        }
    }*/
    public Usuario obtenerUsuarioPorEmail(String email){
        return usuarioRepositorio.buscarEmail(email);
    }
    private void validar(String nombre, String email, String password, String password2 ) throws MiException {
        if (nombre.isEmpty() || nombre == null) {
            throw new MiException("el nombre no puede ser nulo o estar vacío");
        }
        if (email.isEmpty() || email == null) {
            throw new MiException("el email no puede ser nulo o estar vacio");
        }
        if (password.isEmpty() || password == null || password.length() <= 5) {
            throw new MiException("La contraseña no puede estar vacía, y debe tener más de 5 dígitos");
        }

        if (!password.equals(password2)) {
            throw new MiException("Las contraseñas ingresadas deben ser iguales");
        }
    }
}
