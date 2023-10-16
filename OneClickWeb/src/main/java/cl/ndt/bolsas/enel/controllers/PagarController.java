/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.ndt.bolsas.enel.controllers;

import cl.ndt.bolsas.enel.model.DetallesCard;
import cl.ndt.bolsas.enel.utils.Configuraciones;
import cl.ndt.bolsas.enel.ws.ClienteBolsas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 *
 * @author Lenovo
 */
public class PagarController implements Controller {

	private static Logger log = (Logger) Logger.getLogger(PagarController.class);
	Configuraciones prop = new Configuraciones();

	@Override
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

		ModelAndView modelAndView = new ModelAndView();

		modelAndView.setViewName("pagar");

		String identificador = request.getParameter("identificador");

		if (identificador == null) {
			return modelAndView;
		}
		
		modelAndView.addObject("identificador", identificador);

		String cards = consultar(identificador);

		JSONObject jsonCard = new JSONObject(cards);

		if (jsonCard.isNull("cards")) {
			modelAndView.addObject("texto", "no existen cards");
		} else {

			List<DetallesCard> detalles = new ArrayList<>();

			for (int i = 0; i < jsonCard.getJSONArray("cards").length(); i++) {

				DetallesCard detalle = new DetallesCard();

				String authorizationCode = jsonCard.getJSONArray("cards").getJSONObject(i)
						.getString("authorizationCode");
				String cardType = jsonCard.getJSONArray("cards").getJSONObject(i).getString("cardType");
				String cardNumber = jsonCard.getJSONArray("cards").getJSONObject(i).getString("cardNumber");
				String tbkUser = jsonCard.getJSONArray("cards").getJSONObject(i).getString("tbkUser");

				detalle.setAuthorizationCode(authorizationCode);
				detalle.setCardType(cardType);
				detalle.setCardNumber(cardNumber);
				detalle.setTbkUser(tbkUser);

				detalles.add(detalle);

			}

			modelAndView.addObject("cards", detalles);
		}

		return modelAndView;
	}

	public String consultar(String identificador) throws IOException {

		String[] commandConsultar = { "curl", "--location", "--request", "GET",
				"http://35.87.226.34/oneClickGenerico/OneClick/getCards?identificador=" + identificador };

		log.info("CMD CREAR : " + Arrays.toString(commandConsultar));

		ProcessBuilder process = new ProcessBuilder(commandConsultar);
		Process p = null;
		BufferedReader reader = null;
		StringBuilder builder = null;

		try {
			p = process.start();
			reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
			builder = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				builder.append(line);
				builder.append(System.getProperty("line.separator"));
			}

		} finally {
			// TODO: handle finally clause
			p.destroy();
			reader.close();
		}
		log.info("result: " + builder.toString());
		String consulta = builder.toString();

		return consulta;

	}
}
