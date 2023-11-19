package com.example.dao;

import com.example.domain.Notificaciones;
import org.springframework.data.repository.CrudRepository;

public interface NotificacionDao extends CrudRepository<Notificaciones, Integer> {

}
