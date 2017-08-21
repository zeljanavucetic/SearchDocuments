$(document).ready(function () {
    
    var url = window.location.href;
    var params = url.split('?');

    //alert(params[1]);
    
	  $('#w-input-search').autocomplete({
		  minLength: 2,
		  source: function (request, response) {
		         $.ajax({
		             url: myContextPath+"/GetDocuments?collection1=" + params[1],
		             data: { name: request.term },
		             dataType: "json",
		             success: function (data) {
		                    response($.map(data, function (item) {
		                        return {
		                            label: item.title,
		                            value: item.title
		                            //id: item._id
		                        }
		                    }));
		                },
		             error: function () {
		            	 alert("ERROR!");
		             }
		         });
		     },
		        select: function (event, ui) {
		             alert(ui.item.label);
		            window.location.href = myContextPath+"/GetSimilarDocuments?title=" + ui.item.label + "&collection2=" + params[2];
		        }
		});
});