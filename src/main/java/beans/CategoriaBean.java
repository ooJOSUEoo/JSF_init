package beans;

import com.google.gson.Gson;
import dao.CategoriaDAO;
import dao.ProductoDAO;
import entities.Categoria;
import entities.Producto;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.model.file.UploadedFile;

import java.awt.*;
import java.io.Serializable;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class CategoriaBean implements Serializable {
    private Categoria categoria = new Categoria();
    private List<Categoria> categorias;

    @Inject
    private CategoriaDAO categoriaDAO;

    @PostConstruct
    public void init() {
        categorias = categoriaDAO.listarTodos();
    }

    public void guardar() {
        Long id = categoria.getId();
        if(categoria.getId() == null) {
            id = categoriaDAO.crear(categoria);
        } else {
            id = categoriaDAO.actualizar(categoria);
        }
        categoria = new Categoria();
        categorias = categoriaDAO.listarTodos();
    }

    public void eliminar(Long id) {
        if (id != null) {
            categoriaDAO.eliminar(id);
            categorias = categoriaDAO.listarTodos();
        }
    }

    public void cargarCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String obtenerNombre(Long id) {
        return categoriaDAO.buscarPorId(id).getNombre();
    }


    // Getters y Setters

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public CategoriaDAO getCategoriaDAO() {
        return categoriaDAO;
    }

    public void setCategoriaDAO(CategoriaDAO categoriaDAO) {
        this.categoriaDAO = categoriaDAO;
    }
}