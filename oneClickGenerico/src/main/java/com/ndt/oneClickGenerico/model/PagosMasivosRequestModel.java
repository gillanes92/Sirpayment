package com.ndt.oneClickGenerico.model;

import java.util.List;

public class PagosMasivosRequestModel {

	private String id_company;
	private String credential_id_company;
	private List<PagoAllRequestModel> payments;
	
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

	public List<PagoAllRequestModel> getPayments() {
		return payments;
	}

	public void setPayments(List<PagoAllRequestModel> payments) {
		this.payments = payments;
	}

	@Override
	public String toString() {
		return "PagosMasivosRequestModel [id_company=" + id_company + ", credential_id_company=" + credential_id_company
				+ ", payments=" + payments + "]";
	}

	
	
	
	
}
