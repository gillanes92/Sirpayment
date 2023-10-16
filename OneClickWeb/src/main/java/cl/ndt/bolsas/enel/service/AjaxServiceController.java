/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cl.ndt.bolsas.enel.service;

import cl.ndt.bolsas.enel.dao.ConsultaDao;
import cl.ndt.bolsas.enel.utils.Configuraciones;
import cl.ndt.bolsas.enel.ws.ClienteBolsas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 *
 * @author Lenovo
 */
public class AjaxServiceController implements Controller {
    
    private static Logger log = (Logger) Logger.getLogger(AjaxServiceController.class);
    Configuraciones prop = new Configuraciones();
    
    private ConsultaDao consultaDao;
    private ClienteBolsas cliente;
    
    public void setConsultaDao(ConsultaDao consultaDao) {
    	this.consultaDao = consultaDao;
    }
    
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws IOException{
        String nombreMetodo = "";
        nombreMetodo = request.getParameter("method");
        String nombreVista = "servicio_ajax";
        
        Map<String, Object> respuesta = new HashMap<>();
	Map<String, Integer> metodos = new HashMap<>();
        Map<String,Object> outParameter = new HashMap<>();
        JSONArray array = null;
        metodos.put("enviarParam", 0);
        metodos.put("buscarIdentificador", 1);
        log.info("prueba");
        switch (metodos.get(nombreMetodo)) {
            case 0:
                
                String email = request.getParameter("email");
                String monto1 = request.getParameter("monto1");
                String monto2 = request.getParameter("monto2");
                
                response.sendRedirect("http://35.87.226.34/oneClickGenerico/OneClick/createInscription?name="+email+"&correo="+email);
                
                
                break;
                
            case 1:
            	
            	String identificador = request.getParameter("identificador");
            	           	
            	
            	String[] commandConsultar = { "curl", "--location", "--request", "GET",
            			"http://35.87.226.34/oneClickGenerico/OneClick/getCards?identificador="+identificador};
				
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
				
                respuesta.put("json_respuesta", new JSONObject(consulta));				
            	
            	break;
        
        }
        
        ModelAndView modelAndView = new ModelAndView(nombreVista, "mapa", respuesta);
        return modelAndView;
    }
    
    public String getIdCart(String nCliente, String monto){
        String idCart = "";
        
        nCliente = nCliente.replace(".", "").split("-")[0];
        
        Calendar fecha = Calendar.getInstance();
        Integer anio = fecha.get(Calendar.YEAR);
        Integer mes = fecha.get(Calendar.MONTH) + 1;
        Integer dia = fecha.get(Calendar.DAY_OF_MONTH);
        Integer hora = fecha.get(Calendar.HOUR_OF_DAY);
        Integer minuto = fecha.get(Calendar.MINUTE);
        Integer segundo = fecha.get(Calendar.SECOND);
        
        nCliente = formatear(nCliente);
        monto = formatear(monto);
        
        idCart = anio.toString()+mes.toString()+dia.toString()+hora.toString()+minuto.toString()+segundo.toString()+nCliente+monto;
        
        return idCart;
    }
    
    public String formatear(String str){
        String res = "";
        
        if(str.length()<7){
            int falta = 7-str.length();
            for(int i = 0; i < falta; i++){
                str = "0"+str;
            }
            res = str;
        }else{
            res = str;
        }
        
        return res;
    }
    
}
