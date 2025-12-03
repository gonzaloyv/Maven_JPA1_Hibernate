package dwes.bidireccional.one_to_many;

import org.hibernate.Session;

import dwes.HibernateUtil;

public class Prueba {
	public static void BiOneToMany() {
		Departamento d = new Departamento("Departamento Informática");

		Empleado e1 = new Empleado("Juan");
		Empleado e2 = new Empleado("Ana");
		Empleado e3 = new Empleado("Carlos");

		// Asociación bidireccional (gracias al helper no hay inconsistencias)
		d.addEmpleado(e1);
		d.addEmpleado(e2);
		d.addEmpleado(e3);

		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();

		// Solo necesitamos persistir el departamento, por cascade:
		session.persist(d);

		session.getTransaction().commit();
		session.close();

		System.out.println("Departamento y empleados guardados correctamente.");
	}
}
