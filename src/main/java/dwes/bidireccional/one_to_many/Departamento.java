package dwes.bidireccional.one_to_many;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity(name = "bi_departamento_one_to_many")
@Table(name = "bi_departamento_one_to_many")
public class Departamento {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    // LADO INVERSO (no dueño)
    @OneToMany(mappedBy = "departamento",
               cascade = CascadeType.ALL,
               orphanRemoval = true)
    /*
        mappedBy = "departamento"
        ➜ indica que la FK está en Empleado.departamento

        cascade = ALL
        ➜ al guardar/eliminar Departamento, se guardan/elim. sus empleados

        orphanRemoval = true
        ➜ si quito un empleado de la lista → se borra de la BD
    */
    private List<Empleado> empleados = new ArrayList<>();

    public Departamento() {}

    public Departamento(String nombre) {
        this.nombre = nombre;
    }

    // 🔥 Método helper para mantener la relación sincronizada
    public void addEmpleado(Empleado e) {
        empleados.add(e);
        if (e.getDepartamento() != this) {
            e.setDepartamento(this);
        }
    }

    public void removeEmpleado(Empleado e) {
        empleados.remove(e);
        if (e.getDepartamento() != null) {
            e.setDepartamento(null);
        }
    }

    // getters y setters...
    public Long getId() {
		return id;
	}

	public List<Empleado> getEmpleados() {
		return empleados;
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
}
