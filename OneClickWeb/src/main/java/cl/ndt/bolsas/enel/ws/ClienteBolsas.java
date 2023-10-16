/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.ndt.bolsas.enel.ws;

/**
 *
 * @author Lenovo
 */
import cl.ndt.bolsas.enel.utils.Configuraciones;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

public class ClienteBolsas {

    Configuraciones prop = new Configuraciones();
    String url;
    
    public String validarCliente(String NroCliente){
        
        String retorno = "";
        
        String ur = prop.getProperties("urlValidaBolsa", prop.CONFIG_SERVICE);
    	
        String input= "{\"id_empresa\":\"1\",\"nro_suministro\":\""+NroCliente.split("-")[0]+"\"}";
        
        try {    
            String[] command = {"curl", "-H", "Content-Type:application/json", ur, "-X", "POST", "-d", input};
            
            ProcessBuilder process = new ProcessBuilder(command);
            Process p;
            p = process.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
            String result = builder.toString();
            System.out.println("RESULT INFO VALIDAR BOLSA: " + result);
            
            result = result.toString().replace("\\", "").replace("\"{", "{").replace("\"}", "}");
            
            JSONObject out = new JSONObject(result);
            
            String cod_retorno = out.getJSONObject("d").getJSONObject("retorno").getString("codigo_retorno");
            String descrip_retorno = out.getJSONObject("d").getJSONObject("retorno").getString("descripcion_retorno");
            
            retorno = descrip_retorno;
            
            System.out.println("cod_retorno: " + cod_retorno + " descrip_retorno: "+descrip_retorno);
            
        } catch (IOException ex) {
            Logger.getLogger(ClienteBolsas.class.getName()).log(Level.SEVERE, null, ex);
        }    
        
        return retorno;
    }

    public String tokenSupply() {

        JSONObject out = null;
        String clientId = prop.getProperties("clientId", prop.CONFIG_SERVICE);
        String clientSecret = prop.getProperties("clientSecret", prop.CONFIG_SERVICE);
        String ur = prop.getProperties("urlTokenSupply", prop.CONFIG_SERVICE);
        String resp = "";
        try {
            String[] command = {"curl", "-H", "Content-Type:application/x-www-form-urlencoded", ur, "-X", "POST", "-d", "client_id=" + clientId + "&client_secret=" + clientSecret + "&grant_type=client_credentials"};

            ProcessBuilder process = new ProcessBuilder(command);
            Process p;
            p = process.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }

            String result = builder.toString();
            System.out.println("RESULT TOKEN" + result);

            if (!result.isEmpty() && !result.contains("Service Not Found") && !result.contains("500")) {
                out = new JSONObject(result);

                if (!out.has("error")) {
                    resp = out.getString("access_token");
                }
            }

            System.out.println(resp);

        } catch (IOException | JSONException ex) {
            Logger.getLogger(ClienteBolsas.class.getName()).log(Level.SEVERE, null, ex);
        }
        return resp;
    }


}
