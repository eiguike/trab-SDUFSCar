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
        <title>YUL Tube ®</title>
        <style>
            html, body {
                margin:0;
                padding:0;
                height:100%;
            }
            #id{
                border-top:thin solid  #e5e5e5;
                border-right:thin solid #e5e5e5;
                border-bottom:0;
                border-left:thin solid  #e5e5e5;
                box-shadow:0px 1px 1px 1px #e5e5e5;
                float:left;
                height:17px;
                margin:.8em 0 0 .5em; 
                outline:0;
                padding:.4em 0 .4em .6em; 
                width:183px; 
            }

            #button-holder{
                background-color:#f1f1f1;
                border-top:thin solid #e5e5e5;
                box-shadow:1px 1px 1px 1px #e5e5e5;
                cursor:pointer;
                float:left;
                height:27px;
                margin:11px 0 0 0;
                text-align:center;
                width:50px;
            }

            #button-holder img{
                margin:4px;
                width:20px; 
            }

            #top {
                background-color: #FFFFFFF;
                -webkit-box-shadow: 0px 0px 3px 2px #000000;
                -moz-box-shadow: 0px 0px 3px 2px #000000;
                box-shadow: 0px 0px 4px 0.3px #000000;
            }
            
            #videoPlayer{
                margin-top: 2%;
                margin-left: 1%;
            }
        </style>        
        <script src="http://code.jquery.com/jquery-latest.js"></script>
    </head>
    <body>
        <div id="top" class="top-shadow">
            <img src="yultubelogo.png" height="60" width="130" style="vertical-align: middle;">
            <div style="display: inline-block; vertical-align: middle;">
                <form id="download" action="" method="GET">
                    <input type='text' placeholder='Procurar...' id='id' style="width: 400px; ">
                    <a id="button-holder" href="#" style="width: 100px"><img src='magnifying_glass.png'/></a>
                </form>
               
            </div>
            <div id="button-holder" style="vertical-align:middle;">heuehue</div>
        </div>
        
        <div id="videoPlayer"></div>
        
        <div id="formEnviarVideo">
            <form action="/trabalhoSDWeb/Upload" method="POST" enctype="multipart/form-data">
                Arquivo do vídeo: <input type="file" name="arquivo" size="chars" ><br>
                Descrição do vídeo: <input type="text" name="descricao"><br>
                <input type="submit" value="Submit">
            </form>         
        </div>



    </body>

    <script>

        $('#button-holder').click(function () { // catch the form's submit event
            $.ajax({// create an AJAX call...
                data: $(this).serialize(), // get the form data
                type: $(this).attr('method'), // GET or POST
                url: "/trabalhoSDWeb/Download", // the file to call
                success: function (response) { // on success..
                    $('#videoPlayer').html(response); // update the DIV
                }
            });
            return false;
        });
    </script>
</html>
