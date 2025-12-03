package dwes.unidireccional.one_to_many;

import org.hibernate.Session;

import dwes.HibernateUtil;

public class Prueba {
	public static void UniOneToMany() {
		// Crear Departamento
        Departamento depto = new Departamento();

        // Crear empleados
        Empleado e1 = new Empleado("Juan");
        Empleado e2 = new Empleado("Ana");
        Empleado e3 = new Empleado("Carlos");

        // Asignarlos al departamento
        depto.addEmpleado(e1);
        depto.addEmpleado(e2);
        depto.addEmpleado(e3);

        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        
        // Persistimos el Departamento y gracias a @JoinColumn se asigna la FK
        // en los Empleados. PERO OJO: no hay cascade => debemos persistir empleados también.
        session.persist(e1);
        session.persist(e2);
        session.persist(e3);

        session.persist(depto);

        session.getTransaction().commit();
        session.close();

        System.out.println("Departamento y empleados creados correctamente");
	}
}
