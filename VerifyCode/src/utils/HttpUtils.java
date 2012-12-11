package utils;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

public class HttpUtils {

	public static final HttpClient getNewHttpClient() {
		DefaultHttpClient client = new DefaultHttpClient();
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, SOCKET_TIMEOUT);
		return client;
	}
	
	public static HttpResponse rawGet(HttpClient client, String url) {
		try {
			HttpGet request = new HttpGet(url);
			setHeader(request);
			HttpResponse response = client.execute(request);
			return response;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static void setHeader(HttpRequest request) {
//		request.setHeader("Host", "ojp.nationalrail.co.uk");
	    request.setHeader("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_4; en-US) AppleWebKit/534.10 (KHTML, like Gecko) Chrome/8.0.552.231 Safari/534.10");
//	    request.setHeader("Accept-Encoding", "gzip,deflate,sdch");
	    request.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,**/*//*;q=0.8");
	    request.setHeader("Accept-Language", "zh-CN,zh;q=0.8");
	    request.setHeader("Accept-Charset", "gbk,utf-8;q=0.7,*;q=0.7");
//	    request.setHeader("Origin", "http://www.nationalrail.co.uk/");
//	    request.setHeader("Referer", "http://www.nationalrail.co.uk/");
	    request.setHeader("Content-Type", "application/x-www-form-urlencoded");	//KEY
//	    request.setHeader("Cookie", "JSESSIONID=61E3E6D3A730A758AD83329F5E14C112");
	    
	}
	
	public static HttpResponse rawPost(HttpClient client, String url,
			List<NameValuePair> params) {
		try {
			HttpPost request = new HttpPost(url);
			setHeader(request);
			HttpEntity httpEntity = new UrlEncodedFormEntity(params, CHARSET_GBK);
			request.setEntity(httpEntity);
			HttpResponse response = client.execute(request);
			return response;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static HttpResponse rawPost(HttpClient client, String url,
			String sendData) {
		try {
			HttpPost request = new HttpPost(url);
			setHeader(request);
			HttpEntity httpEntity = new StringEntity(sendData, CHARSET_GBK);
			request.setEntity(httpEntity);
			HttpResponse response = client.execute(request);
			return response;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	public static int getStatusCodeFromResponse(HttpResponse response) {
		if (response == null) return -1;
		return response.getStatusLine().getStatusCode();
	}
	
	public static String getContentFromResponse(HttpResponse response) {
		if (response == null) return null;
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == HttpStatus.SC_OK) {
			String result;
			try {
				result = EntityUtils.toString(response.getEntity(), CHARSET_UTF_8);
				EntityUtils.consume(response.getEntity());
				return result;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public static String getContentFromGetMethod(HttpClient client, String url) {
		return getContentFromResponse(rawGet(client, url));
	}
	
	public static String getContentFromPostMethod(HttpClient client, String url,
			List<NameValuePair> params) {
		return getContentFromResponse(rawPost(client, url, params));
	}
	
	public static String getContentFromPostMethod(HttpClient client, String url,
			String params) {
		return getContentFromResponse(rawPost(client, url, params));
	}
	
	public static final int CONNECTION_TIMEOUT = 6000;
	public static final int SOCKET_TIMEOUT = 6000;
	public static final String CHARSET_UTF_8 = "UTF-8";
	public static final String CHARSET_GBK = "GBK";
}
