package dao;

import entities.Categoria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

public class CategoriaDAO implements Serializable {
    private static final Logger LOG = Logger.getLogger(CategoriaDAO.class.getName());
    private EntityManagerFactory emf;
    private EntityManager em;

    public CategoriaDAO() {
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

    public Long crear(Categoria categoria) {
        categoria.setEstado(true);
        ejecutarTransaccion(() -> em.persist(categoria));
        return categoria.getId();
    }

    public Categoria buscarPorId(Long id) {
        return em.find(Categoria.class, id);
    }

    public List<Categoria> listarTodos() {
        return em.createQuery("SELECT p FROM Categoria p", Categoria.class).getResultList();
    }

    public Long actualizar(Categoria categoria) {
        ejecutarTransaccion(() -> em.merge(categoria));
        return categoria.getId();
    }

    public void eliminar(Long id) {
        LOG.info("Intentando eliminar categoria con ID: " + id);
        if (id == null) {
            throw new IllegalArgumentException("ID no puede ser nulo");
        }
        Categoria categoria = em.find(Categoria.class, id);
        if (categoria != null) {
            ejecutarTransaccion(() -> em.remove(categoria));
        } else {
            LOG.warning("Categoria no encontrada con ID: " + id);
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
