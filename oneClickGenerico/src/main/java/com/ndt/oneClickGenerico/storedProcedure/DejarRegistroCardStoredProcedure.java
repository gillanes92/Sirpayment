package com.ndt.oneClickGenerico.storedProcedure;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class DejarRegistroCardStoredProcedure {

	private CallableStatement callableStatement = null;
	private Connection conn = null;
	
	public DejarRegistroCardStoredProcedure(Connection conn) throws SQLException{
		
		this.conn = conn;
		this.conn.setAutoCommit(true);
		callableStatement = this.conn.prepareCall("{call oneclick.insert_historial_consumo(?,?,?,?) }");
 
	}
	
	public Map<String, Object> execute(Integer id, String servicio, String parametros_entrada,
			String parametros_salida){
		
		Map<String,Object> outParams = new HashMap<String, Object>();
		 	
		try {
			
			callableStatement.setInt(1, id);
			callableStatement.setString(2, servicio);
			callableStatement.setString(3, parametros_entrada);
			callableStatement.setString(4, parametros_salida);
			
			callableStatement.registerOutParameter(1, Types.VARCHAR);
			callableStatement.registerOutParameter(2, Types.VARCHAR);

			System.out.println("call oneclick.insert_historial_consumo("+id+","+servicio+","+parametros_entrada+","+parametros_salida+")");
			
			callableStatement.execute();
			
			outParams.put("out_cod_retorno", callableStatement.getString(1));
			outParams.put("out_desc_retorno", callableStatement.getString(2));
			System.out.println(outParams.get("out_cod_retorno"));
			System.out.println(outParams.get("out_desc_retorno"));			
			
			callableStatement.close();
			this.conn.close();
 
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			
			try {
				
				if(callableStatement != null) 
					callableStatement.close();
			
				if(this.conn != null)
					this.conn.close();
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
		}
		
		return outParams;
		
	}
}
