package com.hq.springcloud.msvc.cursos.controllers;

import com.hq.springcloud.msvc.cursos.models.entity.Curso;
import com.hq.springcloud.msvc.cursos.service.CursoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class CursoController {
    @Autowired
    private CursoService service;

    @GetMapping
    public ResponseEntity<List<Curso>> listar(){
        return ResponseEntity.ok(service.listar());
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id){
        Optional<Curso> cursoOptional = service.porId(id);
        if(cursoOptional.isPresent()){
            return ResponseEntity.ok(cursoOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Curso curso, BindingResult result){
        if(result.hasErrors()){
            return validar(result);
        }
        Curso cursoDb = service.guardar(curso);
        return ResponseEntity.status(HttpStatus.CREATED).body(cursoDb);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Curso curso, BindingResult result,@PathVariable Long id){
        if(result.hasErrors()){
            return validar(result);
        }
        Optional<Curso> cursoOptional = service.porId(id);
        if(cursoOptional.isPresent()){
            Curso cursoDb = cursoOptional.orElseThrow();
            cursoDb.setNombre(curso.getNombre());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(cursoDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        Optional<Curso> cursoOptional = service.porId(id);
        if(cursoOptional.isPresent()){
            service.eliminar(cursoOptional.orElseThrow().getId());
            return  ResponseEntity.noContent().build();
        }
        return  ResponseEntity.notFound().build();
    }

    private static ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String,String> errors = new HashMap<>();
        result.getFieldErrors().forEach(err->{
            errors.put(err.getField(),"El Campo "+err.getField()+" "+err.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
