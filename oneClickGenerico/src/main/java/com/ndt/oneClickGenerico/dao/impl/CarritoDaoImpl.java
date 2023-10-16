package com.ndt.oneClickGenerico.dao.impl;

import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import com.ndt.oneClickGenerico.dao.CarritoDao;
import com.ndt.oneClickGenerico.model.ConsultTransactionRequestModel;
import com.ndt.oneClickGenerico.model.CreateInscriptionRequestModel;
import com.ndt.oneClickGenerico.model.DeleteInscriptionRequestModel;
import com.ndt.oneClickGenerico.model.GetCardsRequestModel;
import com.ndt.oneClickGenerico.model.PagoRequestModel;
import com.ndt.oneClickGenerico.storedProcedure.ConsultarTransaccionStoredProcedure;
import com.ndt.oneClickGenerico.storedProcedure.DejarRegistroCardStoredProcedure;
import com.ndt.oneClickGenerico.storedProcedure.DeleteDetalleCardStoredProcedure;
import com.ndt.oneClickGenerico.storedProcedure.InsertDetalleCardStoredProcedure;
import com.ndt.oneClickGenerico.storedProcedure.InsertIdentificadorStoredProcedure;
import com.ndt.oneClickGenerico.storedProcedure.InsertTransaccionStoredProcedure;
import com.ndt.oneClickGenerico.storedProcedure.ObtenerCardsStoredProcedure;
import com.ndt.oneClickGenerico.storedProcedure.ObtenerComercioStoredProcedure;
import com.ndt.oneClickGenerico.storedProcedure.ValidarEmpresaIdentificadorStoredProcedure;
import com.ndt.oneClickGenerico.storedProcedure.ValidarEmpresaStoredProcedure;

public class CarritoDaoImpl implements CarritoDao {

	private DataSource dataSourceBTN;
	
		public void setDataSourceBTN(DataSource dataSourceBTN) {
		this.dataSourceBTN = dataSourceBTN;
	}

	public Map<String, Object> obtenerCards(GetCardsRequestModel getCardsRequestModel) throws SQLException {
		// TODO Auto-generated method stub
		ObtenerCardsStoredProcedure obtenerCardStoredProcedure = new ObtenerCardsStoredProcedure(dataSourceBTN.getConnection());
		Map<String, Object> response = obtenerCardStoredProcedure.execute(getCardsRequestModel);
		return response;
	}

	public Map<String, Object> insertIdentificador(CreateInscriptionRequestModel createInscriptionRequestModel) throws SQLException {
		// TODO Auto-generated method stub
		InsertIdentificadorStoredProcedure insertIdentStoredProcedure = new InsertIdentificadorStoredProcedure(dataSourceBTN.getConnection());
		Map<String, Object> response = insertIdentStoredProcedure.execute(createInscriptionRequestModel);
		return response;
	}

	public Map<String, Object> insertDetalleCard(String id_cliente, String tbkUser, String authorizationCode,
			String cardType, String cardNumber) throws SQLException {
		// TODO Auto-generated method stub
		InsertDetalleCardStoredProcedure insertDetCardStoredProcedure = new InsertDetalleCardStoredProcedure(dataSourceBTN.getConnection());
		Map<String, Object> response = insertDetCardStoredProcedure.execute(id_cliente,tbkUser,authorizationCode,cardType,cardNumber);
		return response;
	}
	
	public Map<String, Object> validarEmpresa(String nombreEmpresa, String credencialEmpresa) throws SQLException {
		// TODO Auto-generated method stub
		ValidarEmpresaStoredProcedure validarEmpresaStoredProcedure = new ValidarEmpresaStoredProcedure(dataSourceBTN.getConnection());
		Map<String, Object> response = validarEmpresaStoredProcedure.execute(nombreEmpresa,credencialEmpresa);
		return response;
	}
	
	public Map<String, Object> validarEmpresaIdentificador(PagoRequestModel pagoRequestModel) throws SQLException {
		// TODO Auto-generated method stub
		ValidarEmpresaIdentificadorStoredProcedure validarEmpresaIdentificadorStoredProcedure = new ValidarEmpresaIdentificadorStoredProcedure(dataSourceBTN.getConnection());
		Map<String, Object> response = validarEmpresaIdentificadorStoredProcedure.execute(pagoRequestModel);
		return response;
	}
	
	public Map<String, Object> insertTransacciones(String id, String empresa, String identificador, String correo, String tbkUser, Long amount, String transaction_order, String transaction_type, String sucursal, String url_callback, String estado, String accounting_date, String buy_order, String transaction_date, String card_number, Long detalle_amount, String detalle_authorization_code, String detalle_buy_order, String detalle_commerce_code, String detalle_installments_number, String detalle_payment_type_code, String detalle_status, String detalle_code) throws SQLException {
		// TODO Auto-generated method stub
		InsertTransaccionStoredProcedure insertDetCardStoredProcedure = new InsertTransaccionStoredProcedure(dataSourceBTN.getConnection());
		Map<String, Object> response = insertDetCardStoredProcedure.execute(id,empresa,identificador,correo,tbkUser,amount,transaction_order,transaction_type,sucursal,url_callback,estado,accounting_date,buy_order,transaction_date,card_number,detalle_amount,detalle_authorization_code,detalle_buy_order,detalle_commerce_code,detalle_installments_number,detalle_payment_type_code,detalle_status,detalle_code);
		return response;
	}

	public Map<String, Object> obtenerComercio(PagoRequestModel pagoRequestModel) throws SQLException {
		// TODO Auto-generated method stub
		ObtenerComercioStoredProcedure obtenerComercioStoredProcedure = new ObtenerComercioStoredProcedure(dataSourceBTN.getConnection());
		Map<String, Object> response = obtenerComercioStoredProcedure.execute(pagoRequestModel);
		return response;
	}

	public Map<String, Object> dejarRegistro(Integer id, String servicio, String parametros_entrada, String parametros_salida) throws SQLException {
		// TODO Auto-generated method stub
		DejarRegistroCardStoredProcedure dejarRegistroCardStoredProcedure = new DejarRegistroCardStoredProcedure(dataSourceBTN.getConnection());
		Map<String, Object> response = dejarRegistroCardStoredProcedure.execute(id, servicio, parametros_entrada, parametros_salida);
		return response;
	}

	public Map<String, Object> eliminarTarjeta(DeleteInscriptionRequestModel deleteInscriptionRequestModel) throws SQLException {
		// TODO Auto-generated method stub
		DeleteDetalleCardStoredProcedure deleteDetalleCardStoredProcedure = new DeleteDetalleCardStoredProcedure(dataSourceBTN.getConnection());
		Map<String, Object> response = deleteDetalleCardStoredProcedure.execute(deleteInscriptionRequestModel);
		return response;
	}

	public Map<String, Object> consultarTransaccion(ConsultTransactionRequestModel consultTransactionRequestModel) throws SQLException {
		// TODO Auto-generated method stub
		ConsultarTransaccionStoredProcedure consultarTransaccionStoredProcedure = new ConsultarTransaccionStoredProcedure(dataSourceBTN.getConnection());
		Map<String, Object> response = consultarTransaccionStoredProcedure.execute(consultTransactionRequestModel);
		return response;
	}

}
