package cl.miguelramos.springbootbackend.apirest.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.miguelramos.springbootbackend.apirest.models.entity.Cliente;

public interface IClienteDao extends JpaRepository<Cliente, Long> {

}
