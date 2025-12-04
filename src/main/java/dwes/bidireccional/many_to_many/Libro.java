package dwes.bidireccional.many_to_many;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity(name = "bi_libro_many_to_many")
@Table(name = "bi_libro_many_to_many")
public class Libro {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    // LADO DUEÑO de la relación
    @ManyToMany
    @JoinTable(
        name = "autor_libro",                          // tabla intermedia
        joinColumns = @JoinColumn(name = "libro_id"),  // FK hacia libro
        inverseJoinColumns = @JoinColumn(name = "autor_id") // FK hacia autor
    )
    /*
        Libro es el dueño LA RELACIÓN porque define @JoinTable.
        Autor solo refleja la relación con mappedBy.
    */
    private List<Autor> autores = new ArrayList<>();

    public Libro() {}

    public Libro(String titulo) {
        this.titulo = titulo;
    }

    // Método helper de sincronización
    public void addAutor(Autor autor) {
        if (!autores.contains(autor)) {
            autores.add(autor);
        }
        if (!autor.getLibros().contains(this)) {
            autor.getLibros().add(this);
        }
    }

    // Getters y setters...
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public List<Autor> getAutores() {
		return autores;
	}
}
