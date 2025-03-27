package dao;

import entities.Producto;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

public class ProductoDAO implements Serializable {
    private static final Logger LOG = Logger.getLogger(ProductoDAO.class.getName());
    private EntityManagerFactory emf;
    private EntityManager em;

    public ProductoDAO() {
        // Se crea manualmente el EntityManagerFactory usando la unidad de persistencia configurada
        this.emf = Persistence.createEntityManagerFactory("miUnidadPersistencia");
        this.em = emf.createEntityManager();
    }

    // Método para ejecutar tareas dentro de una transacción
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

    public void crear(Producto producto) {
        ejecutarTransaccion(() -> em.persist(producto));
    }

    public Producto buscarPorId(Long id) {
        return em.find(Producto.class, id);
    }

    public List<Producto> listarTodos() {
        return em.createQuery("SELECT p FROM Producto p", Producto.class).getResultList();
    }

    public void actualizar(Producto producto) {
        ejecutarTransaccion(() -> em.merge(producto));
    }

    public void eliminar(Long id) {
        LOG.info("Intentando eliminar producto con ID: " + id);
        if (id == null) {
            throw new IllegalArgumentException("ID no puede ser nulo");
        }
        Producto producto = em.find(Producto.class, id);
        if (producto != null) {
            ejecutarTransaccion(() -> em.remove(producto));
        } else {
            LOG.warning("Producto no encontrado con ID: " + id);
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
