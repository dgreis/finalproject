<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<head>

<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
 <script src="http://code.jquery.com/jquery-1.10.2.js"></script>
  <script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
  <style>
  
</style>
</head>
<%@ include file="template/localHeader.jsp"%>


<div id="tabs">
  <ul>
    <li><a href="#tabs-1">Compile Data</a></li>
    <li><a href="#tabs-2">Train Model</a></li>
    
  </ul>
  <div id="tabs-1">
    <p>Hello ${user.systemId}! This wizard will install or update the prediction engine as needed.</p>
<form id ="create" method="get" action="">
<p>Click the button below to compile the data. The process will take a minute or two.</p>
<input type="submit" value="Start">
</form>
  </div>
  <div id="tabs-2">
    <p>Click the button below to train the prediction engine. After it completes, you're good to go!</p>
    <form id ="runmodel" method="get" action="">
    <input type="submit" value="Start">
    </form>
  </div>
 
</div>




<script>
$j(document).ready(function(){
	$j('h2').text("Diagknowzit Module Management")
	
	var a = $j("<div>"+""+"</div>");
    a.attr("id","dialog-message");
    
    $j( "body" ).append(a);
   
    
    
	console.log("ready");
	
	// enable tabs
	$(function() {
	    $( "#tabs" ).tabs();
	    // disable the second tab
	    $( "#tabs" ).tabs( { disabled: [1] } );
	    
	  });
});
</script>
<script> 
$j('#create').submit(function (event){
	 $j("#dialog-message").html("<img src=https://sunvalleyfilmfestival.org/ecommerce/img/animation_processing.gif>");
	 
	$( "#dialog-message" ).dialog({
	      modal: true,
	      height:"auto",
	      width:"auto",
	      buttons: {
	        Ok: function() {
	        	
	        	console.log("ok clicked");
	          $( this ).dialog( "close" );
	        }
	      }
	    });
	event.preventDefault();
	console.log("creating data file");	
	
	$j.ajax({
        url: "http://localhost:8080/openmrs/module/machinelearning/createip.form",
        data: {},
        success: function(data)
        {
        	 $j("#dialog-message").html("Process Completed.");
        	 $( "#tabs" ).tabs( "option", "active", 1 );
        	 console.log("step 1");
        	 
        	 $j.ajax({
        		
        	      url: "http://localhost:8080/openmrs/module/machinelearning/structuredata.form",
        	      data: {},
        	      success: function(data)
        	      {
        	      		console.log("step 2");
        	      		// enable and switch
        	      		
        	      		$( "#tabs" ).tabs("enable",1);
        	      		$( "#tabs" ).tabs( "option", "active", 1 );
        	      }
   	      		});
        	      		        	              	
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

<script>
$j('#runmodel').submit(function (event)
{
  event.preventDefault();
  console.log("run");	
  
  $j.ajax({
      url: "http://localhost:8080/openmrs/module/machinelearning/runmodel.form",
      data: {},
      success: function(data)
      {
      		console.log("step3");
      		
      		
      
      }
      });
  
  
	
});

</script>



<%@ include file="/WEB-INF/template/footer.jsp"%>