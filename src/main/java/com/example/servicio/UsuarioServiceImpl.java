package com.example.servicio;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.dao.UsuarioDao;
import com.example.domain.Usuarios;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioDao personaDao;

    @Override
    public List<Usuarios> listarUsuarios() {
        return (List<Usuarios>) personaDao.findAll();
    }

    @Override
    public void guardar(Usuarios usuario) {
        usuario.setFecha_ingreso(LocalDate.now().toString());
        personaDao.save(usuario);
    }

    @Override
    public void actualizar(Usuarios usuario) {
        personaDao.save(usuario);
    }

    @Override
    public void eliminar(Usuarios usuario) {
        personaDao.delete(usuario);
    }

    @Override
    public Usuarios encontrarUsuario(Usuarios usuario) {
        return personaDao.findById(usuario.getId()).orElse(null);
    }

}
