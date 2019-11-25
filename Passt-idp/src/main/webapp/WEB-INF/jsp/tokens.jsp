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
        <script src="/script/tokens.js" type="text/javascript"></script>
        <title>Tokens</title>
    </head>
    <body>
        <div class="container">
            <table class="table">
                <tr>
                    <th style="width:35%">Identifier (jti)</th>
                    <th style="width:35%">Client</th>
                    <th styre="width:35%">Location</th>
                    <th style="width:35%">Browserinfo</th>                    
                    <th style="width:5%"></th>
                </tr>
                <c:forEach items="${tokens}" var="token">
                    <tr>
                        <td>${token.jti}</td>
                        <td>${token.clientId}</td>
                        <td>${token.location}</td>
                        <td>${token.userAgent}</td>
                        <td>
                            <!-- Edit/Delete button -->
                            <div class="btn-group inline pull-left">
                                <button type="button" class="btn btn-default btn-sm shadow-none revokeByJtiButton" value="${token.jti}">
                                    <i class="fa fa-trash" aria-hidden="true"></i>
                                </button>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <button type="button" class="btn btn-default btn-sm shadow-none" id="revokeAllButton">
                <i class="fa fa-trash" aria-hidden="true">Revoke all</i>
            </button>
        </div>
    </body>
</html>