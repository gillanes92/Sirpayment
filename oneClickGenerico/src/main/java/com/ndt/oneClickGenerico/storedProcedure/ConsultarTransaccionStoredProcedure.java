package com.ndt.oneClickGenerico.storedProcedure;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import com.ndt.oneClickGenerico.model.ConsultTransactionRequestModel;
import com.ndt.oneClickGenerico.model.GetCardsRequestModel;

public class ConsultarTransaccionStoredProcedure {

	private CallableStatement callableStatement = null;
	private Connection conn = null;
	
	public ConsultarTransaccionStoredProcedure(Connection conn) throws SQLException{
		
		this.conn = conn;
		this.conn.setAutoCommit(false);
		callableStatement = this.conn.prepareCall("{call oneclick.consultar_transaccion(?,?,?,?,?) }");
 
	}
	
	public Map<String, Object> execute(ConsultTransactionRequestModel consultTransactionRequestModel){
		
		Map<String,Object> outParams = new HashMap<String, Object>();
		 	
		try {
			
			callableStatement.setString(1, consultTransactionRequestModel.getId_company());
			callableStatement.setString(2, consultTransactionRequestModel.getCredential_id_company());
			callableStatement.setString(3, consultTransactionRequestModel.getId_client());
			callableStatement.setString(4, consultTransactionRequestModel.getEmail());
			callableStatement.setString(5, consultTransactionRequestModel.getTransaction_order());
			
			callableStatement.registerOutParameter(1, Types.VARCHAR);
			callableStatement.registerOutParameter(2, Types.VARCHAR);

			System.out.println("call oneclick.consultar_transaccion("+consultTransactionRequestModel.getId_company()+","+consultTransactionRequestModel.getCredential_id_company()+","+consultTransactionRequestModel.getId_client()+","+consultTransactionRequestModel.getEmail()+","+consultTransactionRequestModel.getTransaction_order()+")");
			
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
