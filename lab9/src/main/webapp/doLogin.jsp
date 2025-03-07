<%@page language="java" pageEncoding="UTF-8" %>
<%-- Импортировать JSTL-библиотеки --%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%-- Импортировать собственную библиотеку тегов --%>
<%@taglib prefix="ad" uri="http://adboard.tags/ad" %>
<%-- Указать, что мы ожидаем данные в кодировке UTF-8 --%>
<fmt:requestEncoding value="UTF-8" />
<%-- Обратиться к собственному тегу для аутентификации пользователя на основе
указанных им логина и пароля --%>
<ad:login login="${param.login}" password="${param.password}" />
<%-- Проверить сообщение об ошибке, чтобы узнать результат аутентификации --%>
<c:choose>
    <c:when test="${sessionScope.errorMessage==null}">
        <%-- Ошибок не возникло, переадресовать на страницу личногокабинета --%>
        <c:redirect url="/cabinet.jsp" />
    </c:when>
    <c:otherwise>
        <%-- Переадресовать на начальную страницу --%>
        <c:redirect url="/index.jsp" />
    </c:otherwise>
</c:choose>