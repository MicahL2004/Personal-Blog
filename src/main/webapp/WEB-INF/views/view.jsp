<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"  %>

<c:set var="pageTitle" value="${post.title}"/>
<c:set var="navPage" value="home"/>
<%@ include file="header.jsp" %>

<article class="post-full">
    <h1 class="post-full-title"><c:out value="${post.title}"/></h1>

    <div class="post-full-meta">
        <span>&#128100; <strong><c:out value="${post.author}"/></strong></span>
        <span>&#128197; Posted
            <fmt:formatDate value="${post.createdAt}" pattern="MMM d, yyyy 'at' h:mm a"/>
        </span>
        <c:if test="${post.updatedAt != null && post.updatedAt != post.createdAt}">
            <span>&#9998; Updated
                <fmt:formatDate value="${post.updatedAt}" pattern="MMM d, yyyy 'at' h:mm a"/>
            </span>
        </c:if>
    </div>

    <div class="post-full-content">
        <c:out value="${post.content}"/>
    </div>

    <div class="post-full-actions">
        <a href="${pageContext.request.contextPath}/"
           class="btn btn-secondary">&#8592; Back to Posts</a>
        <a href="${pageContext.request.contextPath}/edit?id=${post.id}"
           class="btn btn-warning">&#9998; Edit Post</a>
        <form method="post"
              action="${pageContext.request.contextPath}/delete"
              style="display:inline"
              data-title="<c:out value='${post.title}'/>"
              onsubmit="return confirmDelete(this.dataset.title)">
            <input type="hidden" name="id" value="${post.id}">
            <button type="submit" class="btn btn-danger">&#128465; Delete Post</button>
        </form>
    </div>
</article>

<%@ include file="footer.jsp" %>
