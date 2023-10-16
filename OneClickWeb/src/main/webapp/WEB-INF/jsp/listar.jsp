
<!DOCTYPE html>
<html>
<head>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>OneClick Generico</title>
<link type="image/x-icon" href="resources/images/logo_chile.png"
	rel="icon" />
<link rel="shortcut icon" href="resources/images/logo_chile.png" />
<link rel="stylesheet" href="resources/css/bootstrap.min.css">
<link rel="stylesheet" href="resources/css/style.css">

<script src="resources/js/jquery-3.3.1.min.js"></script>
<script src="resources/js/jquery.mask.min.js"></script>
<script src="resources/js/rut.min.js"></script>


</head>
<body>
	<div id="landing_bolsas">
		<div class="container" style="background: lime;">
			<ul class="navbar">
				<li><a href="listar.htm">Listar</a>
				<li><a href="inscribir.htm">Inscribir</a>
				<li><a href="pagar.htm">Pagar</a>
			</ul>
		</div>
		<div class="contenido">
			<div class="container">
				<div class="row justify-content-center">
					<div class="col-lg-10">
						<div class="row bloque">
							<div class="col">
								<h2>Listar Tarjetas</h2>
							</div>
						</div>
						<form id="form-boleta" action="" novalidate="novalidate">
							<div class="row">
								<div class="col-md-6">
									<div class="fila">
										<p>
											<label for="email">Identificador</label>
										</p>
										<input type="text" id="identificador" name="identificador"
											class="form-control" placeholder="">
									</div>
								</div>
								<button id="buscar" class="btn_purple" type="submit">BUSCAR</button>
								<input type="hidden" id="busca" name="busca"
									style="display: none" />
							</div>
						</form>
					</div>
					<br>
					<div class="col-lg-12" style="margin-top: 20px;">
						<div class="row justify-content-center">
							<h2>${texto}</h2>
						</div>
					</div>
					<br>
					<c:forEach items="${cards}" var="card">
						<div class="card" style="margin-top: 20px;">
							<div class="row">
								<div class="col-md-4">
									<img class="card-img-top" src="resources/images/${card.cardType}.png"
										alt="Card image cap">
								</div>
								<div class="col-md-8">
									<div class="card-body">
										<h5 class="card-title">Authortization Code: ${card.authorizationCode}</h5>
										<h5 class="card-title">Cart Type: ${card.cardType}</h5>
										<h5 class="card-title">Card Number: ${card.cardNumber}</h5>
										<h5 class="card-title">tbkUser: ${card.tbkUser}</h5>
									</div>
								</div>
							</div>
						</div>
					</c:forEach>
				</div>
			</div>
		</div>
	</div>

	</div>

</body>
</html>