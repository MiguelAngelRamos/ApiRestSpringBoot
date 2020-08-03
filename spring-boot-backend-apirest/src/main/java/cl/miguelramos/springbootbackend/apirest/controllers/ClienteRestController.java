package cl.miguelramos.springbootbackend.apirest.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import cl.miguelramos.springbootbackend.apirest.models.entity.Cliente;
import cl.miguelramos.springbootbackend.apirest.models.services.IClienteService;

@CrossOrigin(origins= {"http://localhost:4200"}) // Cors aca es donde especifico los dominios permitidos para interactuar con mi api
@RestController
@RequestMapping("/api")
public class ClienteRestController {
	@Autowired
	private IClienteService clienteService; // La Interface esta siendo implemantada solo una vez, por eso no hace falta un qualificador
	
	@GetMapping("/clientes")
	public List<Cliente> index() {
		return clienteService.findAll();
	}
	
	@GetMapping("/clientes/{id}")
	@ResponseStatus(HttpStatus.OK) /* Esto seria redundante ya que si lo crea de forma automatica devuelve el estado 200*/
	public Cliente show(@PathVariable Long id) {
		return clienteService.findById(id);
	}
	
	@PostMapping("/clientes")
	@ResponseStatus(HttpStatus.CREATED) /*Aca si necesitamos el ResponseStatus CREATED devuelve codigo 201*/
	public Cliente create(@RequestBody Cliente cliente) {
		/*Como es un objeto json que se envia desde el cliente angular dentro del cuerpo del request, tenemos que usar el @RequestBody,
		 * para poder transformar esos datos, para que spring los vaya a buscar tome esos parametros y poble o mapee al objeto cliente 
		 * y lo guarda*/
		/* se retorna el objeto creado*/
		return clienteService.save(cliente);
	}
	
	@PutMapping("/clientes/{id}")
	@ResponseStatus(HttpStatus.CREATED) /*Aca si necesitamos el ResponseStatus CREATED devuelve codigo 201*/
	public Cliente update(@RequestBody Cliente cliente, @PathVariable Long id) {
		Cliente clienteActual = clienteService.findById(id);
		clienteActual.setApellido(cliente.getApellido());
		clienteActual.setNombre(cliente.getNombre());
		clienteActual.setEmail(cliente.getEmail());
		
		return clienteService.save(clienteActual);
	}
	
	@DeleteMapping("/clientes/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT) /* Retorna un 204 */
	public void delete(@PathVariable Long id) {
		clienteService.delete(id);
	}

}
