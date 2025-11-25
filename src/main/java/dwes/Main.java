package dwes;

import java.util.List;
import java.util.Scanner;

import org.hibernate.Session;
class MergeCasos {
	//
	// Caso 1 - Entidad detached que venía de una sesión anterior
	//
	public static void mergeCaso1() {
		// --- SESIÓN 1 ---
		Session s1 = HibernateUtil.getSessionFactory().openSession();
		s1.beginTransaction();

		Persona p = s1.find(Persona.class, 1); // MANAGED
		s1.getTransaction().commit();
		s1.close(); // ahora p está DETACHED

		// --- Fuera de Hibernate ---
		p.setNombre("Nombre cambiado fuera de sesión");

		// --- SESIÓN 2 ---
		Session s2 = HibernateUtil.getSessionFactory().openSession();
		s2.beginTransaction();

		s2.merge(p);  // Copia datos de p a una entidad MANAGED
		              // Hibernate genera UPDATE al commit

		s2.getTransaction().commit();
		s2.close();
	}
	//
	// Caso 2 - Crear un objeto manualmente para actualizarlo (sin cargarlo antes)
	public static void mergeCaso2() {
		Persona p = new Persona();
		p.setId((long) 1);         // ID existente en BD
		p.setNombre("Nuevo");

		Session s = HibernateUtil.getSessionFactory().openSession();
		s.beginTransaction();

		s.merge(p);         // Hibernate carga Persona 1 internamente
		                    // Copia nombre = "Nuevo"
		                    // Hace UPDATE al commit

		s.getTransaction().commit();
		s.close();
	}
	//
	// Caso 3 - Insertar si es nuevo, actualizar si existe (comportamiento dual)
	//
	public static void mergeCaso3() {
		Persona p = new Persona();
		p.setNombre("Carlos");    // id == null → objeto nuevo

		Session s = HibernateUtil.getSessionFactory().openSession();
		s.beginTransaction();

		Persona managed = s.merge(p);
		// INSERT automático porque id = null

		s.getTransaction().commit();
		s.close();
	}
	//
	// Caso 4
	// 
	public static void mergeCaso4() {
		Session s = HibernateUtil.getSessionFactory().openSession();
		s.beginTransaction();

		// Esta persona ya está en la sesión
		Persona managed = s.find(Persona.class, 5);

		// Ahora llega otra instancia con el mismo ID
		Persona detached = new Persona();
		detached.setId((long)5);
		detached.setNombre("Nombre actualizado");

		// Esto daría error si usas update():
		// s.update(detached);  // ❌ excepción

		// Pero merge lo resuelve:
		s.merge(detached);   // Copia el estado sobre "managed"

		s.getTransaction().commit();
		s.close();
	}
	//
	// Caso 5
	//
	public static void mergeCaso5() {
		Session s = HibernateUtil.getSessionFactory().openSession();
		s.beginTransaction();

		Persona detached = new Persona();
		detached.setId((long)10);
		detached.setNombre("Nuevo nombre");

		Persona managed = s.merge(detached);

		System.out.println(s.contains(detached)); // false
		System.out.println(s.contains(managed));  // true

		s.getTransaction().commit();
		s.close();
	}
}

public class Main {
	public static void insertPersona(Persona p) {
		Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        session.persist(p);

        session.getTransaction().commit();
        session.close();
        
        System.out.println("> Usuario con id: " + p.getId() + " insertado en la BBDD.");
    }
	
	public static void updatePersona(String nuevoNombre) {
		Scanner scn = new Scanner(System.in); // leak sin cerrar, pero cierra System.in tb, global no quiero porque todos son metodos estaticos
		System.out.println("> Dame id de user a modificar:");
		int id = scn.nextInt();
		
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        //		----------------------------------------------
        //				FORMA 1 (Recomendada): Dirty Checking
        //		----------------------------------------------
        // B. Persiste la persona en la Session
        // 1. Carga la persona desde la base de datos
        // 2. La mete en el 1st Level Cache (la sesión)
        // 3. Guarda una “fotografía” interna del estado original del objeto
        //			DEPRECADO TODOS LOS get!!!
        //Persona p = session.get(Persona.class, id);
        Persona p = session.find(Persona.class, id);
        if (p != null) {
            p.setNombre(nuevoNombre); // 4. no update, hibernate detecta cambio (dirty-checking)
        }

        // En el commit, hibernate:
        // 1. Compara el snapshot ↔ el objeto actual
        // 2. Detecta qué campos cambiaron
        // 3. Genera automáticamente la sentencia SQL UPDATE
        // 4. La ejecuta por ti
        session.getTransaction().commit();
        session.close();
        
        //		----------------------------------------------
        //				FORMA 2: session.merge(entity) (si tu entidad es DETACHED)
        //		----------------------------------------------
//        Persona p = new Persona();
//        p.setId(id);  
//        p.setNombre("Nombre Actualizado"); 
//
//        session.merge(p);  // Hibernate crea o actualiza según corresponda
        // ¿Cuándo usar merge()?
        //	- Cuando la entidad NO fue cargada en esta sesión
        //	- Cuando te llega desde el frontend
        //	- Cuando te llega desde un servicio externo
        //	- Cuando creas tú el objeto sin cargarlo antes
        // En Hibernate 7:
        // ✔ merge() sigue siendo el método moderno
        // ✔ Sustituye al viejo session.update() (que es API legacy)
    }
	
//	public static void selectPersona() {
//		Session session = HibernateUtil.getSessionFactory().openSession();
//        session.beginTransaction();
//        
//        Persona p = session.find(Persona.class, 1);
//	}
	
	public static List<Persona> selectAll() {
		Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();

        List<Persona> lista = session.createQuery("from Persona", Persona.class).list();

        session.getTransaction().commit();
        session.close();

        return lista;
	}

	public static void deletePersona() {
		Scanner scn = new Scanner(System.in); // leak sin cerrar!!
		System.out.println("> Dame id de user a borrar:");
		int id = scn.nextInt();
		
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        
        Persona p = session.find(Persona.class, id);
        if (p != null) {
        	session.remove(p);
        	System.out.println("> Persona con id " + id + " borrado ");
        }
        
        session.getTransaction().commit();
        session.close();
	}

	public static void main(String[] args) {
//// 		Anticuado Hibernato 5.0
////		SessionFactory factory = new Configuration().configure().buildSessionFactory();
////		Session session = factory.openSession();
//		
//		StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
//		SessionFactory factory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
//		Session session = factory.getCurrentSession();
//			
//		insertPersona(session);
//		
//		factory.close();
//		
//		System.out.println("Persona guardada");
		Persona p = new Persona("Alberto", 20, "123456789");
		insertPersona(p);
		updatePersona("Caracolo");
		Persona p2 = new Persona("Francisco", 34, "123456789");
		insertPersona(p2);
		
		List<Persona> personas = selectAll();
		System.out.println("== Listas personas ===");
		for (Persona persona: personas) {
			System.out.println(persona);
		}
		
		deletePersona();
		deletePersona();
		
		// Cerrar session factory
		HibernateUtil.getSessionFactory().close();
		System.out.println(" Fin ");
	}
}
