<%-- 
    Document   : index
    Created on : Dec 5, 2015, 6:03:51 PM
    Author     : ricke
--%>

<%@page import="java.io.IOException"%>
<%@page import="java.io.File"%>
<%@page import="java.io.FileInputStream"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <form action="/trabalhoSDWeb/Upload" method="POST" enctype="multipart/form-data">
            Arquivo do vídeo: <input type="file" name="arquivo" size="chars" ><br>
            Descrição do vídeo: <input type="text" name="descricao"><br>
            <input type="submit" value="Submit">
        </form>         
    </body>
</html>
