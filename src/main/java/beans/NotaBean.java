package beans;


import dao.CategoriaDAO;
import dao.NotaDAO;
import entities.Categoria;
import entities.Nota;
import entities.Persona;
import entities.Usuario;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Named
@ViewScoped
public class NotaBean implements Serializable {
    private Nota nota = new Nota();
    private List<Nota> notas;

    @Inject
    private NotaDAO notaDAO;

    @Inject
    private CategoriaDAO categoriaDAO;

    private Long categoria;

    @PostConstruct
    public void init() {
        notas = notaDAO.listarTodos();
    }

    public void guardar() {
        Long id = nota.getId();

        if (nota.getCalificacion() != null) {
            if (nota.getCalificacion() < 1) {
                nota.setCalificacion((short) 1);
            } else if (nota.getCalificacion() > 5) {
                nota.setCalificacion((short) 5);
            }
        }
        if(nota.getId() == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            Usuario u = (Usuario) context.getExternalContext().getSessionMap().get("usuario");
            nota.setFecha(new Date());
            nota.setCategoria(categoriaDAO.buscarPorId((Long) categoria));
            nota.setUsuario(u);
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

    public Long getCategoria() {
        return categoria;
    }

    public void setCategoria(Long categoria) {
        this.categoria = categoria;
    }
}