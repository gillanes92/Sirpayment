package com.ndt.oneClickGenerico.model;


public class PagoAllRequestModel {

	private String id_client;
	private String email;
	private Long amount;
	private String transaction_order;
	private String transaction_type;
	private String sucursal;
	private String url_callback;
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
	public String getUrl_callback() {
		return url_callback;
	}
	public void setUrl_callback(String url_callback) {
		this.url_callback = url_callback;
	}
	@Override
	public String toString() {
		return "PagoAllRequestModel [id_client=" + id_client + ", email=" + email + ", amount=" + amount
				+ ", transaction_order=" + transaction_order + ", transaction_type=" + transaction_type + ", sucursal="
				+ sucursal + ", url_callback=" + url_callback + "]";
	}
	
		
}
