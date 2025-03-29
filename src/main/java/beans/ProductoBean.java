package beans;

import com.google.gson.Gson;
import dao.ProductoDAO;
import entities.Producto;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.convert.BigDecimalConverter;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;
import org.primefaces.model.file.UploadedFile;

import java.awt.*;
import java.io.Serializable;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Named
@ViewScoped
public class ProductoBean implements Serializable {
    private Producto producto = new Producto();
    private UploadedFile imagen;
    private List<Producto> productos;
    private String chartModel;

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
        generarGrafico();
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
        generarGrafico();
    }

    public void eliminar(Long id) {  // <-- Añade parámetro
        if (id != null) {
            productoDAO.eliminar(id);
            productos = productoDAO.listarTodos();
            generarGrafico();
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

    public void generarGrafico() {
        String type = "pie";
        Map<String, Boolean> options = Map.of(
                "responsive", true,
                "maintainAspectRatio", false
        );
        Map<String, Object> data = Map.of(
                "labels", productos.stream().map(Producto::getNombre).collect(Collectors.toList()),
                "datasets", List.of(Map.of(
                        "data", productos.stream().map(Producto::getPrecio).collect(Collectors.toList()),
                        "backgroundColor", productos.stream().map(p -> {
                            Color c = new Color((int) (Math.random() * 255),
                                    (int) (Math.random() * 255),
                                    (int) (Math.random() * 255));
                            // Convertir el Color a formato CSS rgba
                            return "rgba(" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + ",0.2)";
                        }).collect(Collectors.toList()),
                        "borderColor", productos.stream().map(p -> {
                            Color c = new Color((int) (Math.random() * 255),
                                    (int) (Math.random() * 255),
                                    (int) (Math.random() * 255));
                            return "rgba(" + c.getRed() + "," + c.getGreen() + "," + c.getBlue() + ",1)";
                        }).collect(Collectors.toList()),
                        "borderWidth", 1
                ))
        );

        Map<String, Object> chartConfig = Map.of(
                "type", type,
                "data", data,
                "options", options
        );

        Gson gson = new Gson();
        chartModel = gson.toJson(chartConfig);
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

    public String getChartModel() {
        return chartModel;
    }

    public void setChartModel(String pieChartModel) {
        this.chartModel = pieChartModel;
    }

    public ProductoDAO getProductoDAO() {
        return productoDAO;
    }

    public void setProductoDAO(ProductoDAO productoDAO) {
        this.productoDAO = productoDAO;
    }
}