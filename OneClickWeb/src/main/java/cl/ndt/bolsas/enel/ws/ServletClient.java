package cl.ndt.bolsas.enel.ws;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Plugin para comunicacion con un servidor Telnet
 *
 * @author jramos
 */
public class ServletClient {

    private Object getJsonValue(JSONObject obj, String key, Object defaultValue) {
        try {
            if (obj.get(key) instanceof JSONObject) {
                return ((JSONObject) obj.get(key)).toString();
            } else {
                return obj.get(key);
            }
        } catch (JSONException e) {
            return defaultValue;
        }
    }

    /**
     * Recibe un objeto JSON con los datos a enviar al servlet
     *
     * @param jobj
     * @return
     * @throws Exception
     */
    public String sendCommand(JSONObject jobj) throws Exception {
        String out = "";
        StringBuilder urltext = new StringBuilder();
        try {
            String method = (String) getJsonValue(jobj, "method", "");
            String getParameters = (String) getJsonValue(jobj, "getParameters", "");
            String postParameters = (String) getJsonValue(jobj, "postParameters", "");
            String urlContext = (String) getJsonValue(jobj, "urlContext", "");
            String contentType = (String) getJsonValue(jobj, "contentType", "");
            String authorization = (String) getJsonValue(jobj, "authorization","");
            String charset = (String) getJsonValue(jobj, "charset","");

            if (urlContext.length() > 0) {
                urltext.append(urlContext);
            }

            if (method.length() > 0) {
                urltext.append("/");
                urltext.append(method);
            }

            if (getParameters.length() > 0) {
                urltext.append("?");
                urltext.append(ParametersFormat(getParameters));
            }

            URL url = new URL(urltext.toString());

            HttpURLConnection servlet = (HttpURLConnection) url.openConnection();
            servlet.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)");
            

            if (contentType.length() > 0) {
                servlet.setRequestProperty("content-type", contentType);
            }
            
            if (authorization.length() > 0){
                servlet.addRequestProperty("Authorization", authorization);
                servlet.addRequestProperty("Accept", "*/*");
            }

// Envia parametros por POST si es que vienen
            OutputStreamWriter  wr = null;
            if (postParameters.length() > 0) {
                servlet.setDoOutput(true);
                servlet.setDoInput(true);
                servlet.setRequestMethod("POST");
                wr = new OutputStreamWriter (servlet.getOutputStream());
               
                wr.write(ParametersFormat(postParameters));
                wr.flush();
            }

            StringBuilder acum;
            
            try (BufferedReader in = new BufferedReader(new InputStreamReader(servlet.getInputStream()))) {
                String inputLine;
                acum = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    acum.append(inputLine);
                }
                
                in.close();
            }
            out = acum.toString();
            
            wr.close();
            servlet.disconnect();

        } catch (FileNotFoundException e) {
            Logger.getLogger(ServletClient.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException ex) {
            Logger.getLogger(ServletClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return out;
    }

    /**
     * Crea la linea de parametros con el formato correcto para ser llamado por
     * http
     *
     * @param in
     * @return
     * @throws UnsupportedEncodingException
     */
    private String ParametersFormat(String in) throws UnsupportedEncodingException {
        StringBuilder out = new StringBuilder();
        if (in.contains("&")) {
            String[] params = in.split("&");
            String[] parameter;
            for (int i = 0; i < params.length; i++) {
                parameter = params[i].split("=");
                out.append(parameter[0]);
                out.append("=");
                out.append(URLEncoder.encode(cleanData(parameter[1]), "UTF-8"));
                if ((i + 1) < params.length) {
                    out.append("&");
                }
            }
        } else {
            if (in.contains("=")) {
                String[] line = in.split("=");
                out.append(line[0]);
                out.append("=");
                out.append(URLEncoder.encode(cleanData(line[1]), "UTF-8"));
            } else {
                out.append(in);
            }
        }
        return out.toString();
    }

    /**
     * Limpia el dato para no tener problemas con la url
     *
     * @param in
     * @return
     */
    private String cleanData(String in) {
        String out = in.replaceAll("º", "");
        out = out.replaceAll("ª", "");
        out = out.toLowerCase().replaceAll("nro", "");
        out = out.replaceAll("#", "");
        return out;
    }
}
