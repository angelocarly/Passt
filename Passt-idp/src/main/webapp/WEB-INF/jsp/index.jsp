<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jstl/sql" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html >
    <head>
        <link href="/webjars/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css">
        <script src="/webjars/jquery/1.9.1/jquery.min.js"></script>
        <script src="/webjars/bootstrap/3.3.6/js/bootstrap.min.js"></script>
        <script src="/script/index.js" type="text/javascript"></script>
        <title>Index</title>
    </head>
    <body>
        <h1>Login</h1>
        <c:if test="${!loggedIn}">
            <div class="container">
                <div>
                    With Passt <a href="/connect/passt">click here</a>
                </div>
            </div>
        </c:if>
        <c:if test="${loggedIn}">
            <div class="container">
                <p>Logged in as: ${username}</p>
                <div>
                    <button id="buttonTokens" class="btn btn-primary">Tokens</button>
                </div>
                <b>access token:</b>
                <p class="content" style="word-wrap:break-word">${access_token}</p>
                <div>
                    <button id="buttonLogout" class="btn btn-error">Logout</button>
                </div>
            </div>
        </c:if>
    </body>
</html>