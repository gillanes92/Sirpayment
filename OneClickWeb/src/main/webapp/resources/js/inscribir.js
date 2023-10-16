$(document).ready(function() {


    $("#enviar").on('click', function(e) {
        e.preventDefault();
        
        var identificador = $("#identificador").val();
        var email = $("#email").val();
		
		document.getElementById("ident").value = identificador;
		document.getElementById("corr").value = email;
		$('#form-detalle').attr('action', 'redirect.htm');
		$('#form-detalle').submit();
		
		/*		
		var miObjeto = new Object();
		miObjeto.nombre = identificador;
		miObjeto.correo = email;
		
		
		const xhr = new XMLHttpRequest();

		xhr.onload = function (data) {
            document.getElementById("html").innerHTML = xhr.responseText;
            document.getElementById("f").submit();
        };

		xhr.open('POST', 'http://35.87.226.34/oneClickGenerico/OneClick/createInscription');
	
		xhr.setRequestHeader('Content-Type', 'application/json');
			
		xhr.send(JSON.stringify(miObjeto));
		*/		
    });


});