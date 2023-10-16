package com.ndt.oneClickGenerico.storedProcedure;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import com.ndt.oneClickGenerico.model.CreateInscriptionRequestModel;

public class InsertIdentificadorStoredProcedure {

	private CallableStatement callableStatement = null;
	private Connection conn = null;
	
	public InsertIdentificadorStoredProcedure(Connection conn) throws SQLException{
		
		this.conn = conn;
		this.conn.setAutoCommit(true);
		callableStatement = this.conn.prepareCall("{call oneclick.insert_identificador(?,?,?,?) }");
 
	}
	
	public Map<String, Object> execute(CreateInscriptionRequestModel createInscriptionRequestModel){
		
		Map<String,Object> outParams = new HashMap<String, Object>();
		 	
		try {
			
			callableStatement.setString(1, createInscriptionRequestModel.getId_company());
			callableStatement.setString(2, createInscriptionRequestModel.getCredential_id_company());
			callableStatement.setString(3, createInscriptionRequestModel.getId_client());
			callableStatement.setString(4, createInscriptionRequestModel.getEmail());
			
			callableStatement.registerOutParameter(1, Types.VARCHAR);
			callableStatement.registerOutParameter(2, Types.VARCHAR);
			System.out.println("call oneclick.insert_identificador("+createInscriptionRequestModel.getId_company()+","+createInscriptionRequestModel.getCredential_id_company()+","+createInscriptionRequestModel.getId_client()+","+createInscriptionRequestModel.getEmail()+")");
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
