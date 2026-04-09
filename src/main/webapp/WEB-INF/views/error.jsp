<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageTitle" value="Error"/>
<%@ include file="header.jsp" %>

<div style="text-align:center; padding: 60px 20px;">
    <div style="font-size:4rem; margin-bottom:16px">&#9888;</div>
    <h1 style="font-size:1.8rem; color:#1e293b; margin-bottom:12px">Oops! Something went wrong.</h1>
    <p style="color:#64748b; margin-bottom:28px">
        <c:out value="${not empty errorMessage ? errorMessage : 'An unexpected error occurred.'}"/>
    </p>
    <a href="${pageContext.request.contextPath}/" class="btn btn-primary">&#8592; Return Home</a>
</div>

<%@ include file="footer.jsp" %>
