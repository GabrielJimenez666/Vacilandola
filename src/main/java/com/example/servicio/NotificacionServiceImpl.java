package com.example.servicio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.dao.NotificacionDao;
import com.example.domain.Notificaciones;

@Service
public class NotificacionServiceImpl implements NotificacionService {

    @Autowired
    private NotificacionDao notificacionDao;

    @Override
    public List<Notificaciones> listarNotificaciones() {
        return (List<Notificaciones>) notificacionDao.findAll();
    }

    @Override
    public void guardar(Notificaciones notificacion) {
        notificacionDao.save(notificacion);
    }

    @Override
    public void eliminar(Notificaciones notificacion) {
        notificacionDao.delete(notificacion);
    }

    @Override
    public Notificaciones encontrarNotificacion(Notificaciones notificacion) {
        return notificacionDao.findById(notificacion.getId()).orElse(null);
    }

}
