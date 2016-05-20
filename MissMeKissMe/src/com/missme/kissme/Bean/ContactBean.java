package com.missme.kissme.Bean;

import java.util.Comparator;

public class ContactBean {
	
	private String name;
	private String phoneNo;
	private String email;
	private String profileImg;
	
	private String srname;
	private String srphoneNo;
	private String srUserid;
	private String srprofileImg;
	private String srlat;
	private String srlangi;
	private String srInvite;
	private String srGpsStatus;
	private String srSentStatus;
	private String srSend;
	
	public String getSrSend() {
		return srSend;
	}
	public void setSrSend(String srSend) {
		this.srSend = srSend;
	}
	public String getSrSentStatus() {
		return srSentStatus;
	}
	public void setSrSentStatus(String srSentStatus) {
		this.srSentStatus = srSentStatus;
	}
	public String getSrGpsStatus() {
		return srGpsStatus;
	}
	public void setSrGpsStatus(String srGpsStatus) {
		this.srGpsStatus = srGpsStatus;
	}
	public String getSrInvite() {
		return srInvite;
	}
	public void setSrInvite(String srInvite) {
		this.srInvite = srInvite;
	}
	public String getSrlat() {
		return srlat;
	}
	public void setSrlat(String srlat) {
		this.srlat = srlat;
	}
	public String getSrlangi() {
		return srlangi;
	}
	public void setSrlangi(String srlangi) {
		this.srlangi = srlangi;
	}
	public String getSrname() {
		return srname;
	}
	public void setSrname(String srname) {
		this.srname = srname;
	}
	public String getSrphoneNo() {
		return srphoneNo;
	}
	public void setSrphoneNo(String srphoneNo) {
		this.srphoneNo = srphoneNo;
	}
	public String getSrUserid() {
		return srUserid;
	}
	public void setSrUserid(String srUserid) {
		this.srUserid = srUserid;
	}
	public String getSrprofileImg() {
		return srprofileImg;
	}
	public void setSrprofileImg(String srprofileImg) {
		this.srprofileImg = srprofileImg;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProfileImg() {
		return profileImg;
	}
	public void setProfileImg(String profileImg) {
		this.profileImg = profileImg;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public static class CompareTitle implements Comparator<ContactBean>{
		@Override
		public int compare(ContactBean lhs, ContactBean rhs) {
			return lhs.srInvite.compareToIgnoreCase(rhs.srInvite);
		}
	}
	
	public static class CompareTitleSend implements Comparator<ContactBean>{
		@Override
		public int compare(ContactBean lhs, ContactBean rhs) {
			return lhs.srSend.compareToIgnoreCase(rhs.srSend);
		}
	}
	
	public static class CompareName implements Comparator<ContactBean>{
		@Override
		public int compare(ContactBean lhs, ContactBean rhs) {
			return lhs.srname.compareToIgnoreCase(rhs.srname);
		}
	}
}
