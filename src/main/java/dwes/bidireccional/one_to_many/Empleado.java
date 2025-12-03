package dwes.bidireccional.one_to_many;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity(name = "bi_empleado_one_to_many")
@Table(name = "bi_empleado_one_to_many")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	private String nombre;

    // LADO DUEÑO (owner)
    @ManyToOne
    @JoinColumn(name = "id_departamento")
    /*
        Este lado ES EL DUEÑO.
        ➜ id_departamento es la FK real en la tabla empleado.
    */
    
//    Con bidireccionalidad puedo:
//    * Puedo navegar desde Empleado hacia Departamento
//    * Debo sincronizar ambos lados
//    * Debo usar mappedBy en el lado inverso
    private Departamento departamento;

    public Empleado() {}

    public Empleado(String nombre) {
        this.nombre = nombre;
    }

    // Setter sincronizado (para evitar inconsistencia)
    public void setDepartamento(Departamento d) {
        this.departamento = d;

        // Sincronizar el otro lado
        if (d != null && !d.getEmpleados().contains(this)) {
            d.getEmpleados().add(this);
        }
    }

    // getters y setters..
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

	public Departamento getDepartamento() {
		return departamento;
	}
}