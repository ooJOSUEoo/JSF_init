package entities;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class Producto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String nombre;

    @Column
    private Double precio;

    @Column
    private byte[] imagen;

    @Lob
    @Column(nullable = true, columnDefinition = "TEXT")
    private String imagenb64;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public String getImagenb64() {
        return imagenb64;
    }

    public void setImagenb64(String imagenb64) {
        this.imagenb64 = imagenb64;
    }
}