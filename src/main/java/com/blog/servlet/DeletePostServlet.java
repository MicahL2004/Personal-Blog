package com.blog.servlet;

import com.blog.dao.BlogPostDAO;
import com.blog.util.DatabaseErrorUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles /delete – deletes a blog post and redirects to the home page.
 *
 * Accepts both GET (from a confirmation link) and POST (from a form button)
 * so that it works regardless of how the browser submits the request.
 */
@WebServlet("/delete")
public class DeletePostServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(DeletePostServlet.class.getName());
    private final BlogPostDAO dao = new BlogPostDAO();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        handleDelete(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        handleDelete(req, resp);
    }

    private void handleDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idParam = req.getParameter("id");

        if (idParam == null || idParam.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idParam.trim());
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        HttpSession session = req.getSession();
        try {
            boolean deleted = dao.deletePost(id);
            if (deleted) {
                session.setAttribute("flashMessage", "Post deleted successfully.");
                session.setAttribute("flashType",    "success");
            } else {
                session.setAttribute("flashMessage", "Post not found or already deleted.");
                session.setAttribute("flashType",    "warning");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error deleting post id=" + id, e);
            session.setAttribute("flashMessage", DatabaseErrorUtil.toUserMessage(e));
            session.setAttribute("flashType",    "error");
        }

        resp.sendRedirect(req.getContextPath() + "/");
    }
}
