package com.ndt.oneClickGenerico.storedProcedure;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import com.ndt.oneClickGenerico.model.DeleteInscriptionRequestModel;

public class DeleteDetalleCardStoredProcedure {

	private CallableStatement callableStatement = null;
	private Connection conn = null;
	
	public DeleteDetalleCardStoredProcedure(Connection conn) throws SQLException{
		
		this.conn = conn;
		this.conn.setAutoCommit(true);
		callableStatement = this.conn.prepareCall("{call oneclick.delete_detallecard(?,?,?,?) }");
 
	}
	
	public Map<String, Object> execute(DeleteInscriptionRequestModel deleteInscriptionRequestModel){
		
		Map<String,Object> outParams = new HashMap<String, Object>();
		 	
		try {
			
			callableStatement.setString(1, deleteInscriptionRequestModel.getId_company());
			callableStatement.setString(2, deleteInscriptionRequestModel.getCredential_id_company());
			callableStatement.setString(3, deleteInscriptionRequestModel.getId_client());
			callableStatement.setString(4, deleteInscriptionRequestModel.getEmail());
			
			callableStatement.registerOutParameter(1, Types.VARCHAR);
			callableStatement.registerOutParameter(2, Types.VARCHAR);

			System.out.println("call oneclick.delete_detallecard("+deleteInscriptionRequestModel.getId_company()+","+deleteInscriptionRequestModel.getCredential_id_company()+","+deleteInscriptionRequestModel.getId_client()+","+deleteInscriptionRequestModel.getEmail()+")");
			
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
