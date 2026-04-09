package com.blog.dao;

import com.blog.model.BlogPost;
import com.blog.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for {@link BlogPost}.
 * All database operations use PreparedStatements to prevent SQL injection.
 * Resources are closed in finally blocks to prevent connection leaks.
 */
public class BlogPostDAO {

    private static final Logger LOGGER = Logger.getLogger(BlogPostDAO.class.getName());

    // SQL statements
    private static final String SQL_INSERT =
            "INSERT INTO blog_posts (title, content, author, created_at, updated_at) " +
            "VALUES (?, ?, ?, NOW(), NOW())";

    private static final String SQL_SELECT_ALL =
            "SELECT id, title, content, author, created_at, updated_at " +
            "FROM blog_posts ORDER BY created_at DESC";

    private static final String SQL_SELECT_BY_ID =
            "SELECT id, title, content, author, created_at, updated_at " +
            "FROM blog_posts WHERE id = ?";

    private static final String SQL_UPDATE =
            "UPDATE blog_posts SET title = ?, content = ?, author = ?, updated_at = NOW() " +
            "WHERE id = ?";

    private static final String SQL_DELETE =
            "DELETE FROM blog_posts WHERE id = ?";

    private static final String SQL_SEARCH =
            "SELECT id, title, content, author, created_at, updated_at " +
            "FROM blog_posts " +
            "WHERE title LIKE ? OR content LIKE ? OR author LIKE ? " +
            "ORDER BY created_at DESC";

    // -------------------------------------------------------------------------
    // Create
    // -------------------------------------------------------------------------

    /**
     * Persists a new blog post and returns the generated ID.
     *
     * @param post the post to create (id field is ignored)
     * @return the auto-generated primary key, or -1 on failure
     * @throws SQLException if a database error occurs
     */
    public int createPost(BlogPost post) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getContent());
            ps.setString(3, post.getAuthor());
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return -1;
        } finally {
            closeResources(rs, ps, conn);
        }
    }

    // -------------------------------------------------------------------------
    // Read
    // -------------------------------------------------------------------------

    /**
     * Returns all blog posts, newest first.
     *
     * @return list of all posts (never null, may be empty)
     * @throws SQLException if a database error occurs
     */
    public List<BlogPost> getAllPosts() throws SQLException {
        List<BlogPost> posts = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(SQL_SELECT_ALL);
            rs = ps.executeQuery();
            while (rs.next()) {
                posts.add(mapRow(rs));
            }
        } finally {
            closeResources(rs, ps, conn);
        }
        return posts;
    }

    /**
     * Finds a single post by its primary key.
     *
     * @param id the post ID
     * @return the matching {@link BlogPost}, or {@code null} if not found
     * @throws SQLException if a database error occurs
     */
    public BlogPost getPostById(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(SQL_SELECT_BY_ID);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
            return null;
        } finally {
            closeResources(rs, ps, conn);
        }
    }

    /**
     * Searches posts by keyword (matches title, content, or author).
     *
     * @param keyword the search term
     * @return list of matching posts (never null, may be empty)
     * @throws SQLException if a database error occurs
     */
    public List<BlogPost> searchPosts(String keyword) throws SQLException {
        List<BlogPost> posts = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(SQL_SEARCH);
            String like = "%" + keyword + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            ps.setString(3, like);
            rs = ps.executeQuery();
            while (rs.next()) {
                posts.add(mapRow(rs));
            }
        } finally {
            closeResources(rs, ps, conn);
        }
        return posts;
    }

    // -------------------------------------------------------------------------
    // Update
    // -------------------------------------------------------------------------

    /**
     * Updates an existing blog post.
     *
     * @param post the post with updated values (id must be set)
     * @return {@code true} if the update affected at least one row
     * @throws SQLException if a database error occurs
     */
    public boolean updatePost(BlogPost post) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(SQL_UPDATE);
            ps.setString(1, post.getTitle());
            ps.setString(2, post.getContent());
            ps.setString(3, post.getAuthor());
            ps.setInt(4, post.getId());
            return ps.executeUpdate() > 0;
        } finally {
            closeResources(null, ps, conn);
        }
    }

    // -------------------------------------------------------------------------
    // Delete
    // -------------------------------------------------------------------------

    /**
     * Deletes a blog post by ID.
     *
     * @param id the post ID to delete
     * @return {@code true} if the deletion affected at least one row
     * @throws SQLException if a database error occurs
     */
    public boolean deletePost(int id) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBConnection.getConnection();
            ps = conn.prepareStatement(SQL_DELETE);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } finally {
            closeResources(null, ps, conn);
        }
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    /**
     * Maps the current ResultSet row to a {@link BlogPost} object.
     */
    private BlogPost mapRow(ResultSet rs) throws SQLException {
        BlogPost post = new BlogPost();
        post.setId(rs.getInt("id"));
        post.setTitle(rs.getString("title"));
        post.setContent(rs.getString("content"));
        post.setAuthor(rs.getString("author"));
        post.setCreatedAt(rs.getTimestamp("created_at"));
        post.setUpdatedAt(rs.getTimestamp("updated_at"));
        return post;
    }

    /**
     * Closes JDBC resources silently, logging any errors.
     */
    private void closeResources(ResultSet rs, Statement stmt, Connection conn) {
        if (rs != null) {
            try { rs.close(); } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Failed to close ResultSet", e);
            }
        }
        if (stmt != null) {
            try { stmt.close(); } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Failed to close Statement", e);
            }
        }
        if (conn != null) {
            try { conn.close(); } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Failed to close Connection", e);
            }
        }
    }
}
