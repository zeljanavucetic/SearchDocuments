<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
    <h1>Similar documents</h1>
  </div>

<div class="container">
    <div class="row">
        <div class="col-md-12">
            <h2 class="text-center"><c:out value="${title}" /></h2>
<!--            <div class="text-justify"><c:out value="${content}" /></div>-->
            
        </div>

    </div>
    <div class="row">
            <div class="col-md-12">
            <h2 class="text-left">Recommended documents</h2>
            
            <table>
		<c:forEach items="${similarDocuments}" var="sim">
                  <tr>
                     <c:set var="count" value="${count + 1}" scope="page"/>
      <td class="text-justify"><c:out value = "${count}"></c:out>. <b><c:out value="${sim.simTitle}"/></b> - <c:out value="${sim.simContent}"/></td>
     <li>Similarity: <c:out value="${sim.similarity}"/></li>   
                  </tr>
		</c:forEach>
    	    </table>



        </div>
    </div>

    <div class="row">
        <div class="col-md-8 text-justify">                  
            <br />
            <br />
            <a href="${pageContext.request.contextPath}/" class="btn btn-primary">
                <span class="glyphicon glyphicon-hand-left"></span> Back
            </a>
        </div> 
    </div>
    <br>
</div>
</div><!-- /.container -->
	<!-- script references -->
		
	</body>
</html>