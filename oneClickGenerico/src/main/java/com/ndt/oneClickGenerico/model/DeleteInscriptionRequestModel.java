package com.ndt.oneClickGenerico.model;

public class DeleteInscriptionRequestModel {

	private String id_company;
	private String credential_id_company;
	private String id_client;
	private String email;
	
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
	@Override
	public String toString() {
		return "GetCardsRequestModel [id_company=" + id_company + ", credential_id_company=" + credential_id_company
				+ ", id_client=" + id_client + ", email=" + email + "]";
	}
	
	
	
}
