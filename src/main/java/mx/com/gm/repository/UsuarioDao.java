package mx.com.gm.repository;

import mx.com.gm.domain.Usuario;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioDao extends JpaRepository<Usuario, Long> {

    Usuario findByUsername(String username);

    @Query("SELECT COUNT(u) FROM Usuario u JOIN u.roles r WHERE r.nombre = :rolNombre")
    long countByRoleName(@Param("rolNombre") String rolNombre);

    @Query("SELECT u FROM Usuario u JOIN FETCH u.roles")
    List<Usuario> findAllWithRoles();

    @Query("SELECT u.id_usuario, u.username, u.nombre, u.apellido, u.celular, u.correo, u.dni, r.nombre,u.password FROM Usuario u JOIN u.roles r")
    List<Object[]> findAllUsersWithRoleNames();
}
