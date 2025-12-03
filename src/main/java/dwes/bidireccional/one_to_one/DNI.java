package dwes.bidireccional.one_to_one;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity(name="bi_dni_one_to_one")
@Table(name="bi_dni_one_to_one")
public class DNI {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String numero;

	@OneToOne(mappedBy = "dni")
	/*
	 * mappedBy = "dni" ➜ - "dni" es el nombre del atributo en la clase Persona que
	 * TIENE la FK y la anotación @JoinColumn. - Este lado (DNI) es el lado INVERTO
	 * / no dueño. - NO lleva @JoinColumn, porque la FK ya está en Persona.
	 */
	private Persona persona;

	public DNI() {
	}

	public DNI(String numero) {
		this.numero = numero;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
//		En una relación bidireccional, los dos lados deben mantenerse coherentes.
//
//		Los setters sincronizados actualizan automáticamente el otro lado,
//		pero el if evita una recursión infinita y garantiza que la sincronización solo se haga una vez.
//
//		Esto asegura que:
//
//		persona.getDni().getPersona() == persona
		if (persona != null && persona.getDni() != this) {
			persona.setDni(this);
		}
	}
}
