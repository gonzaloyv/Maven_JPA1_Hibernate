package dwes.unidireccional.one_to_one;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name="uni_dni_one_to_one")
@Table(name="uni_dni_one_to_one")
public class DNI {
	@Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String numero;

  public DNI() {}

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
}