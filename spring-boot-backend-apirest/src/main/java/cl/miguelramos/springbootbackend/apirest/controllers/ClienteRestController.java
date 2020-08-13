package cl.miguelramos.springbootbackend.apirest.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
	// para el paginador
	@GetMapping("/clientes/page/{page}")
	public Page<Cliente> index(@PathVariable Integer page) {
		Pageable pageable = PageRequest.of(page, 4);
		return clienteService.findAll(pageable);
	}
	
	@GetMapping("/clientes/{id}")
	// @ResponseStatus(HttpStatus.OK) /* Esto seria redundante ya que si lo crea de forma automatica devuelve el estado 200*/
	public ResponseEntity<?> show(@PathVariable Long id) {
		Cliente cliente = null;
		Map<String, Object> response = new HashMap<>();
	
		try {
			cliente = clienteService.findById(id);
		} catch (DataAccessException e) {
			// DataAccessException para manejar errores en spring del api dao repository
			response.put("mensaje", "Error al realizar la consulta en la base de datos");
			response.put("error",  e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if(cliente == null) {
			response.put("mensaje", "El cliente ID: ".concat(id.toString()).concat(" no existe en la base de datos!"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Cliente>(cliente, HttpStatus.OK);
	}
	
	@PostMapping("/clientes")
	// @ResponseStatus(HttpStatus.CREATED) /*Aca si necesitamos el ResponseStatus CREATED devuelve codigo 201*/
	public ResponseEntity<?> create(@Valid @RequestBody Cliente cliente, BindingResult result) {
		/*Como es un objeto json que se envia desde el cliente angular dentro del cuerpo del request, tenemos que usar el @RequestBody,
		 * para poder transformar esos datos, para que spring los vaya a buscar tome esos parametros y poble o mapee al objeto cliente 
		 * y lo guarda*/
		/* se retorna el objeto creado*/
		Cliente clienteNew = null;
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			/* List<String> errors = new ArrayList<>(); 
			for(FieldError err: result.getFieldErrors()) {
				// result.getFieldErrors() retorna una lista de los errores
				errors.add("El campo '" + err.getField()+ "'" + err.getDefaultMessage());
			} forma anterior al JAVA 8*/
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage()) // se recibe un fielerror y se convierte un string
					.collect(Collectors.toList()); // collect(Collectors.toList()); me sirve para convertilo en una lista
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		try {
			clienteNew = clienteService.save(cliente);
		} catch(DataAccessException e) {
			response.put("mensaje", "Error a realizar el insert en la base de Datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El cliente ha sido creado con éxito!");
		response.put("cliente", clienteNew);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@PutMapping("/clientes/{id}")
	// @ResponseStatus(HttpStatus.CREATED) /*Aca si necesitamos el ResponseStatus CREATED devuelve codigo 201*/
	public ResponseEntity<?> update(@Valid @RequestBody Cliente cliente, BindingResult result, @PathVariable Long id ) {

		Cliente clienteActual = clienteService.findById(id);
		Cliente clienteUpdated = null;
		Map<String, Object> response = new HashMap<>();
		
		if(result.hasErrors()) {
			List<String> errors = result.getFieldErrors()
					.stream()
					.map(err -> "El campo '" + err.getField() +"' "+ err.getDefaultMessage())
					.collect(Collectors.toList());
			response.put("errors", errors);
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
			
		}
		
		if(clienteActual == null) {
			response.put("mensaje", "Error: nose pudo editar, el cliente ID: ".concat(id.toString()).concat(" no existe en la base de datos!"));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		try {
			clienteActual.setApellido(cliente.getApellido());
			clienteActual.setNombre(cliente.getNombre());
			clienteActual.setEmail(cliente.getEmail());
			clienteActual.setCreateAt(cliente.getCreateAt());
			
			clienteUpdated = clienteService.save(clienteActual);
		} catch(DataAccessException e ) {
			response.put("mensaje", "Error al realizar la actualizacion en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		response.put("mensaje", "El cliente ha sido actualizado con exito");
		response.put("cliente", clienteUpdated);
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/clientes/{id}")
	// @ResponseStatus(HttpStatus.NO_CONTENT) /* Retorna un 204 */
	public ResponseEntity<?> delete(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		try {
			clienteService.delete(id); // no es necesario validar si el cliente existe en la base de datos spring lo hace por debajo
		} catch(DataAccessException e) {
			response.put("mensaje", "Error al realizar la eliminación del usuario en la base de datos");
			response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("mensaje", "El cliente eliminado con éxito!");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	
	}
	// se obtiene automaticamente desde el request el nombre del parametro es archivo 
	@PostMapping("/clientes/upload")
	public ResponseEntity<?> upload(@RequestParam("archivo") MultipartFile archivo, @RequestParam("id") Long id){
		Map<String, Object> response = new HashMap<>();
		// capturar el cliente por su id
		Cliente cliente = clienteService.findById(id);
		// validamos si el archivo viene si es distinto de empty
		if(!archivo.isEmpty()) {
			String nombreArchivo = archivo.getOriginalFilename();
			Path rutaArchivo = Paths.get("upload").resolve(nombreArchivo).toAbsolutePath();
			// file copy mueve el archivo a la ruta escojida
			try {
				Files.copy(archivo.getInputStream(), rutaArchivo);
			} catch (IOException e) {
				response.put("mensaje", "Error al subir la imagen del cliente" + nombreArchivo);
				response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
			}
			// si todo sale bien se sube y no hay ningun error
			cliente.setFoto(nombreArchivo);
			// actualizar el cliente
			clienteService.save(cliente);
			response.put("cliente", cliente);
			response.put("mensaje", "Has subido correctamente la imagen: " + nombreArchivo);
		}

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

}
