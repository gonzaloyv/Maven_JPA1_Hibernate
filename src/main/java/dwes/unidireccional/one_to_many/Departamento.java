package dwes.unidireccional.one_to_many;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity(name="uni_departamento_one_to_many")
@Table(name="uni_departamento_one_to_many")
public class Departamento {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@OneToMany
	@JoinColumn(name="idDepartamento")
	/*
	 * @JoinColumn ➜ crea la FK idDepartamento en la tabla de empleados.
	 * Unidireccional: Empleado NO tiene referencia a Departamento.
	 */
// 
//			@JoinColumn en la entidad cuya tabla no tiene esa FK??? raro raro
//		
//	@JoinColumn SIEMPRE indica la columna de la tabla que contiene la FK
//	… pero esa tabla no siempre es la de la entidad donde pones la anotación.
//	Porque en una relación 1:N unidireccional, el lado “uno” es el dueño de la relación,
//	pero la FK se coloca SIEMPRE en el lado “muchos", porque:
//	✔️ En 1:N, SOLO tiene sentido colocar la FK en el lado N
//
//	Un empleado pertenece a un departamento → FK en empleado
//
//	Un departamento NO pertenece a un empleado → NO tiene FK

// 🧠 Regla de oro:
// 
//En relaciones 1:N, la FK SIEMPRE va en el lado “muchos”,
//incluso si @JoinColumn está puesto en el lado “uno”.

//	En 1:1, la FK va donde esté el @JoinColumn.
//
//	En 1:N, la FK SIEMPRE va en el lado N,
//	aunque el @JoinColumn esté en el lado 1.
//
//	Esto es así porque SQL no puede poner listas en una fila del lado 1.
	private List<Empleado> empleados = new ArrayList<>();
	
	public void addEmpleado(Empleado empleado) {
		empleados.add(empleado);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

//			PROHIBIDO!!! NUNCA HACER UN SETTER DE LISTA
//	public List<Empleado> getEmpleados() {
//		return empleados;
//	}
//
//	public void setEmpleados(List<Empleado> empleados) {
//		this.empleados = empleados;
//	}
}