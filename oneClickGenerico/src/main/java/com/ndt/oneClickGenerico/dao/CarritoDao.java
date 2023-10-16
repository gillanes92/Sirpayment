package com.ndt.oneClickGenerico.dao;

import java.sql.SQLException;
import java.util.Map;

import com.ndt.oneClickGenerico.model.ConsultTransactionRequestModel;
import com.ndt.oneClickGenerico.model.CreateInscriptionRequestModel;
import com.ndt.oneClickGenerico.model.DeleteInscriptionRequestModel;
import com.ndt.oneClickGenerico.model.GetCardsRequestModel;
import com.ndt.oneClickGenerico.model.PagoRequestModel;

public interface CarritoDao {
	
	public Map<String, Object> obtenerCards(GetCardsRequestModel getCardsRequestModel) throws SQLException;
	
	public Map<String, Object> insertIdentificador(CreateInscriptionRequestModel createInscriptionRequestModel) throws SQLException;
	
	public Map<String, Object> insertDetalleCard(String identificador, String tbkUser, String authorizationCode,
			String cardType, String cardNumber) throws SQLException;
	
	public Map<String, Object> validarEmpresa(String nombreEmpresa, String credencialEmpresa) throws SQLException;
	
	public Map<String, Object> validarEmpresaIdentificador(PagoRequestModel pagoRequestModel) throws SQLException;
	
	public Map<String, Object> insertTransacciones(String id, String empresa, String identificador, String correo, String tbkUser, Long amount, String transaction_order, String transaction_type, String sucursal, String url_callback, String estado, String accounting_date, String buy_order, String transaction_date, String card_number, Long detalle_amount, String detalle_authorization_code, String detalle_buy_order, String detalle_commerce_code, String detalle_installments_number, String detalle_payment_type_code, String detalle_status, String detalle_code) throws SQLException;
	
	public Map<String, Object> obtenerComercio(PagoRequestModel pagoRequestModel) throws SQLException;
		
	public Map<String, Object> dejarRegistro(Integer id, String servicio, String parametros_entrada, String parametros_salida) throws SQLException;
	
	public Map<String, Object> eliminarTarjeta(DeleteInscriptionRequestModel deleteInscriptionRequestModel) throws SQLException;
	
	public Map<String, Object> consultarTransaccion(ConsultTransactionRequestModel consultTransactionRequestModel) throws SQLException;
}
