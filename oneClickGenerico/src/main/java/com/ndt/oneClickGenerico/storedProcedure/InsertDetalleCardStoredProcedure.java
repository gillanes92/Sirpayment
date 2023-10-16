package com.ndt.oneClickGenerico.storedProcedure;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

public class InsertDetalleCardStoredProcedure {

	private CallableStatement callableStatement = null;
	private Connection conn = null;
	
	public InsertDetalleCardStoredProcedure(Connection conn) throws SQLException{
		
		this.conn = conn;
		this.conn.setAutoCommit(true);
		callableStatement = this.conn.prepareCall("{call oneclick.insert_detallecard(?,?,?,?,?) }");
 
	}
	
	public Map<String, Object> execute(String id_cliente, String tbkUser, String authorizationCode,
			String cardType, String cardNumber){
		
		Map<String,Object> outParams = new HashMap<String, Object>();
		 	
		try {
			
			callableStatement.setString(1, id_cliente);
			callableStatement.setString(2, authorizationCode);
			callableStatement.setString(3, cardType);
			callableStatement.setString(4, cardNumber);
			callableStatement.setString(5, tbkUser);
			
			callableStatement.registerOutParameter(1, Types.VARCHAR);
			callableStatement.registerOutParameter(2, Types.VARCHAR);

			System.out.println("call oneclick.insert_detallecard("+id_cliente+","+authorizationCode+","+cardType+","+cardNumber+","+tbkUser+")");
			
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
