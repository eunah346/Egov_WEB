package com.lib.model;

public class UserVO {	// 멤버변수를 두고 값을 저장하고 꺼내는 용도
	
	private String userid;
	private String userpw;
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getUserpw() {
		return userpw;
	}
	public void setUserpw(String userpw) {
		this.userpw = userpw;
	}
}
