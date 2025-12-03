package dwes.unidireccional.one_to_one;

import org.hibernate.Session;

import dwes.HibernateUtil;

public class Prueba {
	public static void UniOneToOne() {
		Persona p = new Persona("Gonzalo", 46, "123456789");
		DNI dni = new DNI("12345678A");

		p.setDni(dni); // 🔥 Muy importante porque mantiene la relación en ambos lados.

		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		session.persist(p); // Al tener cascade, también persiste DNI

		session.getTransaction().commit();
		session.close();
	}
}
