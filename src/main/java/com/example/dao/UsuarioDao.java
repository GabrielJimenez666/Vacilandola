package com.example.dao;

import com.example.domain.Usuarios;
import org.springframework.data.repository.CrudRepository;

public interface UsuarioDao extends CrudRepository<Usuarios, Integer>{
    
}