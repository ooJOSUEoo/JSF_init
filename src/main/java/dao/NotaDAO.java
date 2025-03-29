package dao;

import entities.Nota;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

public class NotaDAO implements Serializable {
    private static final Logger LOG = Logger.getLogger(NotaDAO.class.getName());
    private EntityManagerFactory emf;
    private EntityManager em;

    public NotaDAO() {
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

    public Long crear(Nota nota) {
        ejecutarTransaccion(() -> em.persist(nota));
        return nota.getId();
    }

    public Nota buscarPorId(Long id) {
        return em.find(Nota.class, id);
    }

    public List<Nota> listarTodos() {
        return em.createQuery("SELECT p FROM Nota p", Nota.class).getResultList();
    }

    public Long actualizar(Nota nota) {
        ejecutarTransaccion(() -> em.merge(nota));
        return nota.getId();
    }

    public void eliminar(Long id) {
        LOG.info("Intentando eliminar nota con ID: " + id);
        if (id == null) {
            throw new IllegalArgumentException("ID no puede ser nulo");
        }
        Nota nota = em.find(Nota.class, id);
        if (nota != null) {
            ejecutarTransaccion(() -> em.remove(nota));
        } else {
            LOG.warning("Nota no encontrada con ID: " + id);
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
