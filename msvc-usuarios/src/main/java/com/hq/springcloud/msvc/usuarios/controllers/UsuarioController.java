package com.hq.springcloud.msvc.usuarios.controllers;

import com.hq.springcloud.msvc.usuarios.models.entity.Usuario;
import com.hq.springcloud.msvc.usuarios.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
//@RequestMapping("/api")
public class UsuarioController {
    @Autowired
    private UsuarioService service;

    @GetMapping("/")
    public List<Usuario> listar(){
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id){
        Optional<Usuario> usuarioOptional = service.porId(id);
        if(usuarioOptional.isPresent()){
            return ResponseEntity.ok().body(usuarioOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    //@ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario, BindingResult result){
        if(result.hasErrors()){
            return validar(result);
        }
        if(!usuario.getEmail().isEmpty() && service.porEmail(usuario.getEmail()).isPresent()){
            return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje","Ya Existe un Usuario con el mismo Correo!"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuario));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Usuario usuario, BindingResult result, @PathVariable Long id){

        if(result.hasErrors()){
            return validar(result);
        }
        Optional<Usuario> usuarioOptional = service.porId(id);
        if(usuarioOptional.isPresent()){
            Usuario usuarioDb = usuarioOptional.orElseThrow();
            System.out.println(!(usuario.getEmail().equalsIgnoreCase(usuarioDb.getEmail())));
            if(!usuario.getEmail().isEmpty() && (usuario.getEmail().equalsIgnoreCase(usuarioDb.getEmail())) && service.porEmail(usuario.getEmail()).isPresent()){
                return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje","Ya Existe un Usuario con el mismo Correo!"));
            }

            usuarioDb.setNombre(usuario.getNombre());
            usuarioDb.setEmail(usuario.getEmail());
            usuarioDb.setPassword(usuario.getPassword());

            return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuarioDb));
        }
        return  ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        Optional<Usuario> usuarioOptional = service.porId(id);
        if(usuarioOptional.isPresent()){
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    private static ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String,String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err->{
            errors.put(err.getField(),"El Campo "+err.getField()+" "+err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }

}
