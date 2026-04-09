<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${empty pageTitle ? 'Personal Blog' : pageTitle.concat(' – Personal Blog')}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css?v=20260409">
</head>
<body>

<!-- ===== Navigation ===== -->
<nav class="navbar">
    <div class="container">
        <a href="${pageContext.request.contextPath}/" class="navbar-brand">&#9997; My Blog</a>

        <div class="nav-actions">
            <ul class="navbar-nav">
                <li><a href="${pageContext.request.contextPath}/"
                       class="${navPage == 'home' || empty navPage ? 'active' : ''}">Home</a></li>
                <li><a href="${pageContext.request.contextPath}/create"
                       class="${navPage == 'create' ? 'active' : ''}">&#43; Create Post</a></li>
            </ul>

            <form class="nav-search" method="get"
                  action="${pageContext.request.contextPath}/">
                <input type="text" name="q" placeholder="Search posts…"
                       value="<c:out value='${searchKeyword}'/>">
                <button type="submit" aria-label="Search posts">&#128269;</button>
            </form>
        </div>
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
