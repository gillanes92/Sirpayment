package com.ndt.oneClickGenerico.model;


public class PagoRequestModel {

	private String id_company;
	private String credential_id_company;
	private String id_client;
	private String email;
	private Long amount;
	private String transaction_order;
	private String transaction_type;
	private String sucursal;
	
	public String getId_company() {
		return id_company;
	}
	public void setId_company(String id_company) {
		this.id_company = id_company;
	}
	public String getCredential_id_company() {
		return credential_id_company;
	}
	public void setCredential_id_company(String credential_id_company) {
		this.credential_id_company = credential_id_company;
	}
	public String getId_client() {
		return id_client;
	}
	public void setId_client(String id_client) {
		this.id_client = id_client;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Long getAmount() {
		return amount;
	}
	public void setAmount(Long amount) {
		this.amount = amount;
	}
	public String getTransaction_order() {
		return transaction_order;
	}
	public void setTransaction_order(String transaction_order) {
		this.transaction_order = transaction_order;
	}
	public String getTransaction_type() {
		return transaction_type;
	}
	public void setTransaction_type(String transaction_type) {
		this.transaction_type = transaction_type;
	}
	public String getSucursal() {
		return sucursal;
	}
	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}
	@Override
	public String toString() {
		return "PagoRequestModel [id_company=" + id_company + ", credential_id_company=" + credential_id_company
				+ ", id_client=" + id_client + ", email=" + email + ", amount=" + amount + ", transaction_order="
				+ transaction_order + ", transaction_type=" + transaction_type + ", sucursal=" + sucursal + "]";
	}
	
	
	
		
}
