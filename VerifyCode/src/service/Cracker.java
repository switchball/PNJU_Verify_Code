package service;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

public class Cracker {
	String username;
	String password;
	String code;
	List<NameValuePair> params;
	
	public void run() {
		initParams();
	}

	private void initParams() {
		params = new LinkedList<NameValuePair>();
		params.add(new BasicNameValuePair("username", username));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("code", code));
		
	}
}
