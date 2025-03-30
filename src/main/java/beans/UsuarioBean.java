package beans;

import dao.PersonaDAO;
import dao.UsuarioDAO;
import entities.Persona;
import entities.Usuario;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class UsuarioBean implements Serializable {
    private Usuario usuario = new Usuario();
    private Persona persona = new Persona();
    private List<Usuario> usuarios;
    private List<Persona> personas;
    private Usuario sesion;

    @Inject
    private UsuarioDAO usuarioDAO;

    @Inject
    private PersonaDAO personaDAO;

    @PostConstruct
    public void init() {
        usuarios = usuarioDAO.listarTodos();
        personas = personaDAO.listarTodos();
    }

    public void guardar() {
        Long idu = usuario.getId();
        Long idp = persona.getId();
        if(usuario.getId() == null && persona.getId() == null) {
            idu = usuarioDAO.crear(usuario);
            idp = personaDAO.crear(persona);
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage
                            (FacesMessage.SEVERITY_INFO,
                            "Aviso",
                                    "Se ha guardado el usuario y persona con ID: " + idu
                            )
            );
        } else {
            idu = usuarioDAO.actualizar(usuario);
            idp = personaDAO.actualizar(persona);
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage
                            (FacesMessage.SEVERITY_INFO,
                                    "Aviso",
                                    "Se ha actualizado el usuario y persona con ID: " + idu
                            )
            );
        }
        usuario = new Usuario();
        persona = new Persona();
        usuarios = usuarioDAO.listarTodos();
        personas = personaDAO.listarTodos();
    }

    public void eliminar(Long id) {
        if (id != null) {
            usuarioDAO.eliminar(id);
            usuarios = usuarioDAO.listarTodos();
            personaDAO.eliminar(id);
            personas = personaDAO.listarTodos();
        }
    }

    public void cargarUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public void cargarPersona(Persona persona) {
        this.persona = persona;
    }

    public void iniciarSesion() throws IOException {
        Usuario usuario = usuarioDAO.iniciarSesion(this.usuario);
        if (usuario != null) {
            System.out.println("Usuario encontrado: " + usuario);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", usuario);
            FacesContext.getCurrentInstance().getExternalContext().redirect("/demo-1.0-SNAPSHOT/logged/index.xhtml");
            return;
        }
        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage
                        (FacesMessage.SEVERITY_INFO,
                                "Aviso",
                                "Credenciales incorrectas"
                        )
        );
    }

    public  void verificarSesion(Boolean logged) {
        try{
            FacesContext context = FacesContext.getCurrentInstance();
            Usuario u = (Usuario) context.getExternalContext().getSessionMap().get("usuario");
            System.out.println("Usuario: " + u);
            if (u == null) {
                if (logged){
                    context.getExternalContext().redirect("/demo-1.0-SNAPSHOT/index.xhtml");
                }
            }else{
                if (!logged){
                    context.getExternalContext().redirect("/demo-1.0-SNAPSHOT/logged/index.xhtml");
                }
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void cerrarSesion() {
        try {
        FacesContext context = FacesContext.getCurrentInstance();
            context.getExternalContext().invalidateSession();
            context.getExternalContext().redirect("/demo-1.0-SNAPSHOT/index.xhtml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Getters y Setters

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public List<Persona> getPersonas() {
        return personas;
    }

    public void setPersonas(List<Persona> personas) {
        this.personas = personas;
    }

    public UsuarioDAO getUsuarioDAO() {
        return usuarioDAO;
    }

    public void setUsuarioDAO(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public PersonaDAO getPersonaDAO() {
        return personaDAO;
    }

    public void setPersonaDAO(PersonaDAO personaDAO) {
        this.personaDAO = personaDAO;
    }

    public Usuario getSesion() {
        try {
            return (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
        } catch (Exception e) {
            return null;
        }
    }

    public void setSesion(Usuario sesion) {
        this.sesion = sesion;
    }
}