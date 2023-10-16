package com.ndt.oneClickGenerico.storedProcedure;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import com.ndt.oneClickGenerico.model.PagoRequestModel;

public class InsertTransaccionStoredProcedure {

	private CallableStatement callableStatement = null;
	private Connection conn = null;

	public InsertTransaccionStoredProcedure(Connection conn) throws SQLException {

		this.conn = conn;
		this.conn.setAutoCommit(true);
		callableStatement = this.conn.prepareCall("{call oneclick.insert_transacciones(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) }");

	}

	public Map<String, Object> execute(String id, String empresa, String identificador, String correo, String tbkUser,
			Long amount, String transaction_order, String transaction_type, String sucursal, String url_callback,
			String estado, String accounting_date, String buy_order, String transaction_date, String card_number,
			Long detalle_amount, String detalle_authorization_code, String detalle_buy_order,
			String detalle_commerce_code, String detalle_installments_number, String detalle_payment_type_code,
			String detalle_status, String detalle_code) {

		Map<String, Object> outParams = new HashMap<String, Object>();

		try {

			callableStatement.setString(1, id);
			callableStatement.setString(2, empresa);
			callableStatement.setString(3, identificador);
			callableStatement.setString(4, correo);
			callableStatement.setString(5, tbkUser);
			callableStatement.setLong(6, amount);
			callableStatement.setString(7, transaction_order);
			callableStatement.setString(8, transaction_type);
			callableStatement.setString(9, sucursal);
			callableStatement.setString(10, url_callback);
			callableStatement.setString(11, estado);
			callableStatement.setString(12, accounting_date);
			callableStatement.setString(13, buy_order);
			callableStatement.setString(14, transaction_date);
			callableStatement.setString(15, card_number);
			callableStatement.setLong(16, detalle_amount);
			callableStatement.setString(17, detalle_authorization_code);
			callableStatement.setString(18, detalle_buy_order);
			callableStatement.setString(19, detalle_commerce_code);
			callableStatement.setString(20, detalle_installments_number);
			callableStatement.setString(21, detalle_payment_type_code);
			callableStatement.setString(22, detalle_status);
			callableStatement.setString(23, detalle_code);

			callableStatement.registerOutParameter(1, Types.VARCHAR);
			callableStatement.registerOutParameter(2, Types.VARCHAR);

			System.out.println("call oneclick.insert_transacciones(" + id + "," + empresa + "," + identificador + ","
					+ correo + "," + tbkUser + "," + amount + "," + transaction_order + "," + transaction_type + ","
					+ sucursal + "," + url_callback + "," + estado + "," + accounting_date + "," + buy_order + ","
					+ transaction_date + "," + card_number + "," + detalle_amount + "," + detalle_authorization_code
					+ "," + detalle_buy_order + "," + detalle_commerce_code + "," + detalle_installments_number + ","
					+ detalle_payment_type_code + "," + detalle_status + "," + detalle_code + ")");

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

				if (callableStatement != null)
					callableStatement.close();

				if (this.conn != null)
					this.conn.close();

			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

		return outParams;

	}
}
