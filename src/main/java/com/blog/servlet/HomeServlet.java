package com.blog.servlet;

import com.blog.dao.BlogPostDAO;
import com.blog.model.BlogPost;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the home page (/) – lists all blog posts.
 * Also supports a ?q= query parameter for searching posts.
 */
@WebServlet(urlPatterns = {"", "/"})
public class HomeServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(HomeServlet.class.getName());
    private final BlogPostDAO dao = new BlogPostDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String keyword = req.getParameter("q");
        List<BlogPost> posts;

        try {
            if (keyword != null && !keyword.trim().isEmpty()) {
                posts = dao.searchPosts(keyword.trim());
                req.setAttribute("searchKeyword", keyword.trim());
            } else {
                posts = dao.getAllPosts();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching posts", e);
            req.setAttribute("errorMessage", "Unable to load posts. Please try again later.");
            posts = java.util.Collections.emptyList();
        }

        req.setAttribute("posts", posts);
        req.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(req, resp);
    }
}
