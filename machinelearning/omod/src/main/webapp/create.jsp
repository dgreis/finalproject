<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<head>

</head>
<%@ include file="template/localHeader.jsp"%>

<p>Hello ${user.systemId}!</p>
<form id ="create" method="get" action="">
<p> This module would create the input file. Run the structure data module followed by this module </p>
<input type="submit" value="startprocess">
</form>


<script> 
$j('#create').submit(function (event){
	event.preventDefault();
	console.log("creating data file");	
	
	$j.ajax({
        url: "http://localhost:8080/openmrs/module/machinelearning/createip.form",
        data: {},
        success: function(data){
        	console.log(data);
        	alert("input file created");
        },
        dataType: "html",
        timeout: 200000,
        error: function(objAJAXRequest, strError )
		        {
		        	alert("i am in the error section");
        			console.log(strError);
		        }
      });
	
	console.log("submit called");
});
</script>
<%@ include file="/WEB-INF/template/footer.jsp"%>