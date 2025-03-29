package beans;

import dao.NotaDAO;
import entities.Nota;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class NotaBean implements Serializable {
    private Nota nota = new Nota();
    private List<Nota> notas;

    @Inject
    private NotaDAO notaDAO;

    @PostConstruct
    public void init() {
        notas = notaDAO.listarTodos();
    }

    public void guardar() {
        Long id = nota.getId();
        if(nota.getId() == null) {
            id = notaDAO.crear(nota);
        } else {
            id = notaDAO.actualizar(nota);
        }
        nota = new Nota();
        notas = notaDAO.listarTodos();
    }

    public void eliminar(Long id) {
        if (id != null) {
            notaDAO.eliminar(id);
            notas = notaDAO.listarTodos();
        }
    }

    public void cargarNota(Nota nota) {
        this.nota = nota;
    }


    // Getters y Setters

    public Nota getNota() {
        return nota;
    }

    public void setNota(Nota nota) {
        this.nota = nota;
    }

    public List<Nota> getNotas() {
        return notas;
    }

    public void setNotas(List<Nota> notas) {
        this.notas = notas;
    }

    public NotaDAO getNotaDAO() {
        return notaDAO;
    }

    public void setNotaDAO(NotaDAO notaDAO) {
        this.notaDAO = notaDAO;
    }
}