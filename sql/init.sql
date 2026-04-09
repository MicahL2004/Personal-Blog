-- =============================================================
-- Personal Blog – Database Initialisation Script
-- Compatible with MySQL 5.7+ and Azure Database for MySQL
-- =============================================================

-- Create and select the database
CREATE DATABASE IF NOT EXISTS blog_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE blog_db;

-- Drop the table if it already exists (for a clean re-run)
DROP TABLE IF EXISTS blog_posts;

-- Create the main blog_posts table
CREATE TABLE blog_posts (
    id         INT          NOT NULL AUTO_INCREMENT,
    title      VARCHAR(255) NOT NULL,
    content    TEXT         NOT NULL,
    author     VARCHAR(100) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
                                     ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id)
) ENGINE=InnoDB
  DEFAULT CHARSET=utf8mb4
  COLLATE=utf8mb4_unicode_ci;

-- Full-text index for the search feature
CREATE FULLTEXT INDEX idx_posts_search ON blog_posts (title, content, author);

-- =============================================================
-- Sample data – remove before going to production
-- =============================================================

INSERT INTO blog_posts (title, content, author) VALUES
(
    'Welcome to My Personal Blog!',
    'Hello and welcome to my personal blog! This is a place where I will share my thoughts, experiences, and ideas.\n\nThis blog was built using Java Servlets, JSP, JDBC and MySQL – a classic and solid stack for learning full-stack Java web development.\n\nStay tuned for more posts coming soon!',
    'Blog Author'
),
(
    'Getting Started with Java Servlets',
    'Java Servlets are server-side Java programs that handle HTTP requests and responses. They are the backbone of many enterprise Java web applications.\n\nKey concepts:\n- HttpServlet base class\n- doGet() and doPost() methods\n- Request/Response objects\n- RequestDispatcher for forwarding to JSP views\n\nServlets are deployed inside a servlet container such as Apache Tomcat, which manages their lifecycle (init, service, destroy).',
    'Blog Author'
),
(
    'Why JDBC Still Matters',
    'Despite the popularity of ORM frameworks like Hibernate and JPA, understanding JDBC (Java Database Connectivity) is a fundamental skill for any Java developer.\n\nJDBC gives you full control over your SQL queries, which means:\n- Better performance tuning\n- No "magic" abstractions\n- Deeper understanding of relational databases\n\nAlways use PreparedStatements to prevent SQL injection, and close your resources in finally blocks (or use try-with-resources).',
    'Blog Author'
);
