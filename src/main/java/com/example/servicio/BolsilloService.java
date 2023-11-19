package com.example.servicio;

import com.example.domain.Bolsillos;
import java.util.List;

public interface BolsilloService {

    public List<Bolsillos> listarBolsillos();

    public void guardar(Bolsillos bolsillo);

    public void eliminar(Bolsillos bolsillo);

    public Bolsillos encontrarBolsillo(Bolsillos bolsillo);
}

