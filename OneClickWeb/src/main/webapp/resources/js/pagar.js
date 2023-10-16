$(document).ready( function() {

	$("#pagar").click(function (e) {
	
		var identificador = $("#identificador_aux").val();

		var radios = $("input#radiogroup");
		
		var tbkuser = '';
			
		for(var c = 0; c < radios.length; c++){
			if(radios[c].checked == true){
				tbkuser=radios[c].value;
			}
		}
		
		var producto1 = $("#producto1").val();
		var monto1 = $("#monto1").val();
		var producto2 = $("#producto2").val();
		var monto2 = $("#monto2").val();
		
		
		if(/^\s*$/.test(tbkuser)){
			alert("Selecciona una tarjeta")	
			return
		}
		
		
		if(producto1 === '' && monto1 === '' && producto2 === '' && monto2 === ''){
			alert("Debe ingresar un producto y monto");	
			return
		}
		
		if((producto1 != '' && monto1 === '') || (producto2 != '' && monto2 === '')){
			alert("Debe ingresar bien el producto y su monto");	
			return
		}
		
		if((producto1 === '' && monto1 != '') || (producto2 === '' && monto2 != '')){
			alert("Debe ingresar bien el producto y su monto");	
			return
		}
		
		
		var detallePagos = new Array();
		
		var montototal = 0;
		
		if(producto1 != '' && monto1 != ''){
			var detalle = new Object();
			detalle.monto = monto1;
			detalle.commerceCode = '597055555542';
			detalle.orderTransaction = producto1+monto1;
			detallePagos.push(detalle);
			
			montototal = montototal+parseInt(monto1);
		}
		
		if(producto2 != '' && monto2 != ''){
			var detalle = new Object();
			detalle.monto = monto2;
			detalle.commerceCode = '597055555543';
			detalle.orderTransaction = producto2+monto2;
			detallePagos.push(detalle);
			
			montototal = montototal+parseInt(monto2);
		} 
		 
		var miObjeto = new Object();
		miObjeto.tbkUser = tbkuser;
		miObjeto.identificador = identificador;
		miObjeto.montoTotal = montototal+'.0';
		miObjeto.cuotas = '0';
		miObjeto.detallePagos = detallePagos;
		 
		console.log(miObjeto); 
		 
		
		const xhr = new XMLHttpRequest();

		xhr.onload = function (data) {
			
			var pago = JSON.parse(xhr.responseText);
			
			var texto = '';
			
			for(var i = 0; i < pago.Details.length; i++){
			
				texto = texto + '<h3 style="margin-top: 20px;">PAGO '+(i+1)+' '+pago.Details[i].Status+'</h3>';
			
			}
			
			document.getElementById("textopagado1").innerHTML = texto;
		
        };

		xhr.open('POST', 'http://35.87.226.34/oneClickGenerico/OneClick/pagar');
	
		xhr.setRequestHeader('Content-Type', 'application/json');
			
		xhr.send(JSON.stringify(miObjeto)); 
		 
		 
		 
		 
		/* 
		$.ajax({
				type : 'POST',
				url : 'http://35.87.226.34/oneClickGenerico/OneClick/pagar',
				contentType:'application/json',
				data : JSON.stringify(miObjeto),
				dataType : 'json',
				success : function(data) {
					alert(data);
							
				},
				error : function(xhr, status, errorThrown) {
				}
			});
		*/
		
		
	});


});