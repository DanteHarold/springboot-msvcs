package com.hq.springcloud.msvc.cursos.repository;

import com.hq.springcloud.msvc.cursos.models.entity.Curso;
import org.springframework.data.repository.CrudRepository;

public interface CursoRepository extends CrudRepository<Curso,Long> {
}
