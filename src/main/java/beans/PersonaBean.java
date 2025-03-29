package beans;

import dao.PersonaDAO;
import entities.Persona;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class PersonaBean implements Serializable {
    private Persona persona = new Persona();
    private List<Persona> personas;

    @Inject
    private PersonaDAO personaDAO;

    @PostConstruct
    public void init() {
        personas = personaDAO.listarTodos();
    }

    public void guardar() {
        Long id = persona.getId();
        if(persona.getId() == null) {
            id = personaDAO.crear(persona);
        } else {
            id = personaDAO.actualizar(persona);
        }
        persona = new Persona();
        personas = personaDAO.listarTodos();
    }

    public void eliminar(Long id) {
        if (id != null) {
            personaDAO.eliminar(id);
            personas = personaDAO.listarTodos();
        }
    }

    public void cargarPersona(Persona persona) {
        this.persona = persona;
    }


    // Getters y Setters

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public List<Persona> getPersonas() {
        return personas;
    }

    public void setPersonas(List<Persona> personas) {
        this.personas = personas;
    }

    public PersonaDAO getPersonaDAO() {
        return personaDAO;
    }

    public void setPersonaDAO(PersonaDAO personaDAO) {
        this.personaDAO = personaDAO;
    }
}