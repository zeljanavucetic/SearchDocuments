<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
	<head>  
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<meta charset="utf-8">
		<title>Recommendation System</title>
                <jsp:include page="include.jsp" />
		<meta name="generator" content="Bootply" />
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">

	 <script>var myContextPath = "${pageContext.request.contextPath}"</script>

	</head>
	<body>
<div class="navbar navbar-default navbar-static-top">
  <div class="container">
    <div class="navbar-header">
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
        <span class="icon-bar"></span>
      </button>
      <a class="navbar-brand" href="#">FON</a>
    </div>
    <div class="collapse navbar-collapse">
      <ul class="nav navbar-nav">
        <li class="active"><a href="#">Home</a></li>
        <li><a href="#contact">Contact</a></li>
      </ul>
      <ul class="nav navbar-nav navbar-right">
        <li><a href="#about">About</a></li>
      </ul>
    </div><!--/.nav-collapse -->
  </div>
</div>

<div class="container">
  
  <div class="text-center">
    <h1>Recommendation System</h1>
  </div>
	<div class="row" style="padding-top:25px;">       
        <div id="custom-search-input">
            <div class="input-group col-md-12">
                <input type="text" class="  search-query form-control" placeholder="Search documents" id="w-input-search" />
                <span class="input-group-btn">
                    <button class="btn btn-success" type="button" id="search">
                        <span class=" glyphicon glyphicon-search"></span>
                        <span class="glyphicon glyphicon glyphicon-refresh" id="anim"></span>
                    </button>
                </span>
            </div>
        </div>
    </div>

</div><!-- /.container -->
	<!-- script references -->
		
	</body>
</html>