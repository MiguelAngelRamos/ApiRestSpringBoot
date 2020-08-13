package cl.miguelramos.springbootbackend.apirest.models.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="clientes") // buena practica las tablas empiezan con minuscula y con s al final
public class Cliente implements Serializable{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) // identity se genera de forma incremental
	private Long id;
	
	/*@Column no es necesario aqui por que los campos se van a llamar iguales en la base de datos*/
	@NotEmpty // el campo no puede estar vacio
	// @NotEmpty(message="no puede estar vacio") podemos customizar el mensaje, se puede hacer con el Email y el Size
	@Size(min=4, max=12)
	@Column(nullable=false)
	private String nombre;
	@NotEmpty // el campo no puede estar vacio
	private String apellido;
	@NotEmpty // el campo no puede estar vacio
	@Email // para el formato correcto del email
	@Column(nullable=false, unique=true)
	private String email;
	
	@NotNull(message="no puede estar vacio") // no puede ser null por que es un objeto de Date si fuera un string seria Notempty
	@Column(name="create_at")
	@Temporal(TemporalType.DATE) // para transformar esta fecha java en una fecha de sql
	private Date createAt;
	
	/* antes de que se haga un save un persist nos cree la fecha*/
	/* @PrePersist 
	public void prePersist() {
		createAt = new Date();
	} 
	comentado por que se va crear en el formulario a travez de un data picker en angular
	*/
	
	private String foto;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
		
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	
	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
