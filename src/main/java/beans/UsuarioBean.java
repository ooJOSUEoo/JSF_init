package beans;

import dao.UsuarioDAO;
import entities.Usuario;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class UsuarioBean implements Serializable {
    private Usuario usuario = new Usuario();
    private List<Usuario> usuarios;

    @Inject
    private UsuarioDAO usuarioDAO;

    @PostConstruct
    public void init() {
        usuarios = usuarioDAO.listarTodos();
    }

    public void guardar() {
        Long id = usuario.getId();
        if(usuario.getId() == null) {
            id = usuarioDAO.crear(usuario);
        } else {
            id = usuarioDAO.actualizar(usuario);
        }
        usuario = new Usuario();
        usuarios = usuarioDAO.listarTodos();
    }

    public void eliminar(Long id) {
        if (id != null) {
            usuarioDAO.eliminar(id);
            usuarios = usuarioDAO.listarTodos();
        }
    }

    public void cargarUsuario(Usuario usuario) {
        this.usuario = usuario;
    }


    // Getters y Setters

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public UsuarioDAO getUsuarioDAO() {
        return usuarioDAO;
    }

    public void setUsuarioDAO(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }
}