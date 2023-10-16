$(document).ready(function() {


    $("#buscar").on('click', function(e) {
        e.preventDefault();
        
        var identificador = $("#identificador").val();
				
		$.ajax({
            type: 'POST',
            url: 'servicio_ajax.htm',
            data: 'method=buscarIdentificador&identificador=' + identificador,
            success: function(data) {
                var result = jQuery.parseJSON(data);
                console.log(result);
                
                if(result.code != '404'){
                
                	
                
                }else{
                
                	
                	
                }
                
            },
            error: function(xhr, status, errorThrown) {
            
            }
        });
    });


});