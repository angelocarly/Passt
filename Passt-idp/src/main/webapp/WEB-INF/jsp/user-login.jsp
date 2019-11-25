<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jstl/sql" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<html>

    <head>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css"/>
        <title>Login</title>
        <link href="/webjars/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="/webjars/font-awesome/4.7.0/css/font-awesome.min.css">
        <script src="/webjars/jquery/1.9.1/jquery.min.js"></script>
        <script src="/webjars/bootstrap/3.3.6/js/bootstrap.min.js"></script>
    </head>
    <body>
        <div class="container">
            <div class="row wrapper">
                <form name='f' action="login" method='POST'>
                    <span id="welcometext">&nbsp;</span>

                    <br /><br />
                    <label class="col-sm-4">Username</label>
                    <span class="col-sm-8"><input class="form-control" type='text' name='username' value=''/></span>

                    <br/><br/>        
                    <label class="col-sm-4">password</label>
                    <span class="col-sm-8"><input class="form-control" type='password' name='password' /></span>

                    <br/><br/>        
                    <label class="col-sm-4">Google Authenticator Verification Code</label>
                    <span class="col-sm-8"><input class="form-control" type='text' name='code' /></span>

                    <br/><br/>
                    <input class="btn btn-primary" name="submit" type="submit" />

                </form>
            </div>
        </div>
    </body>

</html>