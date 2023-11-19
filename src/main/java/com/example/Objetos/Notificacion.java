package com.example.Objetos;

import lombok.Data;

//el @Data nos da todos los getters y los setters
@Data
public class Notificacion {

    private int id;
    private int usuario_id;
    private String mensaje;

    public Notificacion(int id, int usuario_id, String mensaje) {
        setId(id);
        setUsuario_id(usuario_id);
        setMensaje(mensaje);
    }

}
