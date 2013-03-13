package org.terracotta.utils.perftester.cache.customcachesearchtester.domain;

import java.io.Serializable;

public class ComplexCacheKey implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String cUrl;
	private String qStr;
	private String nOid;
	private String aOid;
	private String uOid;
	private String iOid;
	
	public ComplexCacheKey() {
		super();
	}

	public ComplexCacheKey(String cUrl, String qStr, String nOid, String aOid,
			String uOid, String iOid) {
		super();
		this.cUrl = cUrl;
		this.qStr = qStr;
		this.nOid = nOid;
		this.aOid = aOid;
		this.uOid = uOid;
		this.iOid = iOid;
	}

	public String getcUrl() {
		return cUrl;
	}

	public void setcUrl(String cUrl) {
		this.cUrl = cUrl;
	}

	public String getqStr() {
		return qStr;
	}

	public void setqStr(String qStr) {
		this.qStr = qStr;
	}

	public String getnOid() {
		return nOid;
	}

	public void setnOid(String nOid) {
		this.nOid = nOid;
	}

	public String getaOid() {
		return aOid;
	}

	public void setaOid(String aOid) {
		this.aOid = aOid;
	}

	public String getuOid() {
		return uOid;
	}

	public void setuOid(String uOid) {
		this.uOid = uOid;
	}

	public String getiOid() {
		return iOid;
	}

	public void setiOid(String iOid) {
		this.iOid = iOid;
	}

	@Override
	public String toString() {
		return "CacheKey [cUrl=" + cUrl + ", qStr=" + qStr + ", nOid=" + nOid
				+ ", aOid=" + aOid + ", uOid=" + uOid + ", iOid=" + iOid + "]";
	}
}
