<%-- 
    Document   : ProductList
    Created on : Nov 15, 2018, 1:37:44 PM
    Author     : JewlyTwin
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <style>
            .image {
                background-repeat: no-repeat;
                background-attachment: fixed;
                background-position: center;
            }
            .bg-image {
                background-image: url("image/bgindex.jpg"); 
                filter: blur(8px);
                height: 100%; 
                background-position: center;
                background-repeat: no-repeat;
                background-size: cover;
              }
              .bg-text {
                background-color: rgb(0,0,0); /* Fallback color */
                background-color: rgba(0,0,0, 0.4); /* Black w/opacity/see-through */
                color: white;
                font-weight: bold;
                border: 3px solid #f1f1f1;
                position: absolute;
                top: 50%;
                left: 50%;
                transform: translate(-50%, -50%);
                z-index: 2;
                width: 80%;
                padding: 20px;
                text-align: center;
            }
            .test {
                height: 20em;
            }
            

        </style>
    </head>
    <body>
        <div class="test">
            <div class="bg-image"></div>
            <div class="bg-text">
                <h2>Blurred Background</h2>
                <h1 style="font-size:50px">I am John Doe</h1>
                <p>And I'm a Photographer</p>
            </div>
        </div>

        <%@include file="/include/Header.jsp"%>
        <div class="container">
            <form class="form-inline my-2 my-lg-0">
                <input class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search">
                <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
            </form>
            <div class="col-12 ">
                <div class="row">
                    <c:forEach items="${products}" var="p" varStatus="i">
                        <div class="col-4">
                            <div class="col-12 my-3">
                                <div class="card">
                                    <img class="image" src="image/${p.productname}.jpeg" alt="${p.productname}" height="250em;">
                                    <div class="card-body">
                                        <h5 class="card-title"><p>${p.productname}</p></h5>
                                        <p class="card-text">${p.detail}</p>
                                    </div>
                                    <div class="card-body">
                                        <a href="#" class="card-link">Detail</a>
                                        <a href="#" class="card-link">Add to cart</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </body>
</html>
