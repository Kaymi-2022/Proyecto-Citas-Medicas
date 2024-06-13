package mx.com.gm.servicio;

import java.util.List;

import mx.com.gm.domain.Usuario;

public interface UsuarioService {

    public List<Usuario> listarUsuarios();

    public void guardar(Usuario usuario);

    public void eliminar(Usuario usuario);

    public Usuario encontrarPersona(Usuario usuario);

}
