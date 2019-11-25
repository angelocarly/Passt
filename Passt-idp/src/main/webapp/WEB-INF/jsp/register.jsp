<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jstl/sql" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html >
    <head>
        <link href="/webjars/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css">
        <script src="/webjars/jquery/1.9.1/jquery.min.js"></script>
        <script src="/webjars/bootstrap/3.3.6/js/bootstrap.min.js"></script>
        <script src="/script/register.js" type="text/javascript"></script>
        <title>Register</title>
    </head>
    <body>
        <div class="container">
            <div class="justify-content-center">
                <div class="col-md-4 mx-auto">
                    <h5 class="card-title text-center">Register</h5>
                    <form:form action="/register" method="POST" enctype="utf8" class="form-signin" modelAttribute="user">

                        <div class="form-group">
                            <label for="inputUsername">Username</label>
                            <div class="controls">
                                <form:input type="text" path="username" id="inputUsername" class="form-control" />
                                <form:errors path="username" cssClass="error"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="inputEmail">Email address</label>
                            <div class="controls">
                                <form:input type="email" path="email" id="inputEmail" class="form-control" placeholder="Email address" />
                                <form:errors path="email" cssClass="error"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="inputPassword">Password</label>
                            <div class="controls">
                                <form:input type="password" path="password" id="inputPassword" class="form-control" placeholder="Password"/>
                                <form:errors path="password" cssClass="error"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="inputMatchingPassword">Password verification</label>
                            <div class="controls">
                                <form:input type="password" path="matchingPassword" id="inputMatchingPassword" class="form-control" placeholder="Password"/>
                                <form:errors path="matchingPassword" cssClass="error"/>
                            </div>
                        </div>

                        <div class="custom-control custom-checkbox mb-3">
                            <form:checkbox id="use2FA" path="using2FA"/>
                            <label class="" for="use2FA">Use 2 Factor Authentication</label>
                        </div>
                        <button class="btn btn-lg btn-primary btn-block text-uppercase" type="submit">Register</button>
                    </form:form>
                    <!--- <a th:href="@{/login}">login</a> --->
                </div>
            </div>
        </div>
    </body>
</html>