<%-- 
    Document   : InputCollector
    Created on : 05-May-2019, 07:59:33
    Author     : sendh
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);
%>
<!DOCTYPE html>
<html>

    <head>

        <title>Welcome To RABO-BANK Processor</title>
        <meta charSet="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="Cache-Control" content="no-cache, no-store, must-revalidate"/>
        <META HTTP-EQUIV="PRAGMA" CONTENT="NO-CACHE"/> 
        <META HTTP-EQUIV="Expires" CONTENT="-1"/>
        <script src="./scripts/jquery-3.3.1.min.js"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                $(".form-basic").submit(function () {
                    $(".btnArea").hide();
                    $("#ajaxLoader").html("<img src=\"./images/ajaxLoader.svg\"/>&nbsp;&nbsp;<span>Processing .. Please Wait </span>");
                });

            });
        </script>
    </head>
    <body>
        <header>
            <div class="form-basic form-title-row">
                <h1>RABO Processing</h1>
        </header>
        <div class="main-content">
            <form id="main-form" autocomplete="off" class="form-basic" method="post" enctype="multipart/form-data"
                  action="/RaboBank_Statement_Processor/controller/processInput">
                <div class="form-row">
                    <label>
                        <span>Please Upload the file in EXCEL format </span>
                        <input type="file" name="file" accept=".xlsx" autocomplete="off" autofocus title="Please upload .xlsx file" required/>

                    </label>
                </div>
                <div align="center" class="btnArea">
                    <button id="submit" type="submit">SUBMIT</button>

                </div>
                <div id="ajaxLoader"></div>
            </form>
    </body>
</html>
<style type="text/css">
    #ajaxLoader{
        animation:blinker is linear infinite;
    }
    @keyframes blinker{
        50% { opacity:0;}

    }
    #content {
        margin-left :48px;
        min-width: 45%
    }
    .btnArea button{
        display:inline !important;
        margin: 0 10px 0 0 !important;

    }
    html
    {
        background-color: #f3f3f3;
    }
    .form-basic{

        max-width: 80%;
        margin:0 auto;
        padding:10px;
        box-sizing:border-box;
        background-color:#C5E3DE;
        box-shadow:0 1px 3px 0 rgba(0, 0, 0, 0.1);
        font: bold 14px sans-serif;
        text-align: center;
    }

    .form-basic.form-row{
        text-align:center;
        margin-bottom:55px;
    }
    /* Form Title*/
    .form-basic h1{
        display:inline-block;
        box-sizing:border-box;
        color: #4c565e;
        font-size: 24px;
        padding : 0 10px 15px;
        border-bottom: 2px solid #6caee0;
        margin: 0;
    }
    .form-basic .form-row> label span{
        display:inline-block;
        box-sizing:border-box;
        color: #4c565e;
        width: auto;
        text-align: center;
        vertical-align: top;
        padding : 12px 25px;

    }

    .form-basic input{
        color: #5f5f5f;
        box-sizing:border-box;
        width: 240px;
        box-shadow: 1px 2px 4px 0 rgba(0, 0, 0, 0.08);
        padding : 12px ;
        border: 1px solid #dbdbdb;
    }
    
    .form-basic button{
        text-align: center;
        display:block;
        border-radius:2px;
          background-color:#0fb8de;
        color: #ffffff;
        font-weight : bold;
          box-shadow: 1px 2px 4px 0 rgba(0, 0, 0, 0.08);
       
        padding : 12px 22px;
        border:0;
        margin: 40px 183px 0;
    }
