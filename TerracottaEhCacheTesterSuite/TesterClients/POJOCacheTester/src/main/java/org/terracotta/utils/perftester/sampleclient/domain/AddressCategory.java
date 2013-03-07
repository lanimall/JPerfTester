package org.terracotta.utils.perftester.sampleclient.domain;

import java.io.Serializable;

public class AddressCategory implements Serializable {
    private static final long serialVersionUID = 1L;
	private String type;
	private String subType;
	
	public static final String[] valuesCategoryType = {"commercial","residential"};
	public static final String[] valuesCategorySubType = {"apt","house","condo", "high rise"};
	
	public AddressCategory(){
		super();
	}
	
	public AddressCategory(String type, String subType) {
		super();
		this.type = type;
		this.subType = subType;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}
}