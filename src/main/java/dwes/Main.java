package dwes;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class Main {
	public static void main(String[] args) {
// 		Anticuado
//		SessionFactory factory = new Configuration().configure().buildSessionFactory();
//		Session session = factory.openSession();
		
		StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
		SessionFactory factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
		Session session = factory.getCurrentSession();
			
		session.beginTransaction();
		
		Persona p = new Persona("Gonzalo Yanez", 46, "123456789");
		session.persist(p);
		
		session.getTransaction().commit();
		session.close();
		
		factory.close();
		
		System.out.println("Persona guardada");
	}
}
