package mx.com.gm.servicio;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import mx.com.gm.domain.Rol;
import mx.com.gm.domain.Usuario;
import mx.com.gm.repository.RolDao;
import mx.com.gm.repository.UsuarioDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService")
public class UsuarioServiceImpl implements UserDetailsService, UsuarioService {

    @Autowired
    private UsuarioDao usuarioDao;

    @Autowired
    private RolDao rolDao;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioDao.findByUsername(username);

        if (usuario == null) {
            throw new UsernameNotFoundException(username);
        }

        List<GrantedAuthority> roles = new ArrayList<>();
        for (Rol rol : usuario.getRoles()) {
            roles.add(new SimpleGrantedAuthority(rol.getNombre()));
        }

        return new User(usuario.getUsername(), usuario.getPassword(), roles);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios() {
        return usuarioDao.findAll();
    }

    @Override
    @Transactional
    public void guardar(Usuario usuario) {
        usuarioDao.save(usuario);
    }

    @Override
    @Transactional
    public void eliminar(Usuario usuario) {
        usuarioDao.delete(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario encontrarPersona(Usuario usuario) {
        return usuarioDao.findById(usuario.getId_usuario()).orElse(null);
    }

    @Transactional
    public Usuario registrarUsuario(Usuario usuario) {
        Optional<Rol> rolPacienteOpt = rolDao.findByNombre("ROLE_PATIENT");
        Rol rolPaciente;

        if (rolPacienteOpt.isEmpty()) {
            rolPaciente = new Rol();
            rolPaciente.setNombre("ROLE_PATIENT");
            rolPaciente = rolDao.save(rolPaciente);
        } else {
            rolPaciente = rolPacienteOpt.get();
        }

        usuario.addRole(rolPaciente);
        return usuarioDao.save(usuario);
    }

    @Transactional
    public void eliminarUsuario(Long idUsuario) {
        usuarioDao.deleteById(idUsuario);
    }

    public long countUsersWithAdminRole(String rol) {
        return usuarioDao.countByRoleName(rol);
    }

    public List<Object[]> findAllUsersWithRoleNames() {
        return usuarioDao.findAllUsersWithRoleNames();
    }
}

