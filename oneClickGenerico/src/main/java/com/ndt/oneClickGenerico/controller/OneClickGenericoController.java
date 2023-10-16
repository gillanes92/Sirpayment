package com.ndt.oneClickGenerico.controller;

import com.ndt.oneClickGenerico.dao.impl.CarritoDaoImpl;
import com.ndt.oneClickGenerico.model.ConsultTransactionRequestModel;
import com.ndt.oneClickGenerico.model.CreateInscriptionRequestModel;
import com.ndt.oneClickGenerico.model.PagoRequestModel;
import com.ndt.oneClickGenerico.model.PagosMasivosRequestModel;
import com.ndt.oneClickGenerico.model.DeleteInscriptionRequestModel;
import com.ndt.oneClickGenerico.model.GetCardsRequestModel;
import com.ndt.oneClickGenerico.model.PagoAllRequestModel;

import cl.transbank.common.IntegrationApiKeys;
import cl.transbank.common.IntegrationCommerceCodes;
import cl.transbank.common.IntegrationType;
import cl.transbank.model.CardDetail;
import cl.transbank.webpay.common.WebpayOptions;
import cl.transbank.webpay.exception.InscriptionDeleteException;
import cl.transbank.webpay.exception.InscriptionStartException;
import cl.transbank.webpay.exception.TransactionAuthorizeException;
import cl.transbank.webpay.oneclick.Oneclick;
import cl.transbank.webpay.oneclick.model.MallTransactionCreateDetails;
import cl.transbank.webpay.oneclick.responses.OneclickMallInscriptionFinishResponse;
import cl.transbank.webpay.oneclick.responses.OneclickMallInscriptionStartResponse;
import cl.transbank.webpay.oneclick.responses.OneclickMallTransactionAuthorizeResponse;
import cl.transbank.webpay.oneclick.responses.OneclickMallTransactionAuthorizeResponse.Detail;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ArrayList;
import java.util.Date;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping({ "/Tokenized/**" })
public class OneClickGenericoController {
	private static final Logger logger = LoggerFactory.getLogger(OneClickGenericoController.class);

	@POST
	@RequestMapping(value = { "/GetTokenSIR" })
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	@Produces({ MediaType.APPLICATION_JSON + ";charset=iso-8859-1"})
	public ResponseEntity<?> getTokenSIR(@RequestParam(value = "grant_type", required = true) String grant_type,
			@RequestParam(value = "client_id", required = true) String client_id,
			@RequestParam(value = "client_secret", required = true) String client_secret) {

		logger.info("ENTRANDO A SIRPAYMENT GETTOKENSIR");

		logger.info(grant_type + " - " + client_id + " - " + client_secret);

		try {

			CarritoDaoImpl carritoDao = new CarritoDaoImpl();
			InitialContext initi = null;
			try {
				initi = new InitialContext();
			} catch (NamingException e2) {
				e2.printStackTrace();
			}
			DataSource dataSourceBTN = null;
			try {
				dataSourceBTN = (DataSource) initi.lookup("java:/SIR");
			} catch (NamingException e1) {
				e1.printStackTrace();
			}
			carritoDao.setDataSourceBTN(dataSourceBTN);

			JSONObject parametros_entrada = new JSONObject();
			parametros_entrada.put("grant_type", grant_type);
			parametros_entrada.put("client_id", client_id);
			parametros_entrada.put("client_secret", client_secret);

			Map<String, Object> dejarRegistro = carritoDao.dejarRegistro(0, "GETTOKENSIR",
					parametros_entrada.toString(), "");

			Integer idRegistro = dejarRegistro.get("out_cod_retorno").toString().equals("00")
					? Integer.valueOf(dejarRegistro.get("out_desc_retorno").toString())
					: 0;

			if ((grant_type.isEmpty() || client_id.isEmpty() || client_secret.isEmpty())
					|| !grant_type.equals("client_credentials")) {
				return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
			}

			Map<String, Object> validaEmpresa = carritoDao.validarEmpresa(client_id, client_secret);

			if (validaEmpresa.get("out_cod_retorno").toString().equals("00")) {

				String properties = "oneClickGenerico.properties";
				String secret_key = getPropertie("secret_key", properties);
				String jwt = Jwts.builder().signWith(SignatureAlgorithm.HS512, secret_key.getBytes())
						.setIssuedAt(new Date()).claim("client_id", client_id).claim("client_secret", client_secret)
						.compact();

				System.out.println(jwt);

				JSONObject json = new JSONObject();
				json.put("token_type", "Bearer");
				json.put("access_token", jwt);

				carritoDao.dejarRegistro(idRegistro, "GETTOKENSIR", parametros_entrada.toString(), json.toString());

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(new org.springframework.http.MediaType("application", "json", Charset.forName("UTF-8")));
				return new ResponseEntity<Object>(json.toString(), headers, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new org.springframework.http.MediaType("application", "json", Charset.forName("UTF-8")));
			return new ResponseEntity<Object>(headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GET
	@RequestMapping(value = { "/GetCards" })
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public ResponseEntity<?> getCards(@RequestBody GetCardsRequestModel getCardsRequestModel,
			@RequestHeader("Authorization") String authorization) {

		logger.info("ENTRANDO A SIRPAYMENT GETCARDS");

		logger.info(getCardsRequestModel.toString() + " - " + authorization);

		JSONObject json = new JSONObject();
		try {

			CarritoDaoImpl carritoDao = new CarritoDaoImpl();
			InitialContext initi = null;
			try {
				initi = new InitialContext();
			} catch (NamingException e2) {
				e2.printStackTrace();
			}
			DataSource dataSourceBTN = null;
			try {
				dataSourceBTN = (DataSource) initi.lookup("java:/SIR");
			} catch (NamingException e1) {
				e1.printStackTrace();
			}
			carritoDao.setDataSourceBTN(dataSourceBTN);

			JSONObject parametros_entrada = new JSONObject();
			parametros_entrada.put("body", getCardsRequestModel.toString());
			parametros_entrada.put("header", authorization);

			Map<String, Object> dejarRegistro = carritoDao.dejarRegistro(0, "GETCARDS", parametros_entrada.toString(),
					"");

			Integer idRegistro = dejarRegistro.get("out_cod_retorno").toString().equals("00")
					? Integer.valueOf(dejarRegistro.get("out_desc_retorno").toString())
					: 0;

			String[] auth = authorization.split("\\s+");

			String token_type = auth[0];

			if (!token_type.equals("Bearer")) {
				return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
			}

			String access_token = auth[1];

			String properties = "oneClickGenerico.properties";
			String secret_key = getPropertie("secret_key", properties);

			logger.info("verificando token");

			Claims claims = Jwts.parser().setSigningKey(secret_key.getBytes()).parseClaimsJws(access_token).getBody();

			logger.info(claims.toString());

			if (claims.get("client_id") != null || claims.get("client_secret") != null) {

				if (!claims.get("client_id").equals(getCardsRequestModel.getId_company())
						|| !claims.get("client_secret").equals(getCardsRequestModel.getCredential_id_company())) {
					return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
				}

				if (!getCardsRequestModel.getId_company().equals("")
						&& !getCardsRequestModel.getCredential_id_company().equals("")
						&& !getCardsRequestModel.getId_client().equals("")
						&& !getCardsRequestModel.getEmail().equals("")) {

					Map<String, Object> obtenerCards = carritoDao.obtenerCards(getCardsRequestModel);

					json.put("code", obtenerCards.get("out_cod_retorno").toString());
					json.put("identificador", getCardsRequestModel.getId_client());

					HttpStatus codeHttp = null;

					if (obtenerCards.get("out_cod_retorno").toString().equals("00")) {
						json.put("cards", new JSONArray(obtenerCards.get("out_desc_retorno").toString()));
						json.put("description", "OK");

						codeHttp = HttpStatus.OK;
					}

					if (obtenerCards.get("out_cod_retorno").toString().equals("400")) {
						json.put("description", obtenerCards.get("out_desc_retorno").toString());

						codeHttp = HttpStatus.BAD_REQUEST;
					}

					carritoDao.dejarRegistro(idRegistro, "GETCARDS", parametros_entrada.toString(), json.toString());

					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(new org.springframework.http.MediaType("application", "json", Charset.forName("UTF-8")));
					return new ResponseEntity<Object>(json.toString(), headers, codeHttp);

				} else {

					json.put("code", "400");
					json.put("identificador", getCardsRequestModel.getId_client());
					json.put("cards", new JSONArray());
					json.put("description", "SE DEBE MANDAR TODOS LOS VALORES");

					carritoDao.dejarRegistro(idRegistro, "GETCARDS", parametros_entrada.toString(), json.toString());

					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(new org.springframework.http.MediaType("application", "json", Charset.forName("UTF-8")));
					return new ResponseEntity<Object>(json.toString(), headers, HttpStatus.BAD_REQUEST);

				}
			} else {
				return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
			}

		} catch (SQLException e) {
			// TODO: handle exception
			json.put("code", "404");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new org.springframework.http.MediaType("application", "json", Charset.forName("UTF-8")));
			return new ResponseEntity<Object>(json.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (SignatureException | ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
			// TODO: handle exception
			return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
		}

	}

	@POST
	@RequestMapping(value = { "/CreateInscription" })
	@Consumes({ org.springframework.http.MediaType.APPLICATION_JSON_VALUE })
	public void createInscription(@RequestBody CreateInscriptionRequestModel createInscriptionRequestModel,
			@RequestHeader("Authorization") String authorization, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		logger.info("ENTRANDO A SIRPAYMENT CREATEINSCRIPTION");

		logger.info(createInscriptionRequestModel.toString());

		try {

			CarritoDaoImpl carritoDao = new CarritoDaoImpl();
			InitialContext initi = null;
			try {
				initi = new InitialContext();
			} catch (NamingException e2) {
				e2.printStackTrace();
			}
			DataSource dataSourceBTN = null;
			try {
				dataSourceBTN = (DataSource) initi.lookup("java:/SIR");
			} catch (NamingException e1) {
				e1.printStackTrace();
			}
			carritoDao.setDataSourceBTN(dataSourceBTN);

			JSONObject parametros_entrada = new JSONObject();
			parametros_entrada.put("body", createInscriptionRequestModel.toString());
			parametros_entrada.put("header", authorization);

			Map<String, Object> dejarRegistro = carritoDao.dejarRegistro(0, "CREATEINSCRIPTION",
					parametros_entrada.toString(), "");

			Integer idRegistro = dejarRegistro.get("out_cod_retorno").toString().equals("00")
					? Integer.valueOf(dejarRegistro.get("out_desc_retorno").toString())
					: 0;

			String[] auth = authorization.split("\\s+");

			String token_type = auth[0];

			if (token_type.equals("Bearer")) {

				String access_token = auth[1];

				String properties = "oneClickGenerico.properties";
				String secret_key = getPropertie("secret_key", properties);

				logger.info("verificando token");

				Claims claims = Jwts.parser().setSigningKey(secret_key.getBytes()).parseClaimsJws(access_token)
						.getBody();

				logger.info(claims.toString());

				if (claims.get("client_id") != null || claims.get("client_secret") != null) {

					if (claims.get("client_id").equals(createInscriptionRequestModel.getId_company()) && claims
							.get("client_secret").equals(createInscriptionRequestModel.getCredential_id_company())) {

						Map<String, Object> validaEmpresa = carritoDao.validarEmpresa(
								createInscriptionRequestModel.getId_company(),
								createInscriptionRequestModel.getCredential_id_company());

						if (validaEmpresa.get("out_cod_retorno").toString().equals("00")) {

							String cod_comercio_empresa = validaEmpresa.get("out_desc_retorno").toString();

							Map<String, Object> insertIdent = carritoDao
									.insertIdentificador(createInscriptionRequestModel);

							if (insertIdent.get("out_cod_retorno").toString().equals("00")
									|| (insertIdent.get("out_cod_retorno").toString().equals("01") && insertIdent
											.get("out_desc_retorno").toString().contains("duplicate key"))) {

								logger.info("inscribiendo identificador");
								String username = createInscriptionRequestModel.getId_client();

								String email = createInscriptionRequestModel.getEmail();

								String response_url = getPropertie("urlResponseInscription", properties);

								Oneclick.MallInscription inscription = new Oneclick.MallInscription(new WebpayOptions(
										cod_comercio_empresa, IntegrationApiKeys.WEBPAY, IntegrationType.TEST));
								final OneclickMallInscriptionStartResponse resp = inscription.start(username, email,
										response_url + "?PARAMS=" + createInscriptionRequestModel.getId_company() + "-"
												+ username + "-" + createInscriptionRequestModel.getUrl_response_OK()
												+ "-" + createInscriptionRequestModel.getUrl_response_NOK());

								String url_webpay = resp.getUrlWebpay();
								String tbk_token = resp.getToken();

								logger.info("url_webpay: " + url_webpay);
								logger.info("tbk_token: " + tbk_token);

								JSONObject parametros_salida = new JSONObject();
								parametros_salida.put("url_webpay", url_webpay);
								parametros_salida.put("tbk_token", tbk_token);

								carritoDao.dejarRegistro(idRegistro, "CREATEINSCRIPTION", parametros_entrada.toString(),
										parametros_salida.toString());

								request.setAttribute("formAction", url_webpay);
								request.setAttribute("tokenWs", tbk_token);

								request.getRequestDispatcher("/redireccion_webpay.jsp").forward(request, response);

							} else {

								JSONObject parametros_salida = new JSONObject();
								parametros_salida.put("url_retorno",
										createInscriptionRequestModel.getUrl_response_NOK());
								parametros_salida.put("identificador", createInscriptionRequestModel.getId_client());
								parametros_salida.put("status", "500");
								parametros_salida.put("description", "ERROR");

								carritoDao.dejarRegistro(idRegistro, "CREATEINSCRIPTION", parametros_entrada.toString(),
										parametros_salida.toString());
								;
								request.setAttribute("url_retorno",
										createInscriptionRequestModel.getUrl_response_NOK());
								request.setAttribute("identificador", createInscriptionRequestModel.getId_client());
								request.setAttribute("status", "500");
								request.setAttribute("description", "ERROR");
								request.getRequestDispatcher("/retorno_portal_nok.jsp").forward(request, response);
							}

						} else {

							JSONObject parametros_salida = new JSONObject();
							parametros_salida.put("url_retorno", createInscriptionRequestModel.getUrl_response_NOK());
							parametros_salida.put("identificador", createInscriptionRequestModel.getId_client());
							parametros_salida.put("status", "400");
							parametros_salida.put("description", "ERROR-EMPRESA/CREDENCIAL INVALIDAS");

							carritoDao.dejarRegistro(idRegistro, "CREATEINSCRIPTION", parametros_entrada.toString(),
									parametros_salida.toString());

							request.setAttribute("url_retorno", createInscriptionRequestModel.getUrl_response_NOK());
							request.setAttribute("identificador", createInscriptionRequestModel.getId_client());
							request.setAttribute("status", "400");
							request.setAttribute("description", "ERROR-EMPRESA/CREDENCIAL INVALIDAS");
							request.getRequestDispatcher("/retorno_portal_nok.jsp").forward(request, response);
						}

					} else {

						JSONObject parametros_salida = new JSONObject();
						parametros_salida.put("url_retorno", createInscriptionRequestModel.getUrl_response_NOK());
						parametros_salida.put("identificador", createInscriptionRequestModel.getId_client());
						parametros_salida.put("status", "401");
						parametros_salida.put("description", "ERROR-UNAUTHORIZED");

						carritoDao.dejarRegistro(idRegistro, "CREATEINSCRIPTION", parametros_entrada.toString(),
								parametros_salida.toString());

						request.setAttribute("url_retorno", createInscriptionRequestModel.getUrl_response_NOK());
						request.setAttribute("identificador", createInscriptionRequestModel.getId_client());
						request.setAttribute("status", "401");
						request.setAttribute("description", "ERROR-UNAUTHORIZED");
						request.getRequestDispatcher("/retorno_portal_nok.jsp").forward(request, response);
					}

				} else {

					JSONObject parametros_salida = new JSONObject();
					parametros_salida.put("url_retorno", createInscriptionRequestModel.getUrl_response_NOK());
					parametros_salida.put("identificador", createInscriptionRequestModel.getId_client());
					parametros_salida.put("status", "401");
					parametros_salida.put("description", "ERROR-UNAUTHORIZED");

					carritoDao.dejarRegistro(idRegistro, "CREATEINSCRIPTION", parametros_entrada.toString(),
							parametros_salida.toString());

					request.setAttribute("url_retorno", createInscriptionRequestModel.getUrl_response_NOK());
					request.setAttribute("identificador", createInscriptionRequestModel.getId_client());
					request.setAttribute("status", "401");
					request.setAttribute("description", "ERROR-UNAUTHORIZED");
					request.getRequestDispatcher("/retorno_portal_nok.jsp").forward(request, response);
				}

			} else {

				JSONObject parametros_salida = new JSONObject();
				parametros_salida.put("url_retorno", createInscriptionRequestModel.getUrl_response_NOK());
				parametros_salida.put("identificador", createInscriptionRequestModel.getId_client());
				parametros_salida.put("status", "401");
				parametros_salida.put("description", "ERROR-UNAUTHORIZED");

				carritoDao.dejarRegistro(idRegistro, "CREATEINSCRIPTION", parametros_entrada.toString(),
						parametros_salida.toString());

				request.setAttribute("url_retorno", createInscriptionRequestModel.getUrl_response_NOK());
				request.setAttribute("identificador", createInscriptionRequestModel.getId_client());
				request.setAttribute("status", "401");
				request.setAttribute("description", "ERROR-UNAUTHORIZED");
				request.getRequestDispatcher("/retorno_portal_nok.jsp").forward(request, response);
			}

		} catch (SQLException | InscriptionStartException e) {
			// TODO: handle exception
			e.printStackTrace();
			request.setAttribute("url_retorno", createInscriptionRequestModel.getUrl_response_NOK());
			request.setAttribute("identificador", createInscriptionRequestModel.getId_client());
			request.setAttribute("status", "500");
			request.setAttribute("description", "ERROR");
			request.getRequestDispatcher("/retorno_portal_nok.jsp").forward(request, response);

		} catch (SignatureException | ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
			// TODO: handle exception
			request.setAttribute("url_retorno", createInscriptionRequestModel.getUrl_response_NOK());
			request.setAttribute("identificador", createInscriptionRequestModel.getId_client());
			request.setAttribute("status", "401");
			request.setAttribute("description", "ERROR-UNAUTHORIZED");
			request.getRequestDispatcher("/retorno_portal_nok.jsp").forward(request, response);
		}

	}

	@POST
	@GET
	@RequestMapping(value = { "/ResultInscription" })
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public void resultInscription(@RequestParam(value = "TBK_TOKEN", required = true) String tbk_token,
			@RequestParam(value = "PARAMS", required = true) String params, HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		logger.info("ENTRANDO A SIRPAYMENT RESULTINSCRIPTION");

		logger.info("TBK_TOKEN: " + tbk_token);

		String[] parametros = params.split("-");
		String empresa = parametros[0];
		String id_cliente = parametros[1];
		String url_ok = parametros[2];
		String url_nok = parametros[3];

		try {

			CarritoDaoImpl carritoDao = new CarritoDaoImpl();
			InitialContext initi = null;
			try {
				initi = new InitialContext();
			} catch (NamingException e2) {
				e2.printStackTrace();
			}
			DataSource dataSourceBTN = null;
			try {
				dataSourceBTN = (DataSource) initi.lookup("java:/SIR");
			} catch (NamingException e1) {
				e1.printStackTrace();
			}
			carritoDao.setDataSourceBTN(dataSourceBTN);

			JSONObject parametros_entrada = new JSONObject();
			parametros_entrada.put("tbk_token", tbk_token);
			parametros_entrada.put("params", params);

			Map<String, Object> dejarRegistro = carritoDao.dejarRegistro(0, "RESULTINSCRIPTION",
					parametros_entrada.toString(), "");

			Integer idRegistro = dejarRegistro.get("out_cod_retorno").toString().equals("00")
					? Integer.valueOf(dejarRegistro.get("out_desc_retorno").toString())
					: 0;

			Oneclick.MallInscription inscription = new Oneclick.MallInscription(new WebpayOptions(
					IntegrationCommerceCodes.ONECLICK_MALL, IntegrationApiKeys.WEBPAY, IntegrationType.TEST));
			final OneclickMallInscriptionFinishResponse resp = inscription.finish(tbk_token);

			String tbkUser = resp.getTbkUser();
			String authorizationCode = resp.getAuthorizationCode();
			String cardType = resp.getCardType();
			String cardNumber = resp.getCardNumber();
			String responseCode = String.valueOf(resp.getResponseCode());

			logger.info("tbkUser: " + tbkUser);
			logger.info("authorizationCode: " + authorizationCode);
			logger.info("cardType: " + cardType);
			logger.info("cardNumber: " + cardNumber);
			logger.info("responseCode: " + responseCode);

			if (responseCode.equals("0")) {

				carritoDao.insertDetalleCard(id_cliente, tbkUser, authorizationCode, cardType, cardNumber);

				JSONObject parametros_salida = new JSONObject();
				parametros_salida.put("tbkUser", tbkUser);
				parametros_salida.put("authorizationCode", authorizationCode);
				parametros_salida.put("cardType", cardType);
				parametros_salida.put("cardNumber", cardNumber);
				parametros_salida.put("responseCode", responseCode);

				carritoDao.dejarRegistro(idRegistro, "RESULTINSCRIPTION", parametros_entrada.toString(),
						parametros_salida.toString());

				request.setAttribute("url_retorno", url_ok);
				request.setAttribute("id_company", empresa);
				request.setAttribute("authorizationCode", authorizationCode);
				request.setAttribute("cardType", cardType);
				request.setAttribute("cardNumber", cardNumber);
				request.setAttribute("id_client", id_cliente);

				request.getRequestDispatcher("/retorno_portal.jsp").forward(request, response);

			} else {

				JSONObject parametros_salida = new JSONObject();
				parametros_salida.put("tbkUser", tbkUser);
				parametros_salida.put("authorizationCode", authorizationCode);
				parametros_salida.put("cardType", cardType);
				parametros_salida.put("cardNumber", cardNumber);
				parametros_salida.put("responseCode", responseCode);

				carritoDao.dejarRegistro(idRegistro, "RESULTINSCRIPTION", parametros_entrada.toString(),
						parametros_salida.toString());

				request.setAttribute("url_retorno", url_nok);
				request.setAttribute("id_company", empresa);
				request.setAttribute("id_client", id_cliente);
				request.setAttribute("status", responseCode);
				request.setAttribute("description", "ERROR");
				request.getRequestDispatcher("/retorno_portal_nok.jsp").forward(request, response);
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();

			request.setAttribute("url_retorno", url_nok);
			request.setAttribute("id_company", empresa);
			request.setAttribute("id_client", id_cliente);
			request.setAttribute("status", "500");
			request.setAttribute("description", "ERROR");
			request.getRequestDispatcher("/retorno_portal_nok.jsp").forward(request, response);

		}

	}

	@POST
	@RequestMapping(value = { "/DeleteInscription" })
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public ResponseEntity<?> deleteInscription(@RequestBody DeleteInscriptionRequestModel deleteInscriptionRequestModel,
			@RequestHeader("Authorization") String authorization) {

		logger.info("ENTRANDO A SIRPAYMENT DELETEINSCRIPTION");

		logger.info(deleteInscriptionRequestModel.toString());

		try {

			CarritoDaoImpl carritoDao = new CarritoDaoImpl();
			InitialContext initi = null;
			try {
				initi = new InitialContext();
			} catch (NamingException e2) {
				e2.printStackTrace();
			}
			DataSource dataSourceBTN = null;
			try {
				dataSourceBTN = (DataSource) initi.lookup("java:/SIR");
			} catch (NamingException e1) {
				e1.printStackTrace();
			}
			carritoDao.setDataSourceBTN(dataSourceBTN);

			JSONObject parametros_entrada = new JSONObject();
			parametros_entrada.put("body", deleteInscriptionRequestModel.toString());
			parametros_entrada.put("header", authorization);

			Map<String, Object> dejarRegistro = carritoDao.dejarRegistro(0, "DELETEINSCRIPTION",
					parametros_entrada.toString(), "");

			Integer idRegistro = dejarRegistro.get("out_cod_retorno").toString().equals("00")
					? Integer.valueOf(dejarRegistro.get("out_desc_retorno").toString())
					: 0;

			String[] auth = authorization.split("\\s+");

			String token_type = auth[0];

			if (!token_type.equals("Bearer")) {
				return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
			}

			String access_token = auth[1];

			String properties = "oneClickGenerico.properties";
			String secret_key = getPropertie("secret_key", properties);

			logger.info("verificando token");

			Claims claims = Jwts.parser().setSigningKey(secret_key.getBytes()).parseClaimsJws(access_token).getBody();

			logger.info(claims.toString());

			if (claims.get("client_id") != null || claims.get("client_secret") != null) {

				if (!claims.get("client_id").equals(deleteInscriptionRequestModel.getId_company()) || !claims
						.get("client_secret").equals(deleteInscriptionRequestModel.getCredential_id_company())) {
					return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
				}

				if (!deleteInscriptionRequestModel.getId_company().equals("")
						&& !deleteInscriptionRequestModel.getCredential_id_company().equals("")
						&& !deleteInscriptionRequestModel.getId_client().equals("")
						&& !deleteInscriptionRequestModel.getEmail().equals("")) {

					Map<String, Object> eliminarTarjeta = carritoDao.eliminarTarjeta(deleteInscriptionRequestModel);

					if (eliminarTarjeta.get("out_cod_retorno").toString().equals("00")) {

						Oneclick.MallInscription inscription = new Oneclick.MallInscription(
								new WebpayOptions(IntegrationCommerceCodes.ONECLICK_MALL, IntegrationApiKeys.WEBPAY,
										IntegrationType.TEST));
						inscription.delete(eliminarTarjeta.get("out_desc_retorno").toString(),
								deleteInscriptionRequestModel.getId_client());

						JSONObject json = new JSONObject();

						json.put("code", "00");
						json.put("description", "OK");

						carritoDao.dejarRegistro(idRegistro, "DELETEINSCRIPTION", parametros_entrada.toString(),
								json.toString());

						HttpHeaders headers = new HttpHeaders();
						headers.setContentType(new org.springframework.http.MediaType("application", "json", Charset.forName("UTF-8")));
						return new ResponseEntity<Object>(json.toString(), headers, HttpStatus.OK);

					} else {
						JSONObject json = new JSONObject();

						json.put("code", "500");
						json.put("description", "NO SE PUDO ELIMINAR LA TARJETA");

						carritoDao.dejarRegistro(idRegistro, "DELETEINSCRIPTION", parametros_entrada.toString(),
								json.toString());

						HttpHeaders headers = new HttpHeaders();
						headers.setContentType(new org.springframework.http.MediaType("application", "json", Charset.forName("UTF-8")));
						return new ResponseEntity<Object>(json.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
					}

				} else {

					JSONObject json = new JSONObject();

					json.put("code", "400");
					json.put("description", "SE DEBE MANDAR TODOS LOS VALORES");

					carritoDao.dejarRegistro(idRegistro, "DELETEINSCRIPTION", parametros_entrada.toString(),
							json.toString());

					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(new org.springframework.http.MediaType("application", "json", Charset.forName("UTF-8")));
					return new ResponseEntity<Object>(json.toString(), headers, HttpStatus.BAD_REQUEST);
				}
			} else {
				return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
			}

		} catch (SQLException e) {
			// TODO: handle exception
			JSONObject json = new JSONObject();
			json.put("code", "NO SE PUDO PROCESAR LA SOLICITUD");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new org.springframework.http.MediaType("application", "json", Charset.forName("UTF-8")));
			return new ResponseEntity<Object>(json.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (SignatureException | ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
			// TODO: handle exception
			return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
		} catch (InscriptionDeleteException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			JSONObject json = new JSONObject();
			json.put("code", "NO SE PUDO PROCESAR LA SOLICITUD");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new org.springframework.http.MediaType("application", "json", Charset.forName("UTF-8")));
			return new ResponseEntity<Object>(json.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@POST
	@RequestMapping(value = { "/Payment" })
	@Consumes({ org.springframework.http.MediaType.APPLICATION_JSON_VALUE })
	@Produces({ MediaType.APPLICATION_JSON })
	public ResponseEntity<?> payment(@RequestBody PagoRequestModel pagoRequestModel,
			@RequestHeader("Authorization") String authorization) {

		logger.info("ENTRANDO A SIRPAYMENT PAYMENT");

		logger.info(pagoRequestModel.toString());

		HttpStatus codeHttp = null;

		try {

			CarritoDaoImpl carritoDao = new CarritoDaoImpl();
			InitialContext initi = null;
			try {
				initi = new InitialContext();
			} catch (NamingException e2) {
				e2.printStackTrace();
			}
			DataSource dataSourceBTN = null;
			try {
				dataSourceBTN = (DataSource) initi.lookup("java:/SIR");
			} catch (NamingException e1) {
				e1.printStackTrace();
			}
			carritoDao.setDataSourceBTN(dataSourceBTN);

			JSONObject parametros_entrada = new JSONObject();
			parametros_entrada.put("body", pagoRequestModel.toString());
			parametros_entrada.put("header", authorization);

			Map<String, Object> dejarRegistro = carritoDao.dejarRegistro(0, "PAYMENT", parametros_entrada.toString(),
					"");

			Integer idRegistro = dejarRegistro.get("out_cod_retorno").toString().equals("00")
					? Integer.valueOf(dejarRegistro.get("out_desc_retorno").toString())
					: 0;

			String[] auth = authorization.split("\\s+");

			String token_type = auth[0];

			if (!token_type.equals("Bearer")) {
				return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
			}

			String access_token = auth[1];

			String properties = "oneClickGenerico.properties";
			String secret_key = getPropertie("secret_key", properties);

			logger.info("verificando token");

			Claims claims = Jwts.parser().setSigningKey(secret_key.getBytes()).parseClaimsJws(access_token).getBody();

			logger.info(claims.toString());

			if (claims.get("client_id") != null || claims.get("client_secret") != null) {

				if (!claims.get("client_id").equals(pagoRequestModel.getId_company())
						|| !claims.get("client_secret").equals(pagoRequestModel.getCredential_id_company())) {
					return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
				}

				Map<String, Object> validarEmpIdent = carritoDao.validarEmpresaIdentificador(pagoRequestModel);

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(new org.springframework.http.MediaType("application", "json", Charset.forName("UTF-8")));

				JSONObject json = new JSONObject();

				if (validarEmpIdent.get("out_cod_retorno").toString().equals("00")) {

					String[] desc = validarEmpIdent.get("out_desc_retorno").toString().split(";");

					String cod_comercio_empresa = desc[0];
					String tbkUser = desc[1];

					Map<String, Object> insertTransaction = carritoDao.insertTransacciones("",
							pagoRequestModel.getId_company(), pagoRequestModel.getId_client(),
							pagoRequestModel.getEmail(), tbkUser, pagoRequestModel.getAmount(),
							pagoRequestModel.getTransaction_order(), pagoRequestModel.getTransaction_type(),
							pagoRequestModel.getSucursal(), "", "C", "", "", "", "", new Long("0"), "", "", "",
							"", "", "", "");

					if (insertTransaction.get("out_cod_retorno").toString().equals("00")) {

						String id_transaccion = insertTransaction.get("out_desc_retorno").toString();

						String username = pagoRequestModel.getId_client();

						MallTransactionCreateDetails details = MallTransactionCreateDetails.build();

						Map<String, Object> comercioDetalle = carritoDao.obtenerComercio(pagoRequestModel);

						String comercioSucursal = comercioDetalle.get("out_desc_retorno").toString();

						details.add(pagoRequestModel.getAmount(), comercioSucursal,
								pagoRequestModel.getTransaction_order(), Byte.valueOf("0"));

						Oneclick.MallTransaction tx = new Oneclick.MallTransaction(new WebpayOptions(
								cod_comercio_empresa, IntegrationApiKeys.WEBPAY, IntegrationType.TEST));
						final OneclickMallTransactionAuthorizeResponse response = tx.authorize(username, tbkUser,
								pagoRequestModel.getTransaction_order(), details);

						logger.info("AccountingDate: " + response.getAccountingDate());
						logger.info("BuyOrder: " + response.getBuyOrder());
						logger.info("TransactionDate: " + response.getTransactionDate());
						final CardDetail cardDetail = response.getCardDetail();
						logger.info("CardNumber: " + cardDetail.getCardNumber());
						final List<Detail> detailsResp = response.getDetails();

						JSONObject detalle = new JSONObject();

						for (Detail detail : detailsResp) {

							logger.info("Amount: " + String.valueOf(detail.getAmount()));
							logger.info("AuthorizationCode: " + detail.getAuthorizationCode());
							logger.info("BuyOrder: " + detail.getBuyOrder());
							logger.info("CommerceCode: " + detail.getCommerceCode());
							logger.info("InstallmentsNumber: " + String.valueOf(detail.getInstallmentsNumber()));
							logger.info("PaymentTypeCode: " + detail.getPaymentTypeCode());
							logger.info("Status: " + detail.getStatus());

							detalle.put("Amount", detail.getAmount());
							detalle.put("AuthorizationCode", detail.getAuthorizationCode());
							detalle.put("BuyOrder", detail.getBuyOrder());
							detalle.put("CommerceCode", detail.getCommerceCode());
							detalle.put("InstallmentsNumber", String.valueOf(detail.getInstallmentsNumber()));
							detalle.put("PaymentTypeCode", detail.getPaymentTypeCode());
							detalle.put("Status", detail.getStatus());
							detalle.put("Code", detail.getResponseCode());

						}

						insertTransaction = carritoDao.insertTransacciones(id_transaccion,
								pagoRequestModel.getId_company(), pagoRequestModel.getId_client(),
								pagoRequestModel.getEmail(), tbkUser, pagoRequestModel.getAmount(),
								pagoRequestModel.getTransaction_order(), pagoRequestModel.getTransaction_type(),
								pagoRequestModel.getSucursal(), null, "P", response.getAccountingDate(),
								response.getBuyOrder(), response.getTransactionDate(), cardDetail.getCardNumber(),
								detalle.getLong("Amount"), detalle.get("AuthorizationCode").toString(),
								detalle.get("BuyOrder").toString(), detalle.get("CommerceCode").toString(),
								detalle.get("InstallmentsNumber").toString(), detalle.get("PaymentTypeCode").toString(),
								detalle.get("Status").toString(), detalle.get("Code").toString());

						
						json.put("Description", "TRANSACCIÓN PAGADA");
						json.put("TransactionOrder", response.getBuyOrder());
						json.put("TransactionDate", response.getTransactionDate());
						json.put("CardNumber", cardDetail.getCardNumber());
						json.put("Amount", detalle.getLong("Amount"));
						json.put("AuthorizationCode", detalle.get("AuthorizationCode").toString());
						json.put("CommerceCode", detalle.get("CommerceCode").toString());
						json.put("InstallmensNumber", detalle.get("InstallmentsNumber").toString());
						json.put("PaymentTypeCode", detalle.get("PaymentTypeCode").toString());
						json.put("Status", detalle.get("Status").toString());
						json.put("Code", detalle.get("Code"));

						carritoDao.dejarRegistro(idRegistro, "PAYMENT", parametros_entrada.toString(), json.toString());

						return new ResponseEntity<Object>(json.toString(), headers, HttpStatus.OK);

					} else {

						json = new JSONObject();
						json.put("Code", "500");
						json.put("Description", "NO SE PUDO CONCLUIR CON EL PAGO");

						carritoDao.dejarRegistro(idRegistro, "PAYMENT", parametros_entrada.toString(), json.toString());

						return new ResponseEntity<Object>(json.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);

					}

				}

				if (validarEmpIdent.get("out_cod_retorno").toString().equals("01")) {
					json.put("Code", "500");
					json.put("Description", "NO SE PUDO PROCESAR LA SOLICITUD");

					codeHttp = HttpStatus.INTERNAL_SERVER_ERROR;
				}

				if (validarEmpIdent.get("out_cod_retorno").toString().equals("400")) {
					json.put("Code", validarEmpIdent.get("out_cod_retorno").toString());
					json.put("Description", validarEmpIdent.get("out_desc_retorno").toString());

					codeHttp = HttpStatus.BAD_REQUEST;
				}

				carritoDao.dejarRegistro(idRegistro, "PAYMENT", parametros_entrada.toString(), json.toString());

				return new ResponseEntity<Object>(json.toString(), headers, codeHttp);

			} else {
				return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
			}

		} catch (SQLException | TransactionAuthorizeException | IOException e) {
			// TODO: handle exception
			e.printStackTrace();
			JSONObject json = new JSONObject();
			json.put("Code", "500");
			json.put("Description", "NO SE PUDO PROCESAR LA SOLICITUD");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new org.springframework.http.MediaType("application", "json", Charset.forName("UTF-8")));

			return new ResponseEntity<Object>(json.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);

		} catch (SignatureException | ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
			// TODO: handle exception
			return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
		}

	}

	@POST
	@RequestMapping(value = { "/PaymentMassive" })
	@Consumes({ org.springframework.http.MediaType.APPLICATION_JSON_VALUE })
	@Produces({ MediaType.APPLICATION_JSON })
	public ResponseEntity<?> paymentMassive(@RequestBody PagosMasivosRequestModel pagosMasivosRequestModel,
			@RequestHeader("Authorization") String authorization) {

		logger.info("ENTRANDO A SIRPAYMENT PAYMENT");

		logger.info(pagosMasivosRequestModel.toString());

		try {

			String[] auth = authorization.split("\\s+");

			String token_type = auth[0];

			if (!token_type.equals("Bearer")) {
				return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
			}

			String access_token = auth[1];

			String properties = "oneClickGenerico.properties";
			String secret_key = getPropertie("secret_key", properties);

			logger.info("verificando token");

			Claims claims = Jwts.parser().setSigningKey(secret_key.getBytes()).parseClaimsJws(access_token).getBody();

			logger.info(claims.toString());

			if (claims.get("client_id") != null || claims.get("client_secret") != null) {

				if (!claims.get("client_id").equals(pagosMasivosRequestModel.getId_company())
						|| !claims.get("client_secret").equals(pagosMasivosRequestModel.getCredential_id_company())) {
					return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
				}

				CarritoDaoImpl carritoDao = new CarritoDaoImpl();
				InitialContext initi = null;
				try {
					initi = new InitialContext();
				} catch (NamingException e2) {
					e2.printStackTrace();
				}
				DataSource dataSourceBTN = null;
				try {
					dataSourceBTN = (DataSource) initi.lookup("java:/SIR");
				} catch (NamingException e1) {
					e1.printStackTrace();
				}
				carritoDao.setDataSourceBTN(dataSourceBTN);

				Map<String, Object> validarEmp = carritoDao.validarEmpresa(pagosMasivosRequestModel.getId_company(),
						pagosMasivosRequestModel.getCredential_id_company());

				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(new org.springframework.http.MediaType("application", "json", Charset.forName("UTF-8")));

				JSONObject json = new JSONObject();

				if (validarEmp.get("out_cod_retorno").toString().equals("00")) {

					int countPagos = 0;

					List<String> registro = new ArrayList<>();

					for (PagoAllRequestModel pagos : pagosMasivosRequestModel.getPayments()) {

						Map<String, Object> insertTransaction = carritoDao.insertTransacciones("",
								pagosMasivosRequestModel.getId_company(), pagos.getId_client(), pagos.getEmail(), "",
								pagos.getAmount(), pagos.getTransaction_order(), pagos.getTransaction_type(),
								pagos.getSucursal(), "", "C", "", "", "", "", new Long("0"), "", "", "",
								"", "", "", "");

						if (insertTransaction.get("out_cod_retorno").toString().equals("00")) {
							countPagos++;
							registro.add(pagos.getTransaction_order());
						}

					}

					json.put("Code", "00");
					json.put("Description", "OK");
					json.put("Recorded payments", countPagos);
					json.put("Registered", registro);
					return new ResponseEntity<Object>(json.toString(), headers, HttpStatus.OK);

				} else {
					json.put("Code", "400");
					json.put("Description", "EMPRESA Y/O CREDENCIAL INVALIDAS");
					return new ResponseEntity<Object>(json.toString(), headers, HttpStatus.BAD_REQUEST);
				}

			} else {
				return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
			}

		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			JSONObject json = new JSONObject();
			json.put("Code", "500");
			json.put("Description", "NO SE PUDO PROCESAR LA SOLICITUD");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new org.springframework.http.MediaType("application", "json", Charset.forName("UTF-8")));

			return new ResponseEntity<Object>(json.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);

		} catch (SignatureException | ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
			// TODO: handle exception
			return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
		}

	}

	@GET
	@RequestMapping(value = { "/ConsultTransaction" })
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON + ";charset=iso-8859-1"})
	public ResponseEntity<?> consultTransaction(
			@RequestBody ConsultTransactionRequestModel consultTransactionRequestModel,
			@RequestHeader("Authorization") String authorization) {

		logger.info("ENTRANDO A SIRPAYMENT CONSULTTRANSACTION");

		logger.info(consultTransactionRequestModel.toString() + " - " + authorization);

		JSONObject json = new JSONObject();

		try {

			CarritoDaoImpl carritoDao = new CarritoDaoImpl();
			InitialContext initi = null;
			try {
				initi = new InitialContext();
			} catch (NamingException e2) {
				e2.printStackTrace();
			}
			DataSource dataSourceBTN = null;
			try {
				dataSourceBTN = (DataSource) initi.lookup("java:/SIR");
			} catch (NamingException e1) {
				e1.printStackTrace();
			}
			carritoDao.setDataSourceBTN(dataSourceBTN);

			JSONObject parametros_entrada = new JSONObject();
			parametros_entrada.put("body", consultTransactionRequestModel.toString());
			parametros_entrada.put("header", authorization);

			Map<String, Object> dejarRegistro = carritoDao.dejarRegistro(0, "CONSULTTRANSACTION",
					parametros_entrada.toString(), "");

			Integer idRegistro = dejarRegistro.get("out_cod_retorno").toString().equals("00")
					? Integer.valueOf(dejarRegistro.get("out_desc_retorno").toString())
					: 0;

			String[] auth = authorization.split("\\s+");

			String token_type = auth[0];

			if (!token_type.equals("Bearer")) {
				return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
			}

			String access_token = auth[1];

			String properties = "oneClickGenerico.properties";
			String secret_key = getPropertie("secret_key", properties);

			logger.info("verificando token");

			Claims claims = Jwts.parser().setSigningKey(secret_key.getBytes()).parseClaimsJws(access_token).getBody();

			logger.info(claims.toString());

			if (claims.get("client_id") != null || claims.get("client_secret") != null) {

				if (!claims.get("client_id").equals(consultTransactionRequestModel.getId_company()) || !claims
						.get("client_secret").equals(consultTransactionRequestModel.getCredential_id_company())) {
					return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
				}

				if (!consultTransactionRequestModel.getId_company().equals("")
						&& !consultTransactionRequestModel.getCredential_id_company().equals("")
						&& !consultTransactionRequestModel.getId_client().equals("")
						&& !consultTransactionRequestModel.getEmail().equals("")
						&& !consultTransactionRequestModel.getTransaction_order().equals("")) {
					
					
					Map<String, Object> consultarTransaccion = carritoDao.consultarTransaccion(consultTransactionRequestModel);
					
					HttpStatus codeHttp = null;
					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(new org.springframework.http.MediaType("application", "json", Charset.forName("UTF-8")));
					
					if(consultarTransaccion.get("out_cod_retorno").toString().equals("00")) {
						
						carritoDao.dejarRegistro(idRegistro, "CONSULTTRANSACTION", parametros_entrada.toString(), json.toString());
						
						codeHttp = HttpStatus.OK; 
						return new ResponseEntity<Object>(consultarTransaccion.get("out_desc_retorno").toString(), headers, codeHttp);
												
					}

					if (consultarTransaccion.get("out_cod_retorno").toString().equals("400")) {
						json.put("code", consultarTransaccion.get("out_cod_retorno").toString());
						json.put("description", consultarTransaccion.get("out_desc_retorno").toString());

						codeHttp = HttpStatus.BAD_REQUEST;
					}

					carritoDao.dejarRegistro(idRegistro, "CONSULTTRANSACTION", parametros_entrada.toString(), json.toString());

					return new ResponseEntity<Object>(json.toString(), headers, codeHttp);

				} else {

					json.put("code", "400");
					json.put("description", "SE DEBE MANDAR TODOS LOS VALORES");

					carritoDao.dejarRegistro(idRegistro, "GETCARDS", parametros_entrada.toString(), json.toString());

					HttpHeaders headers = new HttpHeaders();
					headers.setContentType(new org.springframework.http.MediaType("application", "json", Charset.forName("UTF-8")));
					return new ResponseEntity<Object>(json.toString(), headers, HttpStatus.BAD_REQUEST);

				}
			} else {
				return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
			}

		} catch (SQLException e) {
			// TODO: handle exception
			json.put("code", "404");
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(new org.springframework.http.MediaType("application", "json", Charset.forName("UTF-8")));
			return new ResponseEntity<Object>(json.toString(), headers, HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (SignatureException | ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
			// TODO: handle exception
			return new ResponseEntity<Object>(HttpStatus.UNAUTHORIZED);
		}

	}

	@GET
	@RequestMapping(value = { "/pruebaretorno" })
	@Consumes({ MediaType.APPLICATION_FORM_URLENCODED })
	public void pruebaretorno(@RequestParam(value = "transaction_status", required = true) String transaction_status,
			HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if (transaction_status.equals("OK")) {
			request.setAttribute("mensaje", "PAGADO");
		} else {
			request.setAttribute("mensaje", "ERROR");
		}

		request.getRequestDispatcher("/retorno.jsp").forward(request, response);

	}

	public String getPropertie(String Key, String arch_propertie) {
		logger.info("getPropertie: " + arch_propertie);
		InputStream input = null;
		Properties prop = new Properties();
		File confDir = new File(System.getProperty("jboss.server.config.dir"));
		File fileProp = new File(confDir, arch_propertie);
		String result = null;
		try {
			input = new FileInputStream(fileProp);
			if (input != null) {
				prop.load(input);
				result = prop.getProperty(Key);
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "";
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "";
		}
		logger.info("resultPropertie: " + result);
		return result;
	}

}
