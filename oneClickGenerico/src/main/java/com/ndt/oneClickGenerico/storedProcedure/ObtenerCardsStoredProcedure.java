package com.ndt.oneClickGenerico.storedProcedure;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import com.ndt.oneClickGenerico.model.GetCardsRequestModel;

public class ObtenerCardsStoredProcedure {

	private CallableStatement callableStatement = null;
	private Connection conn = null;
	
	public ObtenerCardsStoredProcedure(Connection conn) throws SQLException{
		
		this.conn = conn;
		this.conn.setAutoCommit(false);
		callableStatement = this.conn.prepareCall("{call oneclick.select_card_identificador(?,?,?,?) }");
 
	}
	
	public Map<String, Object> execute(GetCardsRequestModel getCardsRequestModel){
		
		Map<String,Object> outParams = new HashMap<String, Object>();
		 	
		try {
			
			callableStatement.setString(1, getCardsRequestModel.getId_company());
			callableStatement.setString(2, getCardsRequestModel.getCredential_id_company());
			callableStatement.setString(3, getCardsRequestModel.getId_client());
			callableStatement.setString(4, getCardsRequestModel.getEmail());
			
			callableStatement.registerOutParameter(1, Types.VARCHAR);
			callableStatement.registerOutParameter(2, Types.VARCHAR);

			System.out.println("call oneclick.select_card_identificador("+getCardsRequestModel.getId_company()+","+getCardsRequestModel.getCredential_id_company()+","+getCardsRequestModel.getId_client()+","+getCardsRequestModel+")");
			
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
