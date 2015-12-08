<!DOCTYPE html>
<html><head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>YUL Tube ®</title>
        <style>
            html, body {
                margin:0;
                padding:0;
                height:100%;

            }
            p  {
                font-family: sans-serif;
                font-size: 100%;
            }
            b  {
                font-family: sans-serif;
                font-size: 120%;
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
                box-shadow: 0px 0px 4px 0.1px #C8C8C8;
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
            <img src="./yultubelogo.png" style="vertical-align: middle; padding-left: 20px;" width="130" height="60">
            <div style="display: inline-block; vertical-align: middle;">
                <form id="download" action="" method="POST">
                    <input placeholder="Procurar pela ID..." name="idVideo" id="id" style="width: 400px; " type="text">
                    <a class="botaoProcurar" id="button-holder" href="#" style="width: 100px"><img src="./magnifying_glass.png"></a>
                    <a class="botaoEnviar" style="float: right; display: inline-block; vertical-align: middle; padding-left: 15px; padding-right: 15px; margin-left: 30px; width: unset;" id="button-holder">
                        <p style="color: rgb(183, 183, 183); float: inherit; vertical-align: middle; line-height: 1px;">Enviar vídeo</p>
                    </a>
                </form>
            </div>

        </div>

        <div id="todoForm" style="display: inline-block; float: left; box-shadow: 0px 0px 4px 0.1px rgb(200, 200, 200); height: 100%; margin-top: 2%; margin-left: 2%; width: 96%;">
            <div id="formEnviar" style="margin:2%;">
                <form action="" method="POST" enctype="multipart/form-data">
                    <p>Arquivo do vídeo:</p> <input name="arquivo" size="chars" type="file"><br>
                    <p>Descrição do vídeo:</p>
                    <textarea name="descricao" cols="50" rows="4"></textarea><br>
                    <a class="botaoEnviarEnviar" style="padding-right: 15px; width: unset; padding-left: 15px;" id="button-holder">
                        <p style="line-height: 1px; color: rgb(183, 183, 183);">Submeter</p>
                    </a>
                </form>         
            </div>
        </div>

        <!--Nessa parte será substituido pelo formulário-->
        <div id="todoVideo" style="height:100%;">
            <div id="videoPlayer" style="display: inline-block; float: left; box-shadow: 0px 0px 4px 0.1px rgb(200, 200, 200); width: 65%; padding-left: 5px; height: 100%;">
                <b id="nomeVideo" style="padding-left: 20px;">
                    Bode doidão
                </b> 
                <div style="" id="video">
                    <video style="padding-bottom: 20px; padding-top: 15px; width: 95%; padding-left: 20px;" controls="controls">
                        <source src="https://trabalhosd.s3-sa-east-1.amazonaws.com/mwnn65vl?AWSAccessKeyId=AKIAJODUPR35QS2J2X3Q&Expires=1449560159&Signature=DSiV2MrADRBezVnJNKJN%2Bb43yIw%3D" type="video/mp4">
                        <source src="https://trabalhosd.s3-sa-east-1.amazonaws.com/mwnn65vl?AWSAccessKeyId=AKIAJODUPR35QS2J2X3Q&Expires=1449560159&Signature=DSiV2MrADRBezVnJNKJN%2Bb43yIw%3D" type="video/ogg">
                        Your browser does not support HTML5 video.
                    </video>

                </div>
            </div>
            <div id="descricaoVideo" style="background: white none repeat scroll 0% 0%; display: inline-block; box-shadow: 0px 0px 4px 0.1px rgb(200, 200, 200); margin-top: 2%; width: 32%; float: right; height: 100%;">
                <div style="margin:3%;">
                    <b> Descrição do vídeo:</b>
                    <p>Meu pé é grande doi demais quando vou ojgar futebol, 
                        isso porquê meu pé e grande e doid demais. Meu pé é grande doi demais 
                        quando vou ojgar futebol, isso porquê meu pé e grande e doid demais. Meu
                        pé é grande doi demais quando vou ojgar futebol, isso porquê meu pé e 
                        grande e doid demais. Meu pé é grande doi demais quando vou ojgar 
                        futebol, isso porquê meu pé e grande e doid demais. Meu pé é grande doi 
                        demais quando vou ojgar futebol, isso porquê meu pé e grande e doid 
                        demais. Meu pé é grande doi demais quando vou ojgar futebol, isso porquê
                        meu pé e grande e doid demais. Meu pé é grande doi demais quando vou 
                        ojgar futebol, isso porquê meu pé e grande e doid demais. Meu pé é 
                        grande doi demais quando vou ojgar futebol, isso porquê meu pé e grande e
                        doid demais. Meu pé é grande doi demais quando vou ojgar futebol, isso 
                        porquê meu pé e grande e doid demais. Meu pé é grande doi demais quando 
                        vou ojgar futebol, isso porquê meu pé e grande e doid demais.</p>
                </div>

            </div>
        </div>







        <script>

            $('.botaoProcurar').click(function () { // catch the form's submit event
                $.ajax({// create an AJAX call...
                    data: $('#download').serialize(), // get the form data
                    type: $('#download').attr('method'), // GET or POST
                    url: "/trabalhoSDWeb/Download", // the file to call
                    success: function (response) { // on success..
                        $('#todoVideo').html(response); // update the DIV
                        $('#todoVideo').show();
                        $('#todoForm').hide(); // update the DIV
                    }
                });
                return false;
            });
            $('.botaoEnviarEnviar').click(function () { // catch the form's submit event
                $.ajax({// create an AJAX call...
                    data: $(this).serialize(), // get the form data
                    type: $(this).attr('method'), // GET or POST
                    url: "/trabalhoSDWeb/Upload", // the file to call
                    success: function (response) { // on success..
                        window.alert("ehehheeh");
                    },
                    error: function (response) {
                        window.alert("ahaha falhou");
                    }
                });
                return false;
            });
            $('.botaoEnviar').click(function () { // catch the form's submit event
                $('#todoForm').show(); // update the DIV
                $('#todoPlayer').hide();
                return false;
            });
            $('#todoForm').hide();
        </script>

    </body></html>