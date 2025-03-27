import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateTest {
    public static void main(String[] args) {
        try {
            SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
            Session session = sessionFactory.openSession();
            System.out.println("Hibernate está funcionando correctamente.");
            session.close();
        } catch (Throwable ex) {
            System.err.println("Fallo al inicializar Hibernate: " + ex);
        }
    }
}
