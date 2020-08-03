package cl.miguelramos.springbootbackend.apirest.models.dao;

import org.springframework.data.repository.CrudRepository;

import cl.miguelramos.springbootbackend.apirest.models.entity.Cliente;

public interface IClienteDao extends CrudRepository<Cliente, Long> {

}
