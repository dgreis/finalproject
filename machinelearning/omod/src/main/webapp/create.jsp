<%@ include file="/WEB-INF/template/include.jsp"%>
<%@ include file="/WEB-INF/template/header.jsp"%>
<head>

<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
 <script src="http://code.jquery.com/jquery-1.10.2.js"></script>
  <script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
 
  
<style>
  
.ui-dialog-titlebar-close {
  visibility: hidden;
} 

a#infowindow1 
{
margin-left: 960px;
}

a#infowindow2 
{
margin-left: 960px;
}
  
</style>
</head>
<%@ include file="template/localHeader.jsp"%>

<div id="tabs">
  <ul>
    <li><a href="#tabs-1">Step One</a></li>
    <li><a href="#tabs-2">Step Two</a></li>
    
  </ul>
  <div id="tabs-1">
    <p>Hello ${user.systemId}! This wizard will install or update the prediction engine as needed.</p>
<form id ="create" method="get" action="">
<p>Click the 'Start' button below to begin. This step will create files needed in Step Two. The process will take a minute or two.</p>
<input type="submit" value="Start">
</form>
<a id="infowindow1" href="#"  > Click to learn more</a>
  </div>
  <div id="tabs-2">
    <p>Click the button below to finish Diagknowzit's update/installation process. After it completes, you're good to go!</p>
    <form id ="runmodel" method="get" action="">
    <input type="submit" value="Start">
    </form>
    
    <a id="infowindow2" href="#"  > Click to learn more</a>
  </div>
 
</div>




<script>
$j(document).ready(function(){
	
	$j('h2').text("Diagknowzit Module Management");
	
	var a = $j("<div>"+""+"</div>");
    a.attr("id","dialog-message");
    var b = $j("<div>"+""+"</div>");
    b.attr("id","dialog-message1");
    var c = $j("<div>"+""+"</div>");
    c.attr("id","dialog-message2");
    
    $j( "body" ).append(a);
    $j( "body" ).append(b);
    $j( "body" ).append(c);
    
    $j("#dialog-message1").html("In this step, Diagnknowzit is pulling data<br>from the Open-MRS database and preparing it<br>to be read by the machine learning algorithm.");
    $j("#dialog-message2").html("In this step, Diagknowzit is training the<br>machine learning algorithm on the data prepared<br>in Step One. Diagknowzit is powered by<br>multinomial logistic regression.");
    
    
   
	console.log("ready");
	
	// enable tabs
	$(function() {
	    $( "#tabs" ).tabs();
	    // disable the second tab
	    $( "#tabs" ).tabs( { disabled: [1] } );
	    
	  });
	
	$j('#infowindow1').click(function()
	{
		 $( "#dialog-message1" ).dialog({
		      modal: true,
		      open: function(event,ui){
		    	console.log(ui);  
		      },
		     // min-height:300,
		     // min-width:300,
		      height:"auto",
		      width:"auto",
		      buttons: {
		        Ok: function() 
		        {
		        	
		        	console.log("ok clicked");
		          $( this ).dialog( "close" );
		        }
		      }
		    });
		
		 $j('#ui-id-3').html("Information");
		console.log("click");
	});
	
	$j('#infowindow2').click(function(){
		$( "#dialog-message2" ).dialog({
		      modal: true,
		      open: function(event,ui){
		    	console.log(ui);  
		      },
		     // min-height:300,
		     // min-width:300,
		      height:"auto",
		      width:"auto",
		      buttons: {
		        Ok: function() 
		        {
		        	
		        	console.log("ok clicked");
		          $( this ).dialog( "close" );
		        }
		      }
		    });
		
		 $j('#ui-id-5').html("Information");
		console.log("click");
	});
	
});
</script>
<script> 
$j('#create').submit(function (event)
	{
	
	$j("#dialog-message").html("<img src=https://sunvalleyfilmfestival.org/ecommerce/img/animation_processing.gif>");
	 //$j("#dialog-message").html("checking for cross !");
	
	
	 $( "#dialog-message" ).dialog({
	      modal: true,
	      open: function(event,ui){
	    	console.log(ui);  
	      },
	     // min-height:300,
	     // min-width:300,
	      height:"auto",
	      width:"auto",
	      buttons: {
	        "Close": function() 
	        {
	        	
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
        	      },
        	      error:function(objAJAXRequest, strError )
	  		        {
	  		        	//alert("i am in the error section");
	  		        	 $j("#dialog-message").html("Something went wrong ! contact your adminstrator !");
	          			console.log(strError);
	  		        }
   	      		
        	 });
        	      		        	              	
        },
        dataType: "html",
        timeout: 200000,
        error: function(objAJAXRequest, strError )
		        {
		        	//alert("i am in the error section");
		        	 $j("#dialog-message").html("Something went wrong ! contact your adminstrator !");
        			console.log(strError);
		        }
      });
	
	console.log("submit called");
});
</script>

<script>
function tryagain1()
{
	var delay=15000;//1 seconds
    setTimeout(function(){
    	$j.ajax({
  	      url: "http://localhost:8080/openmrs/module/machinelearning/runmodel.form",
  	      data: {},
  	      success: function(data)
  	      {
  	      		console.log("recursive call");
  	      		$j("#dialog-message").html("Process Completed.");
  	      		//return;
  	      },
  	      error: function(objAJAXRequest, strError )
  	      {
  	        //	tryagain1();
  	    	    //alert("i am in the error section");
  	    	    $j("#dialog-message").html("Something went wrong with the building of the model. Please try again or contact the system administrator");
  				console.log("trying again 1");
  	    	    console.log(strError);
  	      }
  	      
  	      });
    //your code to be executed after 1 seconds
    },delay); 
	
		
	
}

</script>


<script>
function tryagain()
{
	var delay=15000;//15 second delay seconds
    setTimeout(function(){
    	$j.ajax({
  	      url: "http://localhost:8080/openmrs/module/machinelearning/runmodel.form",
  	      data: {},
  	      success: function(data)
  	      {
  	      		console.log("recursive call");
  	      		$j("#dialog-message").html("Process Completed.");
  	      		return;
  	      },
  	      error: function(objAJAXRequest, strError )
  	      {
  	        	tryagain1();
  	    	    //alert("i am in the error section");
  				console.log("trying again");
  	    	    console.log(strError);
  	      }
  	      
  	      });
    //your code to be executed after 1 seconds
    },delay); 
	
		
	
}

</script>

<script>
$j('#runmodel').submit(function (event)
{
  event.preventDefault();
  console.log("run");	
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
  
  $j.ajax({
      url: "http://localhost:8080/openmrs/module/machinelearning/runmodel.form",
      data: {},
      success: function(data)
      {
      		console.log("step3");
      		$j("#dialog-message").html("Process Completed.");
      },
      error: function(objAJAXRequest, strError )
      {
    	  console.log("failed first time , trying again");
    	  tryagain();
    	   
			console.log(strError);
      }
      
      });
  
  
	
});

</script>

<script>


</script>

<%@ include file="/WEB-INF/template/footer.jsp"%>