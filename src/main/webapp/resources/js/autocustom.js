$(document).ready(function () {
    
	  $('#w-input-search').autocomplete({
		  minLength: 2,
		  source: function (request, response) {
		         $.ajax({
		             url: myContextPath+"/GetDocuments",
		             data: { name: request.term },
		             dataType: "json",
		             success: function (data) {
		                    response($.map(data, function (item) {
		                        return {
		                            label: item.title,
		                            value: item.title
		                            // id:item.num
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
		            window.location.href = myContextPath+"/GetSimilarDocuments?title=" + ui.item.label;
		        }
		});
});