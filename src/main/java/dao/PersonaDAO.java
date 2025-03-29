package dao;

import entities.Persona;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

public class PersonaDAO implements Serializable {
    private static final Logger LOG = Logger.getLogger(PersonaDAO.class.getName());
    private EntityManagerFactory emf;
    private EntityManager em;

    public PersonaDAO() {
        this.emf = Persistence.createEntityManagerFactory("miUnidadPersistencia");
        this.em = emf.createEntityManager();
    }

    private void ejecutarTransaccion(Runnable tarea) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            tarea.run();
            transaction.commit();
        } catch (RuntimeException e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public Long crear(Persona persona) {
        ejecutarTransaccion(() -> em.persist(persona));
        return persona.getId();
    }

    public Persona buscarPorId(Long id) {
        return em.find(Persona.class, id);
    }

    public List<Persona> listarTodos() {
        return em.createQuery("SELECT p FROM Persona p", Persona.class).getResultList();
    }

    public Long actualizar(Persona persona) {
        ejecutarTransaccion(() -> em.merge(persona));
        return persona.getId();
    }

    public void eliminar(Long id) {
        LOG.info("Intentando eliminar persona con ID: " + id);
        if (id == null) {
            throw new IllegalArgumentException("ID no puede ser nulo");
        }
        Persona persona = em.find(Persona.class, id);
        if (persona != null) {
            ejecutarTransaccion(() -> em.remove(persona));
        } else {
            LOG.warning("Persona no encontrada con ID: " + id);
        }
    }

    // Método para cerrar recursos cuando ya no se necesiten
    public void cerrar() {
        if (em != null) {
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
    }
}
