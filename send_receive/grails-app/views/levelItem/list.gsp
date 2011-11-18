<%@ page import="com.ps.sr.model.LevelItem" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'levelItem.label', default: 'LevelItem')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
<div class="nav">
    <span class="menuButton"><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a>
    </span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="default.new.label"
                                                                               args="[entityName]"/></g:link></span>
</div>

<div class="body">
    <h1><g:message code="default.list.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message">${flash.message}</div>
    </g:if>
    <div class="list">
        <table>
            <thead>
            <tr>

                <g:sortableColumn property="id" title="${message(code: 'levelItem.id.label', default: 'Id')}"/>

                <g:sortableColumn property="icon" title="${message(code: 'levelItem.icon.label', default: 'Icon')}"/>

                <g:sortableColumn property="text" title="${message(code: 'levelItem.text.label', default: 'Text')}"/>

            </tr>
            </thead>
            <tbody>
            <g:each in="${levelItemInstanceList}" status="i" var="levelItemInstance">
                <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                    <td><g:link action="show"
                                id="${levelItemInstance.id}">${fieldValue(bean: levelItemInstance, field: "id")}</g:link></td>

                    <td>${fieldValue(bean: levelItemInstance, field: "icon")}</td>

                    <td>${fieldValue(bean: levelItemInstance, field: "text")}</td>

                </tr>
            </g:each>
            </tbody>
        </table>
    </div>

    <div class="paginateButtons">
        <g:paginate total="${levelItemInstanceTotal}"/>
    </div>
</div>
</body>
</html>
