package com.example.servicio;

import com.example.domain.Transacciones;
import java.util.List;

public interface TransaccionService {

    public List<Transacciones> listarTransacciones();

    public void guardar(Transacciones transaccion);

    public void eliminar(Transacciones transaccion);

    public Transacciones encontrarTransacciones(Transacciones transaccion);

}
