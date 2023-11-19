package com.example.servicio;

import com.example.dao.TransaccionDao;
import com.example.domain.Transacciones;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransaccionServiceImpl implements TransaccionService {

    @Autowired
    private TransaccionDao transaccionDao;

    @Override
    public List<Transacciones> listarTransacciones() {
        return (List<Transacciones>) transaccionDao.findAll();
    }

    @Override
    public void guardar(Transacciones transaccion) {
        transaccionDao.save(transaccion);
    }

    @Override
    public void eliminar(Transacciones transaccion) {
        transaccionDao.delete(transaccion);
    }

    @Override
    public Transacciones encontrarTransacciones(Transacciones transaccion) {
        return transaccionDao.findById(transaccion.getId()).orElse(null);
    }

}
