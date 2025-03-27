package servlets;

import beans.ProductoBean;
import dao.ProductoDAO;
import entities.Producto;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/ProductoServlet")
public class ProductoServlet extends HttpServlet {
    private ProductoDAO productoDAO = new ProductoDAO();
    private ProductoBean productoBean = new ProductoBean();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String action = request.getParameter("action");

            if(action.equals("guardar")) {
                guardarProducto(request);
            }
            response.sendRedirect("/");
        } catch(Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    private void guardarProducto(HttpServletRequest request) {
        Producto p = new Producto();
        p.setNombre(request.getParameter("nombre"));
        p.setPrecio(Double.parseDouble(request.getParameter("precio")));

        if(request.getParameter("id") != null && !request.getParameter("id").isEmpty()) {
            p.setId(Long.parseLong(request.getParameter("id")));
            productoDAO.actualizar(p);
        } else {
            productoDAO.crear(p);
        }
    }
}