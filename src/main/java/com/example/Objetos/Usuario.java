package com.example.Objetos;

import java.time.LocalDate;
import java.util.ArrayList;
import lombok.Data;

//el @Data nos da todos los getters y los setters
@Data
public class Usuario {

    private int id;
    private String usuario;
    private String contrasena;
    private String email;
    private String fecha_ingreso;
    private double saldo;
    private ArrayList<Bolsillo> bolsillos = new ArrayList<Bolsillo>();
    private ArrayList<Transaccion> transacciones = new ArrayList<Transaccion>();
    private ArrayList<Notificacion> notificaciones = new ArrayList<Notificacion>();
    // private ArrayList<Meta> metas = new ArrayList<Meta>() ;

    public Usuario(int id, String usuario, String contrasena, String email, String fecha_ingreso, double saldo) {
        setId(id);
        setUsuario(usuario);
        setContrasena(contrasena);
        setEmail(email);
        setFecha_ingreso(fecha_ingreso);
        setSaldo(saldo);
    }

    public void agregar_bolsillo(Bolsillo bolsillo) {
        this.getBolsillos().add(bolsillo);
    }

    public void eliminar_bolsillo(Bolsillo bolsillo) {
        this.getBolsillos().remove(bolsillo);
    }

    public void cambiar_contra(String intento, String nueva) {
        if (intento.equals(this.getContrasena())) {
            this.setContrasena(nueva);
        }
    }

    // en este metodo se pasa dinero del saldo principal del usuario a uno de sus
    // bolsillos
    public void agregar_dinero_bolsillo(Bolsillo bolsillo, double monto) {
        this.setSaldo(this.getSaldo() - monto);
        bolsillo.setSaldo(bolsillo.getSaldo() + monto);
    }

    public void agregar_transaccion(Transaccion transaccion) {
        this.getTransacciones().add(transaccion);
    }

    // este metodo es cuando se simula un gasto manualmente, y se hace el pago con
    // el saldo principal
    public void simular_gasto(int id, double monto) {
        this.agregar_transaccion(new Transaccion(id, this.getId(), "gasto", LocalDate.now().toString(), monto));
        this.setSaldo(this.getSaldo() - monto);
    }

    // este metodo es cuando se simula un gasto manualmente, y se hace el pago con
    // un bolsillo
    public void simular_gasto(int id, double monto, int bolId) {
        this.agregar_transaccion(new Transaccion(id, this.getId(), "gasto", LocalDate.now().toString(), monto));
        for (Bolsillo bol : this.getBolsillos()) {
            if (bol.getId() == bolId) {
                bol.setSaldo(bol.getSaldo() - monto);
            }
        }
    }

    public void simular_ingreso(int id, double monto) {
        this.agregar_transaccion(new Transaccion(id, this.getId(), "ingreso", LocalDate.now().toString(), monto));
        this.setSaldo(this.getSaldo() + monto);
    }

    public void agregar_notificacion(Notificacion notificacion) {
        this.getNotificaciones().add(notificacion);
    }

    public void eliminar_notificacion(Notificacion notificacion) {
        this.getNotificaciones().remove(notificacion);
    }

}
