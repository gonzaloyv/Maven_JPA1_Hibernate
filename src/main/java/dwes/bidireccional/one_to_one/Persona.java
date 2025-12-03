package dwes.bidireccional.one_to_one;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity(name="uni_persona_one_to_one")
@Table(name="uni_persona_one_to_one")
public class Persona {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String nombre;
	
	private int edad;
	private String telefono;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	// activar cascade para que al guardar Persona → también se guarde su DNI.
	@JoinColumn(name = "dni_id", unique = true)
	/*
	 * OJO: aquí NO hay mappedBy. Esta clase es la dueña de la relación. dni_id será
	 * la FK en la tabla personas.
	 */
	private DNI dni;
	
	public DNI getDni() {
		return dni;
	}

	public void setDni(DNI dni) {
		this.dni = dni;
//		En una relación bidireccional, los dos lados deben mantenerse coherentes.
//
//		Los setters sincronizados actualizan automáticamente el otro lado,
//		pero el if evita una recursión infinita y garantiza que la sincronización solo se haga una vez.
//
//		Esto asegura que:
//
//		dni.getPersona().getDni() == dni
		if (dni != null && dni.getPersona() != this) {
			dni.setPersona(this);
		}
	}

	public Persona() {}

	public Persona(String nombre, int edad, String telefono) {
		super();
		this.nombre = nombre;
		this.edad = edad;
		this.telefono = telefono;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombreP) {
		this.nombre = nombreP;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	
	@Override
	public String toString() {
		return "Persona{id=" + id + ", nombre=" + nombre + ", edad=" + edad + ", telefono =" + telefono + "}";
	}
}