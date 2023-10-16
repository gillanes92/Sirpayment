package cl.ndt.bolsas.enel.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class RedirectController implements Controller {

public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
    	String ident = request.getParameter("ident");
    	String corr = request.getParameter("corr");
        
    	
    	ModelAndView modelAndView = new ModelAndView();

		modelAndView.setViewName("redirect");
		
		modelAndView.addObject("ident", ident);
		modelAndView.addObject("corr", corr);
		modelAndView.addObject("url", "http://35.87.226.34/oneClickGenerico/OneClick/createInscription");
    	
        
        return modelAndView;
    }
}
