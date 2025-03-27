package beans;

import dao.ProductoDAO;
import entities.Producto;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class ProductoBean implements Serializable {
    private Producto producto = new Producto();
    private List<Producto> productos;

    @Inject
    private ProductoDAO productoDAO;

    @PostConstruct
    public void init() {
        productos = productoDAO.listarTodos();
    }

    public void guardar() {
        if(producto.getId() == null) {
            productoDAO.crear(producto);
        } else {
            productoDAO.actualizar(producto);
        }
        producto = new Producto();
        productos = productoDAO.listarTodos();
    }

    public void eliminar(Long id) {  // <-- Añade parámetro
        if (id != null) {
            productoDAO.eliminar(id);
            productos = productoDAO.listarTodos();
        }
    }

    public void cargarProducto(Producto producto) {
        this.producto = producto;
    }

    // Getters y Setters

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    public ProductoDAO getProductoDAO() {
        return productoDAO;
    }

    public void setProductoDAO(ProductoDAO productoDAO) {
        this.productoDAO = productoDAO;
    }
}