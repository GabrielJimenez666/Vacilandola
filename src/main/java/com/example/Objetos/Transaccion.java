package com.example.Objetos;

import lombok.Data;

//el @Data nos da todos los getters y los setters
@Data
public class Transaccion {

    private int id;
    private int usuario_id;
    private String tipo;
    private String fecha;
    private double monto;

    public Transaccion(int id, int usuario_id, String tipo, String fecha, double monto) {
        this.id = id;
        this.usuario_id = usuario_id;
        this.tipo = tipo;
        this.fecha = fecha;
        this.monto = monto;
    }

}
