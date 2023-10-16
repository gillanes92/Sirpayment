
<!DOCTYPE html>
<html id="html">
<head>
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
<script src="resources/js/inscribir.js?5cffb14a4e04b"></script>


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
								<h2>Inscripción</h2>
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
								<div class="col-md-6">
									<div class="fila">
										<p>
											<label for="email">Correo electrónico</label>
										</p>
										<input type="email" id="email" name="email"
											class="form-control" placeholder="hola@mail.com" tabindex="6"
											maxlength="35">
									</div>
								</div>
							</div>
						</form>
					</div>
					<br>
					<div id="envia" class="form-group col-md-12" align="center"
						style="margin-top: 10px">
						
						<button id="enviar" class="btn_purple">ENVIAR</button>
						<form id="form-detalle" method="post">
							<input type="hidden" id="ident" name="ident" style="display: none" />
							<input type="hidden" id="corr" name="corr" style="display: none" />
						</form>
					</div>
				</div>
			</div>
		</div>
	</div>

</body>
</html>