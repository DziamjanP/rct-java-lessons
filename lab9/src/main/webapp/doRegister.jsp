<%@page language="java" pageEncoding="UTF-8" %>
<%-- Импортировать JSTL-библиотеку --%>
<%@taglib prefix="c" uri="jakarta.tags.core" %>
<%@taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%-- Импортировать собственную библиотеку тегов --%>
<%@taglib prefix="ad" uri="http://adboard.tags/ad" %>
<%-- Указать, что мы ожидаем данные в кодировке UTF-8 --%>
<fmt:requestEncoding value="UTF-8" />
<%-- Удалить из сессии старые данные --%>
<c:remove var="userData" />
<%-- Сконструировать новый JavaBean в области видимости сессии --%>
<jsp:useBean id="userData" class="entities.User" scope="session" />
<%-- Скопировать в bean все параметры из HTTP-запроса --%>
<jsp:setProperty name="userData" property="*" />
<jsp:setProperty name="userData" property="checkId" value="${sessionScope.captchaCheckId}"/>
<%-- Обратиться к собственному тегу для сохранения пользователя --%>
<ad:addUser user="${userData}" />
<%-- Проанализировать переменную errorMessage в области видимости session --%>
<c:choose>
    <c:when test="${sessionScope.errorMessage==null}">
        <%-- Ошибок не возникло, удалить из сессии сохранѐнные данные пользователя --%>
        <c:remove var="userData" scope="session" />
        <%-- Инициировать процесс аутентификации --%>
        <jsp:forward page="/doLogin.jsp" />
    </c:when>
    <c:otherwise>
        <%-- Переадресовать на форму регистрации --%>
        <c:redirect url="/register.jsp" />
    </c:otherwise>
</c:choose>