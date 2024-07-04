package mx.com.gm.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import mx.com.gm.domain.Rol;

public interface RolDao extends JpaRepository<Rol, Long> {
    Optional<Rol> findByNombre(String nombre);
}