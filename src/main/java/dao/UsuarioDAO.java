package dao;

import entities.Usuario;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

public class UsuarioDAO implements Serializable {
    private static final Logger LOG = Logger.getLogger(UsuarioDAO.class.getName());
    private EntityManagerFactory emf;
    private EntityManager em;

    public UsuarioDAO() {
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

    public Long crear(Usuario usuario) {
        ejecutarTransaccion(() -> em.persist(usuario));
        return usuario.getId();
    }

    public Usuario buscarPorId(Long id) {
        return em.find(Usuario.class, id);
    }

    public List<Usuario> listarTodos() {
        return em.createQuery("SELECT p FROM Usuario p", Usuario.class).getResultList();
    }

    public Long actualizar(Usuario usuario) {
        ejecutarTransaccion(() -> em.merge(usuario));
        return usuario.getId();
    }

    public void eliminar(Long id) {
        LOG.info("Intentando eliminar usuario con ID: " + id);
        if (id == null) {
            throw new IllegalArgumentException("ID no puede ser nulo");
        }
        Usuario usuario = em.find(Usuario.class, id);
        if (usuario != null) {
            ejecutarTransaccion(() -> em.remove(usuario));
        } else {
            LOG.warning("Usuario no encontrada con ID: " + id);
        }
    }

    public Usuario iniciarSesion(Usuario u) {
        Usuario usuario = null;
        try {
            Query query = em.createQuery("SELECT p FROM Usuario p WHERE p.usuario = :usuario AND p.password = :password", Usuario.class);
            query.setParameter("usuario", u.getUsuario());
            query.setParameter("password", u.getPassword());
            List<Usuario> usuarios = query.getResultList();

            if (!usuarios.isEmpty()) {
                usuario = usuarios.get(0);
            }

            return usuario;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
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
