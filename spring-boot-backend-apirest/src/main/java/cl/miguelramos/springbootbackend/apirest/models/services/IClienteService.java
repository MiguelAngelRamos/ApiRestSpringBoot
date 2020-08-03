package cl.miguelramos.springbootbackend.apirest.models.services;

import java.util.List;

import cl.miguelramos.springbootbackend.apirest.models.entity.Cliente;

public interface IClienteService {
	
	public List<Cliente> findAll();
	
	public Cliente findById(Long id);
	
	public Cliente save(Cliente cliente ); /* recibe el cliente que vamos almacenar y retorna el cliente guardado que contiene el id*/
	
	public void delete(Long id);
}
