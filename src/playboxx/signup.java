package playboxx;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Define the servlet mapping
@WebServlet("/signup")
public class signup extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<h2>This endpoint is for POST requests. Please use POST to submit data.</h2>");
    }

    // Handles POST requests from the signup form
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Set response content type
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Retrieve form data
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");
        String password = request.getParameter("password");

        // Check if form data is null
        if (username == null || email == null || mobile == null || password == null) {
            out.println("<h2>Error: Missing form data.</h2>");
            return;
        }

        // Insert data into the database
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            // Establish database connection
            conn = Database.connect();
            
            if (conn == null) {
                out.println("<h2>Database connection failed.</h2>");
                return;
            }

            // SQL query to insert user details
            String sql = "INSERT INTO users(username, email, mobile, password) VALUES (?, ?, ?, ?)";

            stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, mobile);
            stmt.setString(4, password);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                out.println("<h2>Sign up successful!</h2>");
                response.sendRedirect("index.html");
            } else {
                out.println("<h2>Failed to sign up. Please try again.</h2>");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            out.println("<h2>Database error: " + e.getMessage() + "</h2>");
        } finally {
            // Clean up resources
            try {
                if (stmt != null) stmt.close();
                Database.disconnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
