<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty pageTitle ? 'Personal Blog' : pageTitle.concat(' – Personal Blog')}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<!-- ===== Navigation ===== -->
<nav class="navbar">
    <div class="container">
        <a href="${pageContext.request.contextPath}/" class="navbar-brand">&#9997; My Blog</a>

        <ul class="navbar-nav">
            <li><a href="${pageContext.request.contextPath}/"
                   class="${empty param.nav ? 'active' : ''}">Home</a></li>
            <li><a href="${pageContext.request.contextPath}/create"
                   class="${param.nav == 'create' ? 'active' : ''}">&#43; Create Post</a></li>
        </ul>

        <!-- Search form -->
        <form class="nav-search" method="get"
              action="${pageContext.request.contextPath}/">
            <input type="text" name="q" placeholder="Search posts…"
                   value="<c:out value='${searchKeyword}'/>">
            <button type="submit">&#128269;</button>
        </form>
    </div>
</nav>

<!-- ===== Flash Message ===== -->
<c:if test="${not empty sessionScope.flashMessage}">
    <div class="container" style="padding-top:20px">
        <div class="flash flash-${sessionScope.flashType}">
            <c:out value="${sessionScope.flashMessage}"/>
        </div>
    </div>
    <c:remove var="flashMessage" scope="session"/>
    <c:remove var="flashType"    scope="session"/>
</c:if>

<main>
    <div class="container">
