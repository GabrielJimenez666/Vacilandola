package com.example.servicio;

import com.example.domain.Notificaciones;
import java.util.List;

public interface NotificacionService {

    public List<Notificaciones> listarNotificaciones();

    public void guardar(Notificaciones notificacion);

    public void eliminar(Notificaciones notificacion);

    public Notificaciones encontrarNotificacion(Notificaciones notificacion);

}
