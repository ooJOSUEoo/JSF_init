package beans;

import dao.ProductoDAO;
import entities.Producto;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.file.UploadedFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Base64;
import java.util.List;

@Named
@ViewScoped
public class ProductoBean implements Serializable {
    private Producto producto = new Producto();
    private UploadedFile imagen;
    private List<Producto> productos;

    @Inject
    private ProductoDAO productoDAO;

    @PostConstruct
    public void init() {
        List<Producto> p = productoDAO.listarTodos();
        for (Producto producto : p) {
            if (producto.getImagen() != null) {
                producto.setImagenb64("data:image/png;base64," + Base64.getEncoder().encodeToString(producto.getImagen()));
            }
        }
        productos = p;
    }

    public void guardar() {
        Long id = producto.getId();
        producto.setImagenb64(null);
        if(producto.getId() == null) {
            id = productoDAO.crear(producto);
        } else {
            id = productoDAO.actualizar(producto);
        }
        subirImagen(id);
        producto = new Producto();
        List<Producto> p = productoDAO.listarTodos();
        for (Producto producto : p) {
            if (producto.getImagen() != null) {
                producto.setImagenb64("data:image/png;base64," + Base64.getEncoder().encodeToString(producto.getImagen()));
            }
        }
        productos = p;
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

    public void subirImagen(Long id) {
        if (id == null || imagen == null) {
            return;
        }
        try {
            byte[] content = imagen.getContent();
            producto.setImagen(content);
            productoDAO.subirImagen(id, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // Getters y Setters

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public UploadedFile getImagen() {
        return imagen;
    }

    public void setImagen(UploadedFile imagen) {
        this.imagen = imagen;
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