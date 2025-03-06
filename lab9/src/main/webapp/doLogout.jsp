<%@page language="java" pageEncoding="UTF-8" %>
<%-- Импортировать JSTL-библиотеку --%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%-- Удалить из сессии JavaBean с данными пользователя --%>
<c:remove var="authUser" scope="session" />
<%-- Переадресовать на начальную страницу --%>
<c:redirect url="/index.jsp" />