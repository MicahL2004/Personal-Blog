<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"      %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"       %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions"  %>

<c:set var="pageTitle" value="Home"/>
<c:set var="navPage" value="home"/>
<%@ include file="header.jsp" %>

<!-- Page header -->
<div class="page-header page-header-hero">
    <div>
        <span class="eyebrow">Personal Blog</span>
        <h1>
            <c:choose>
                <c:when test="${not empty searchKeyword}">
                    Search results for: &ldquo;<c:out value="${searchKeyword}"/>&rdquo;
                </c:when>
                <c:otherwise>Thoughts, stories, and ideas worth sharing.</c:otherwise>
            </c:choose>
        </h1>
        <p>
            <c:choose>
                <c:when test="${not empty searchKeyword}">
                    Found <strong>${fn:length(posts)}</strong> post(s).
                    <a href="${pageContext.request.contextPath}/">Clear search</a>
                </c:when>
                <c:otherwise>A clean space for publishing updates, reflections, and creative work.</c:otherwise>
            </c:choose>
        </p>
    </div>
    <a href="${pageContext.request.contextPath}/create" class="btn btn-primary">&#43; Write a Post</a>
</div>

<!-- Error banner (DB unreachable, etc.) -->
<c:if test="${not empty errorMessage}">
    <div class="flash flash-error">&#9888; <c:out value="${errorMessage}"/></div>
</c:if>

<!-- Post list -->
<c:choose>
    <c:when test="${empty posts}">
        <div class="empty-state">
            <h2>No posts yet</h2>
            <p>Publish your first post to start building your blog archive.</p>
            <p><a href="${pageContext.request.contextPath}/create" class="btn btn-primary">Create your first post</a></p>
        </div>
    </c:when>
    <c:otherwise>
        <div class="post-grid">
            <c:forEach var="post" items="${posts}">
                <article class="post-card">
                    <h2 class="post-card-title">
                        <a href="${pageContext.request.contextPath}/post?id=${post.id}">
                            <c:out value="${post.title}"/>
                        </a>
                    </h2>

                    <div class="post-card-meta">
                        <span>&#128100; <c:out value="${post.author}"/></span>
                        <span>&#128197;
                            <fmt:formatDate value="${post.createdAt}"
                                            pattern="MMM d, yyyy"/>
                        </span>
                    </div>

                    <p class="post-card-preview">
                        <c:out value="${post.contentPreview}"/>
                    </p>

                    <div class="post-card-actions">
                        <a href="${pageContext.request.contextPath}/post?id=${post.id}"
                           class="btn btn-primary btn-sm">&#128065; View</a>
                        <a href="${pageContext.request.contextPath}/edit?id=${post.id}"
                           class="btn btn-warning btn-sm">&#9998; Edit</a>
                        <form method="post"
                              action="${pageContext.request.contextPath}/delete"
                              style="display:inline"
                              data-title="<c:out value='${post.title}'/>"
                              onsubmit="return confirmDelete(this.dataset.title)">
                            <input type="hidden" name="id" value="${post.id}">
                            <button type="submit" class="btn btn-danger btn-sm">&#128465; Delete</button>
                        </form>
                    </div>
                </article>
            </c:forEach>
        </div>
    </c:otherwise>
</c:choose>

<%@ include file="footer.jsp" %>
