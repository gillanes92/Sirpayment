<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Comprobante de pago electr&oacute;nico</title>
<script type='text/javascript' src="<c:url value='/resources/js/jquery-3.3.1.min.js' />"></script>
<script type='text/javascript'>
	
	$(document).ready(function() {
		var i;
		segundos(15);
		setTimeout(function() {
			segundos(14);
		}, 1000);
		setTimeout(function() {
			segundos(13);
		}, 2000);
		setTimeout(function() {
			segundos(12);
		}, 3000);
		setTimeout(function() {
			segundos(11);
		}, 4000);
		setTimeout(function() {
			segundos(10);
		}, 5000);
		setTimeout(function() {
			segundos(9);
		}, 6000);
		setTimeout(function() {
			segundos(8);
		}, 7000);
		setTimeout(function() {
			segundos(7);
		}, 8000);
		setTimeout(function() {
			segundos(6);
		}, 9000);
		setTimeout(function() {
			segundos(5);
		}, 10000);
		setTimeout(function() {
			segundos(4);
		}, 11000);
		setTimeout(function() {
			segundos(3);
		}, 12000);
		setTimeout(function() {
			segundos(2);
		}, 13000);
		setTimeout(function() {
			segundos(1);
		}, 14000);
		setTimeout(function() {
			$(".second").fadeOut(500);
			var href = '<%= request.getAttribute("url_response") %>';

			if (window.opener != null) {
				window.opener.location.href = href;
				window.close();
			} else {
				window.location.href = href;
			}

			e.preventDefault();

		}, 15000);
	});

	function segundos(x) {
		$("#segundos").text(x);
	}
</script>
</head>


<body bgcolor='#EBEBEB' leftmargin='0' topmargin='0' marginwidth=' 0' marginheight=' 0'>
	<table id='enel-comprobante-pago' width=' 612px ' border='0' cellpadding='0' cellspacing='0' bgcolor='#FFFFFF'>
		<tbody>

			<tr>
				<td colspan='8'>
					<div class="second">
						En los próximos <span id="segundos">15</span> segundos, será redireccionado al sitio para finalizar la transacción!&nbsp; 
						<a href="<%= request.getAttribute("url_response") %>">Siguiente</a>
					</div>
				</td>
			</tr>


			<tr class='logo-container'>
				<td colspan='5' height=' 100px '>
					<img src="<%= request.getAttribute("url_img_collector") %>" alt="enel" style="display: inline-block; margin: 10px 20px; height: 60px;" />
				</td>
				<td colspan='3' class='fecha-pago-container' height=' 100px '>
					<div class='fecha-hora-titulo'>Fecha y hora de pago</div>
					<div class='fecha-hora-pago texto-normal'>
						<span><%= request.getAttribute("fecha_pago_recaudador") %></span>
					</div>
				</td>
			</tr>
			<tr>
				<td colspan='8' height=' 80px ' class='titulo'><%= request.getAttribute("titulo") %></td>
			</tr>
			<tr>
				<td colspan='8' height=' 68px ' class='estimado-usuario'><strong>Estimado(a)
						<span class='username'><%= request.getAttribute("nombre") %>,</span>
				</strong> <br> <span class='texto-normal'><%= request.getAttribute("mensaje") %> </span></td>
			</tr>

			<tr>
				<td colspan='2' height=' 60px ' class='atributos_izq'>
					<strong>Monto</strong>
					<br> 
					<span class='texto-normal'>
						<%= request.getAttribute("currency_symbol") %>&nbsp; <c:set var="price" value='<%= request.getAttribute("monto_pagado") %>' /> 
						<fmt:setLocale value="es_CL" /> 
						<fmt:formatNumber value="${price}" pattern="#,##0" minFractionDigits="0" maxFractionDigits="2" />
					</span>
				</td>
				<td colspan='6' height=' 60px ' class='atributos_der'>
					<strong>N° de Transacción</strong>
					<br>
					<span class='texto-normal'><%= request.getAttribute("num_transaccion") %></span>
				</td>

			</tr>

			<tr>
				<td colspan='2' height=' 78px ' class='atributos_izq'><strong>Medio
						de pago</strong> <br> <span class='texto-normal'><%= request.getAttribute("medio_de_pago") %></span></td>
				<td colspan='6' height=' 78px ' class='atributos_der'><strong>Observaciones</strong>
					<br> <span class='texto-normal'><%= request.getAttribute("observaciones") %></span></td>
			</tr>
			<!-- 
			<tr>
				<td colspan='8' class='guardar-comprobante'>
					<strong>Te recomendamos guardar una copia de este comprobante</strong>&nbsp; 
					<br>
					<span class='texto-normal'> También puedes acceder a 
						<a href='#' target='_blank'>Mi Enel</a> para ver tus históricos de pagos.
					</span>
					<br>
					<span class='texto-normal'> <%= request.getAttribute("info_adicional1") %> </span>
					<br>
					<span class='texto-normal'> <%= request.getAttribute("info_adicional3") %> </span>
					<br>
					<span class='texto-normal'> <%= request.getAttribute("info_adicional5") %> </span>
				</td>
			</tr>
	
			<tr>
				<td colspan='8'><a class="fullwidth"
					href="<%= request.getAttribute("url_desc_comprobante") %>"
					target="_blank" id="descargar">Descargar Comprobante</a></td>
			</tr>
			 -->
			<tr>
				<td width=' 140px ' height=' 1px '></td>
				<td width=' 140px ' height=' 1px '></td>
				<td width=' 26px ' height=' 1px '></td>
				<td width=' 34px ' height=' 1px '></td>
				<td width=' 85px ' height=' 1px '></td>
				<td width=' 13px ' height=' 1px '></td>
				<td width=' 9px ' height=' 1px '></td>
				<td width=' 165px ' height=' 1px '></td>
			</tr>
		</tbody>
	</table>
</body>

</html>
<style>
.fullwidth {
	width: 100%;
	/* display: block; */
	margin-left: 0;
	margin-right: 0;
	box-sizing: border-box;
	color: white;
	padding: 15px 33px;
	/* margin: 25px; */
	background: red;
	display: block;
	text-align: center;
	text-decoration: none;
	border-color: #ff0f64;
	color: #fff;
	background-color: #ff0f64;
	text-transform: uppercase;
}

.fullwidth:hover {
	background-color: #000;
	color: #fff;
	border-color: #000;
}

table#enel-comprobante-pago {
	font-family: Helvetica, Arial, sans-serif;
	font-weight: 400;
	font-size: 14px;
	line-height: 20px;
	border-spacing: 0;
	margin: 32px auto 32px auto;
	background-image: url(< c : url value = '/resources/images/sello-enel.gif'/ >);
	background-size: 612px 300px;
	background-repeat: no-repeat;
}

.fecha-hora-titulo {
	font-weight: 600;
	color: #000;
}

td.fecha-pago-container {
	font-size: 14px;
}

td.fecha-pago-container div {
	margin-left: 16px;
}

td.titulo {
	padding: 12px 0 0 28px;
	font-size: 18px;
	font-weight: bold;
}

td.estimado-usuario {
	padding: 0px 28px 14px 28px;
}

tr.logo-container td {
	border-bottom: 1px solid #f2f2f2;
}

td.atributos_izq {
	padding: 0px 28px 2px 28px;
	vertical-align: top;
}

td.atributos_der {
	padding: 0 28px 8px 28px;
	vertical-align: top;
}

td.detalle_pedido {
	padding: 0px 28px 2px 28px;
	border-top: 1px solid #f2f2f2;
	vertical-align: top;
}

.texto-normal {
	color: #4A4A4A;
}

td.guardar-comprobante {
	padding: 18px 28px 18px 28px;
	border-top: 2px solid #f2f2f2;
	vertical-align: top;
}

td.guardar-comprobante a {
	color: #0756FA;
}

td.descargar-app {
	background-color: #F7F7F7;
	font-size: 16px;
	line-height: 25px;
	color: #222222;
	padding: 4px 28px 0 28px;
}

td.app-store-badge {
	background-color: #F7F7F7;
	padding: 2px 0 0 24px;
}

td.google-play-badge {
	background-color: #F7F7F7;
	padding: 2px 0 0 4px;
}

td.app-store-badge a, td.google-play-badge a {
	text-decoration: none;
}

td.fono-servicio {
	padding: 10px 14px 0 28px;
	border-bottom: 1px solid #373737;
}

tr.footer-links, tr.footer-social-media {
	background-color: #222222;
	color: #FFFFFF;
}

tr.footer-links a {
	color: #FFFFFF;
}

.texto-normal-white {
	color: #757575;
}

.texto-disclaimer {
	color: #4A4A4A;
	font-style: oblique;
	font-size: 12px;
}

td.puntos-enel {
	padding: 10px 14px 0 16px;
	border-bottom: 1px solid #373737;
}

td.oficina-movil {
	padding: 10px 14px 0 12px;
	border-bottom: 1px solid #373737;
}

td.contacto-correo {
	padding: 10px 28px 0 8px;
	border-bottom: 1px solid #373737;
}

td.siguenos {
	padding: 6px 14px 0 28px;
}

td.copyright {
	padding: 6px 28px 0 14px;
	text-align: right;
}

td.social-media-img {
	padding: 10px 0 0 5px;
}

.social-media-img a {
	text-decoration: none;
}

.social-media-img a ~a:not(:first-child){
	margin-left:16px;
}
.second {
	width: 100%;
	padding: 0px;
	background-color: #CCCCCC;
	text-align: center;
}
</style>