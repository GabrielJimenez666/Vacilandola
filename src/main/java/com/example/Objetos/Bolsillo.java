package com.example.Objetos;

import lombok.Data;

//el @Data nos da todos los getters y los setters
@Data
public class Bolsillo {

    private int id;
    private int usuario_id;
    private String nombre;
    private double saldo;

    public Bolsillo(int id, int usuario_id, String name, double saldo) {
        setId(id);
        setUsuario_id(usuario_id);
        setNombre(name);
        setSaldo(saldo);
    }

}
