package com.example.dao;

import com.example.domain.Transacciones;
import org.springframework.data.repository.CrudRepository;

public interface TransaccionDao extends CrudRepository<Transacciones, Integer>{
    
}

