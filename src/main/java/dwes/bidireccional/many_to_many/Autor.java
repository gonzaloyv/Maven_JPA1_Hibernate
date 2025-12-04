package dwes.bidireccional.many_to_many;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity( name = "bi_autor_many_to_many")
@Table(name = "bi_autor_many_to_many")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    // LADO INVERSO de la relación (no dueño)
    @ManyToMany(mappedBy = "autores",
                cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    /*
        mappedBy = "autores" → indica que el dueño es Libro,
        concretamente el atributo List<Autor> llamado "autores".

        Cascade PERSIST + MERGE permite guardar la relación correctamente
        sin causar borrados accidentales.
    */
    private List<Libro> libros = new ArrayList<>();

    public Autor() {}

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

	public List<Libro> getLibros() {
		return libros;
	}

	public Autor(String nombre) {
        this.nombre = nombre;
    }

    // Método helper para sincronizar la relación
    public void addLibro(Libro libro) {
        if (!libros.contains(libro)) {
            libros.add(libro);
        }
        if (!libro.getAutores().contains(this)) {
            libro.getAutores().add(this);
        }
    }

    // Getters y setters...
}