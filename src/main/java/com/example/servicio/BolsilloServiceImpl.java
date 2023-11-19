package com.example.servicio;

import com.example.dao.BolsilloDao;
import com.example.domain.Bolsillos;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BolsilloServiceImpl implements BolsilloService {

    @Autowired
    private BolsilloDao bolsilloDao;

    @Override
    public List<Bolsillos> listarBolsillos() {
        return (List<Bolsillos>) bolsilloDao.findAll();
    }

    @Override
    public void guardar(Bolsillos bolsillo) {
        bolsilloDao.save(bolsillo);
    }

    @Override
    public void eliminar(Bolsillos bolsillo) {
        bolsilloDao.delete(bolsillo);
    }

    @Override
    public Bolsillos encontrarBolsillo(Bolsillos bolsillo) {
        return bolsilloDao.findById(bolsillo.getId()).orElse(null);
    }

}
