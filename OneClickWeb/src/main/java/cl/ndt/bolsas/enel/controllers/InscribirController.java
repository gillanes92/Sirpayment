/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.ndt.bolsas.enel.controllers;

import cl.ndt.bolsas.enel.utils.Configuraciones;
import cl.ndt.bolsas.enel.ws.ClienteBolsas;
import java.text.DecimalFormat;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 *
 * @author Lenovo
 */
public class InscribirController implements Controller {

    Configuraciones prop = new Configuraciones();
    
    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        
        ModelAndView modelAndView = new ModelAndView();
        
        modelAndView.setViewName("inscribir");

        return modelAndView;
    }
}
