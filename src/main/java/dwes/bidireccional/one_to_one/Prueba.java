package dwes.bidireccional.one_to_one;

import org.hibernate.Session;

import dwes.HibernateUtil;

public class Prueba {
	public static void BiOneToOne() {
		{
			Persona p = new Persona("Gonzalo", 46, "123456789");
			DNI dni = new DNI("12345678A");

			p.setDni(dni); // 🔥 Muy importante porque mantiene la relación en ambos lados.

			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();

			session.persist(p); // Al tener cascade, también persiste DNI

			session.getTransaction().commit();
			session.close();
		}
		{
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			Persona personaBD = session.find(Persona.class, 1L);
			System.out.println(personaBD.getDni().getNumero());

			DNI dniBD = session.find(DNI.class, 1L);
			System.out.println(dniBD.getPersona().getNombre());
			
			session.getTransaction().commit();
			session.close();
		}
		// orphan removal test
		{
			Session session = HibernateUtil.getSessionFactory().openSession();
			session.beginTransaction();
			
			Persona personaBD = session.find(Persona.class, 1L);
			System.out.println(personaBD.getDni().getNumero());
			
			System.out.println("Vamos a modificar el dni de " + personaBD.getDni().getNumero() 
					+ " a 11111111. El anterior dni deberia de borrarse de la BBDD porque queda huerfano.");
			personaBD.setDni(new DNI("11111111p"));
			session.getTransaction().commit();
			session.close();
		}
	}
}
