# Personal Blog Web Application

A full-stack **Personal Blog** built with **Java Servlets, JSP, JDBC, and MySQL**.  
Supports full CRUD operations (Create, Read, Update, Delete) for blog posts with a clean, responsive UI.

---

## Table of Contents

1. [Features](#features)
2. [Tech Stack](#tech-stack)
3. [Project Structure](#project-structure)
4. [Prerequisites](#prerequisites)
5. [Local Setup](#local-setup)
6. [Running Locally](#running-locally)
7. [Azure Deployment](#azure-deployment)
8. [Environment Variables](#environment-variables)
9. [Database Schema](#database-schema)
10. [Screenshots](#screenshots)

---

## Features

- **View all posts** – home page lists every post with title, author, date and a preview
- **View a single post** – full content displayed on its own page
- **Create a post** – form with server-side validation
- **Edit / Update a post** – pre-filled form
- **Delete a post** – confirmation dialog before deletion
- **Search posts** – search by keyword across title, content and author
- **Flash messages** – success/error feedback after each action
- **Responsive layout** – works on desktop and mobile

---

## Tech Stack

| Layer      | Technology                          |
|------------|-------------------------------------|
| Backend    | Java 11, Jakarta/Javax Servlets 4.0 |
| View Layer | JSP 2.3, JSTL 1.2                   |
| Database   | MySQL 8 / Azure Database for MySQL  |
| JDBC Driver| mysql-connector-java 8.0.33         |
| Build      | Apache Maven 3.x                    |
| Server     | Apache Tomcat 9.x                   |
| Deployment | Azure App Service (Java/Tomcat)      |

---

## Project Structure

```
personal-blog/
├── pom.xml
├── sql/
│   └── init.sql                        # DB creation + sample data
└── src/
    └── main/
        ├── java/com/blog/
        │   ├── model/
        │   │   └── BlogPost.java       # Domain model
        │   ├── dao/
        │   │   └── BlogPostDAO.java    # All DB operations (JDBC)
        │   ├── util/
        │   │   └── DBConnection.java   # Connection factory
        │   └── servlet/
        │       ├── HomeServlet.java
        │       ├── ViewPostServlet.java
        │       ├── CreatePostServlet.java
        │       ├── EditPostServlet.java
        │       └── DeletePostServlet.java
        └── webapp/
            ├── index.jsp               # Redirect to HomeServlet
            ├── css/
            │   └── style.css
            ├── js/
            │   └── app.js
            └── WEB-INF/
                ├── web.xml
                └── views/
                    ├── header.jsp
                    ├── footer.jsp
                    ├── index.jsp       # Post list
                    ├── view.jsp        # Single post
                    ├── create.jsp      # Create form
                    ├── edit.jsp        # Edit form
                    └── error.jsp       # Error page
```

---

## Prerequisites

- **Java 11+** (OpenJDK or Oracle JDK)
- **Apache Maven 3.6+**
- **Apache Tomcat 9.x**
- **MySQL 8.0** (or Azure Database for MySQL)
- **Git**

---

## Local Setup

### 1 – Clone the repository

```bash
git clone https://github.com/MicahL2004/Personal-Blog.git
cd Personal-Blog
```

### 2 – Create the database

```bash
mysql -u root -p < sql/init.sql
```

Or paste the contents of `sql/init.sql` into a MySQL client such as MySQL Workbench.

### 3 – Set environment variables

The application reads its configuration from environment variables.  
Set these in your shell (or in Tomcat's `setenv.sh`):

```bash
# Linux / macOS
export DB_HOST=localhost
export DB_PORT=3306
export DB_NAME=blog_db
export DB_USER=root
export DB_PASSWORD=your_password

# Windows PowerShell
$env:DB_HOST="localhost"
$env:DB_PORT="3306"
$env:DB_NAME="blog_db"
$env:DB_USER="root"
$env:DB_PASSWORD="your_password"
```

### 4 – Build the WAR file

```bash
mvn clean package
```

The WAR is created at `target/personal-blog.war`.

---

## Running Locally

### Option A – Deploy to Tomcat manually

1. Copy `target/personal-blog.war` to `$TOMCAT_HOME/webapps/`
2. Start Tomcat: `$TOMCAT_HOME/bin/startup.sh` (Linux/macOS) or `startup.bat` (Windows)
3. Open [http://localhost:8080/personal-blog/](http://localhost:8080/personal-blog/)

### Option B – Tomcat Maven plugin

Add the following to `pom.xml` plugins section (already included) and run:

```bash
mvn tomcat7:run
```

Then open [http://localhost:8080/personal-blog/](http://localhost:8080/personal-blog/)

---

## Azure Deployment

### 1 – Create Azure resources

```bash
# Resource group
az group create --name blog-rg --location eastus

# Azure Database for MySQL
az mysql server create \
  --resource-group blog-rg \
  --name my-blog-db \
  --admin-user blogadmin \
  --admin-password <secure-password> \
  --sku-name GP_Gen5_2

# Allow Azure services to connect
az mysql server firewall-rule create \
  --resource-group blog-rg \
  --server my-blog-db \
  --name AllowAzureIPs \
  --start-ip-address 0.0.0.0 \
  --end-ip-address 0.0.0.0

# Create the database
az mysql db create \
  --resource-group blog-rg \
  --server-name my-blog-db \
  --name blog_db

# App Service plan
az appservice plan create \
  --resource-group blog-rg \
  --name blog-plan \
  --is-linux \
  --sku B1

# Web App with Tomcat
az webapp create \
  --resource-group blog-rg \
  --plan blog-plan \
  --name my-personal-blog \
  --runtime "TOMCAT|9.0-java11"
```

### 2 – Configure connection string

```bash
az webapp config appsettings set \
  --resource-group blog-rg \
  --name my-personal-blog \
  --settings \
    DB_HOST="my-blog-db.mysql.database.azure.com" \
    DB_PORT="3306" \
    DB_NAME="blog_db" \
    DB_USER="blogadmin@my-blog-db" \
    DB_PASSWORD="<secure-password>"
```

### 3 – Initialise the database

Run `sql/init.sql` against your Azure MySQL instance using MySQL Workbench or the Azure Portal Query Editor.

### 4 – Deploy the WAR

```bash
mvn clean package
az webapp deploy \
  --resource-group blog-rg \
  --name my-personal-blog \
  --src-path target/personal-blog.war \
  --type war
```

Your blog will be live at `https://my-personal-blog.azurewebsites.net/personal-blog/`

---

## Environment Variables

| Variable      | Default             | Description                                      |
|---------------|---------------------|--------------------------------------------------|
| `DB_URL`      | *(not set)*         | Full JDBC URL override (takes precedence)        |
| `DB_HOST`     | `localhost`         | Database hostname                                |
| `DB_PORT`     | `3306`              | Database port                                    |
| `DB_NAME`     | `blog_db`           | Database / schema name                           |
| `DB_USER`     | `root`              | Database username                                |
| `DB_PASSWORD` | *(empty)*           | Database password                                |
| `DB_DRIVER`   | `com.mysql.cj.jdbc.Driver` | JDBC driver class                       |

---

## Database Schema

```sql
CREATE TABLE blog_posts (
    id         INT          NOT NULL AUTO_INCREMENT PRIMARY KEY,
    title      VARCHAR(255) NOT NULL,
    content    TEXT         NOT NULL,
    author     VARCHAR(100) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
                                     ON UPDATE CURRENT_TIMESTAMP
);
```

---

## URL Routes

| URL              | Method | Description              |
|------------------|--------|--------------------------|
| `/`              | GET    | List all posts           |
| `/?q=keyword`    | GET    | Search posts             |
| `/post?id=N`     | GET    | View single post         |
| `/create`        | GET    | Show create form         |
| `/create`        | POST   | Submit new post          |
| `/edit?id=N`     | GET    | Show edit form           |
| `/edit?id=N`     | POST   | Submit updated post      |
| `/delete`        | POST   | Delete a post            |

---

## License

MIT – feel free to use this project as a learning starter.