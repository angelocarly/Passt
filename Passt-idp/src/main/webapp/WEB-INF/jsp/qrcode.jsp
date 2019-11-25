<%-- 
    Document   : qrcode
    Created on : Apr 30, 2019, 1:22:10 PM
    Author     : Angelo Carly
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jstl/sql" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <body>
        <div id="qr">
            <p>
                Scan this Barcode using your Authenticator app on your phone 
                to use it later in login
            </p>
            <img src="data:image/jpeg;base64,${qrcodebase64}"/>
        </div>
        <a href="/client" class="btn btn-primary">Go to login page</a>
    </body>
</html>