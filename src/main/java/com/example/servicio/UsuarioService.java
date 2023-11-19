package com.example.servicio;

import com.example.domain.Usuarios;
import java.util.List;

public interface UsuarioService {

    public List<Usuarios> listarUsuarios();

    public void guardar(Usuarios usuario);

    public void eliminar(Usuarios usuario);

    public Usuarios encontrarUsuario(Usuarios usuario);

    public void actualizar(Usuarios usuario);
}
