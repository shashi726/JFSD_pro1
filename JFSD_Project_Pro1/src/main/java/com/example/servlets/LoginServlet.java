package com.example.servlets;

import com.example.util.DBConnection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("username");
        String password = req.getParameter("password");

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, email);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                // User authenticated
                HttpSession session = req.getSession();
                session.setAttribute("user", rs.getString("name"));  // Store user info in session
                resp.sendRedirect("home.html");  // Redirect to home page after successful login
            } else {
                // User not authenticated
                resp.sendRedirect("login.html?error=1");  // Redirect back to login page with error
            }
        } catch (SQLException e) {
            e.printStackTrace();
            resp.sendRedirect("login.html?error=1");
        }
    }
}
