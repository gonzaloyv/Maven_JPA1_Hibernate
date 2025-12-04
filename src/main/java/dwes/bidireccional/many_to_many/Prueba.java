package dwes.bidireccional.many_to_many;

import org.hibernate.Session;

import dwes.HibernateUtil;

public class Prueba {
	public static void BiManyToMany() {
		Autor a1 = new Autor("García Márquez");
        Autor a2 = new Autor("Isabel Allende");

        Libro l1 = new Libro("Cien años de soledad");
        Libro l2 = new Libro("La casa de los espíritus");

        // Relación bidireccional sincronizada
        a1.addLibro(l1);
        a1.addLibro(l2);

        a2.addLibro(l2); // este autor escribió solo el segundo libro

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        // Como Libro es el DUEÑO y hay cascade PERSIST/MERGE,
        // basta con persistir cualquier lado, pero es más lógico persistir autores:
        session.persist(a1);
        session.persist(a2);

        session.getTransaction().commit();
        session.close();

        System.out.println("Autores y libros guardados correctamente.");
    }
}
