package com.blog.servlet;

import com.blog.dao.BlogPostDAO;
import com.blog.model.BlogPost;
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
 * Handles /edit?id=... – shows the pre-filled edit form (GET) and saves changes (POST).
 */
@WebServlet("/edit")
public class EditPostServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(EditPostServlet.class.getName());
    private final BlogPostDAO dao = new BlogPostDAO();

    /** Load the existing post and show the edit form. */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        int id = parseId(req, resp);
        if (id < 0) return; // already redirected

        try {
            BlogPost post = dao.getPostById(id);
            if (post == null) {
                req.setAttribute("errorMessage", "Post not found.");
                req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req, resp);
                return;
            }
            req.setAttribute("post", post);
            req.getRequestDispatcher("/WEB-INF/views/edit.jsp").forward(req, resp);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error loading post id=" + id + " for edit", e);
            req.setAttribute("errorMessage", DatabaseErrorUtil.toUserMessage(e));
            req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req, resp);
        }
    }

    /** Validate and persist the updated post. */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        int id = parseId(req, resp);
        if (id < 0) return; // already redirected

        String title   = req.getParameter("title");
        String content = req.getParameter("content");
        String author  = req.getParameter("author");

        // Server-side validation
        if (isEmpty(title) || isEmpty(content) || isEmpty(author)) {
            refillForm(req, id, title, content, author, "All fields are required.");
            req.getRequestDispatcher("/WEB-INF/views/edit.jsp").forward(req, resp);
            return;
        }

        if (title.length() > 255) {
            refillForm(req, id, title, content, author, "Title must be 255 characters or fewer.");
            req.getRequestDispatcher("/WEB-INF/views/edit.jsp").forward(req, resp);
            return;
        }

        try {
            BlogPost post = new BlogPost();
            post.setId(id);
            post.setTitle(title.trim());
            post.setContent(content.trim());
            post.setAuthor(author.trim());

            boolean updated = dao.updatePost(post);
            if (updated) {
                HttpSession session = req.getSession();
                session.setAttribute("flashMessage", "Post updated successfully!");
                session.setAttribute("flashType",    "success");
                resp.sendRedirect(req.getContextPath() + "/post?id=" + id);
            } else {
                req.setAttribute("errorMessage", "Failed to update post. Please try again.");
                refillForm(req, id, title, content, author, "Failed to update post.");
                req.getRequestDispatcher("/WEB-INF/views/edit.jsp").forward(req, resp);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error updating post id=" + id, e);
            refillForm(req, id, title, content, author,
                    DatabaseErrorUtil.toUserMessage(e));
            req.getRequestDispatcher("/WEB-INF/views/edit.jsp").forward(req, resp);
        }
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private int parseId(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.trim().isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/");
            return -1;
        }
        try {
            return Integer.parseInt(idParam.trim());
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/");
            return -1;
        }
    }

    private void refillForm(HttpServletRequest req, int id, String title,
                             String content, String author, String error) {
        BlogPost post = new BlogPost();
        post.setId(id);
        post.setTitle(title);
        post.setContent(content);
        post.setAuthor(author);
        req.setAttribute("post",         post);
        req.setAttribute("errorMessage", error);
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
