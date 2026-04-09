<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Create Post"/>
<%@ include file="header.jsp" %>

<div class="page-header">
    <h1>&#43; Create New Post</h1>
    <p>Share your thoughts with the world.</p>
</div>

<c:if test="${not empty errorMessage}">
    <div class="error-box">&#9888; <c:out value="${errorMessage}"/></div>
</c:if>

<div class="form-card">
    <form method="post" action="${pageContext.request.contextPath}/create"
          novalidate>
        <div class="form-group">
            <label for="title">Title <span style="color:#ef4444">*</span></label>
            <input type="text" id="title" name="title" maxlength="255" required
                   placeholder="Enter post title…"
                   value="<c:out value='${title}'/>">
            <span class="hint">Max 255 characters</span>
        </div>

        <div class="form-group">
            <label for="author">Author <span style="color:#ef4444">*</span></label>
            <input type="text" id="author" name="author" maxlength="100" required
                   placeholder="Your name…"
                   value="<c:out value='${author}'/>">
        </div>

        <div class="form-group">
            <label for="content">Content <span style="color:#ef4444">*</span></label>
            <textarea id="content" name="content" required
                      placeholder="Write your post here…"><c:out value="${content}"/></textarea>
        </div>

        <div class="form-actions">
            <button type="submit" class="btn btn-success">&#128190; Publish Post</button>
            <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">Cancel</a>
        </div>
    </form>
</div>

<%@ include file="footer.jsp" %>
