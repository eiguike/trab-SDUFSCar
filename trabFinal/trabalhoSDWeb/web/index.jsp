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
                <form id="upload" action="/trabalhoSDWeb/Upload" method="POST" enctype="multipart/form-data">
                    <p>Arquivo do vídeo:</p> <input name="arquivo" size="chars" type="file"><br>
                    <p>Descrição do vídeo:</p>
                    <textarea name="descricao" cols="50" rows="4"></textarea><br>
                    <a class="botaoEnviarEnviar" style="padding-right: 15px; width: unset; padding-left: 15px;" id="button-holder">
                        <p style="line-height: 1px; color: rgb(183, 183, 183);">Submeter</p>
                    </a>
                    <!--<input type="submit" value="Submit">-->
                </form>         
            </div>
        </div>

        <!--Nessa parte será substituido pelo formulário-->
        <div id="todoVideo" style="height:100%;">
        </div>



                <form id="dowlnoadHidden" action="" method="POST">
                </form>



        <script>
                $.ajax({// create an AJAX call...
                    data: {idVideo:"6"},// get the form data
                    type: $('#downloadHidden').attr('method'), // GET or POST
                    url: "/trabalhoSDWeb/Download", // the file to call
                    success: function (response) { // on success..
                        $('#todoVideo').html(response); // update the DIV
                        $('#todoVideo').show();
                        $('#todoForm').hide(); // update the DIV
                    }
                });
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

//USAGE: $("#form").serializefiles();
            (function ($) {
                $.fn.serializefiles = function () {
                    var obj = $(this);
                    /* ADD FILE TO PARAM AJAX */
                    var formData = new FormData();
                    $.each($(obj).find("input[type='file']"), function (i, tag) {
                        $.each($(tag)[0].files, function (i, file) {
                            formData.append(tag.name, file);
                        });
                    });
                    var params = $(obj).serializeArray();
                    $.each(params, function (i, val) {
                        formData.append(val.name, val.value);
                    });
                    return formData;
                };
            })(jQuery);

            $('.botaoEnviarEnviar').click(function () { // catch the form's submit event
                var formData = new FormData($('#upload')[0]);
                $.ajax({
                    url: '/trabalhoSDWeb/Upload', //server script to process data
                    type: 'POST',
                    xhr: function () {  // custom xhr
                        myXhr = $.ajaxSettings.xhr();
                        if (myXhr.upload) { // if upload property exists
//                            myXhr.upload.addEventListener('progress', progressHandlingFunction, false); // progressbar
                        }
                        return myXhr;
                    },
                    //Ajax events
                    success: function (data) {
                        window.alert("Vídeo enviado com sucesso! Id:"+data);
                    },
                    error: errorHandler = function () {
                        window.alert("não sucess");
                    },
                    // Form data
                    data: formData,
                    //Options to tell JQuery not to process data or worry about content-type
                    cache: false,
                    contentType: false,
                    processData: false
                }, 'json');
            });
            
            $('.botaoEnviar').click(function () { // catch the form's submit event
                $('#todoForm').show(); // update the DIV
                $('#todoPlayer').hide();
                $('#descricaoVideo').hide();
                $('#videoPlayer').hide();
                return false;
            });
            $('#todoForm').hide();
        </script>

    </body></html>