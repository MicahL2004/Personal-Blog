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
 * Handles /create – shows the create form (GET) and processes the submission (POST).
 */
@WebServlet("/create")
public class CreatePostServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(CreatePostServlet.class.getName());
    private final BlogPostDAO dao = new BlogPostDAO();

    /** Show the blank create form. */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/create.jsp").forward(req, resp);
    }

    /** Process the submitted form. */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String title   = req.getParameter("title");
        String content = req.getParameter("content");
        String author  = req.getParameter("author");

        // Server-side validation
        if (isEmpty(title) || isEmpty(content) || isEmpty(author)) {
            req.setAttribute("errorMessage", "All fields are required.");
            req.setAttribute("title",   title);
            req.setAttribute("content", content);
            req.setAttribute("author",  author);
            req.getRequestDispatcher("/WEB-INF/views/create.jsp").forward(req, resp);
            return;
        }

        if (title.length() > 255) {
            req.setAttribute("errorMessage", "Title must be 255 characters or fewer.");
            req.setAttribute("title",   title);
            req.setAttribute("content", content);
            req.setAttribute("author",  author);
            req.getRequestDispatcher("/WEB-INF/views/create.jsp").forward(req, resp);
            return;
        }

        try {
            BlogPost post = new BlogPost(title.trim(), content.trim(), author.trim());
            int newId = dao.createPost(post);
            if (newId > 0) {
                // Flash success message via session
                HttpSession session = req.getSession();
                session.setAttribute("flashMessage", "Post created successfully!");
                session.setAttribute("flashType",    "success");
                resp.sendRedirect(req.getContextPath() + "/post?id=" + newId);
            } else {
                req.setAttribute("errorMessage", "Failed to create post. Please try again.");
                req.setAttribute("title",   title);
                req.setAttribute("content", content);
                req.setAttribute("author",  author);
                req.getRequestDispatcher("/WEB-INF/views/create.jsp").forward(req, resp);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error creating post", e);
            req.setAttribute("errorMessage", DatabaseErrorUtil.toUserMessage(e));
            req.setAttribute("title",   title);
            req.setAttribute("content", content);
            req.setAttribute("author",  author);
            req.getRequestDispatcher("/WEB-INF/views/create.jsp").forward(req, resp);
        }
    }

    private boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
