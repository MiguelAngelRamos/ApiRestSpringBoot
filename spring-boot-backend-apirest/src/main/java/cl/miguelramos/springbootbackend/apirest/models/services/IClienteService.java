package cl.miguelramos.springbootbackend.apirest.models.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cl.miguelramos.springbootbackend.apirest.models.entity.Cliente;

public interface IClienteService {
	
	public List<Cliente> findAll();
	
	public Page<Cliente> findAll(Pageable pageable); // para la paginación
	
	public Cliente findById(Long id);
	
	public Cliente save(Cliente cliente ); /* recibe el cliente que vamos almacenar y retorna el cliente guardado que contiene el id*/
	
	public void delete(Long id);
}
