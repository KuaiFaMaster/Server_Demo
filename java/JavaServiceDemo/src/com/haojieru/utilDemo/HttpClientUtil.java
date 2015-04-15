package com.haojieru.utilDemo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;



/**   
*    
* 项目名称：JavaServiceDemo   
* 类名称：HttpClientUtil   
* 类描述：   
* 创建人：haojieru 
* 创建时间：2015年4月9日 下午2:43:16   
* 修改人：admin   
* 修改时间：2015年4月9日 下午2:43:16      
* @version    
*    
*/
public class HttpClientUtil {
	
	private static Properties props = new Properties();
//	static {
//		// 把配置文件-->输入流-->props.load()，这就把文件中的内容加载到props中
//		InputStream in = ConnectionUtil.class.getClassLoader().getResourceAsStream("dbconfig.properties");
//		try {
//			props.load(in);
//			// 因为加载类只需要执行一次，所以放到static块中。
//			url = props.getProperty("PHPServer");
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
	
	public static String post(Map<String, String> params,String url) {
		HttpClient httpclient = new DefaultHttpClient();
		String body = null;
		
		HttpPost post = postForm(url, params);
		
		body = invoke(httpclient, post);
		
		httpclient.getConnectionManager().shutdown();
		
		return body;
	}
	
	public static String get(String url) {
		HttpClient httpclient = new DefaultHttpClient();
		String body = null;
		
		HttpGet get = new HttpGet(url);
		body = invoke(httpclient, get);
		
		httpclient.getConnectionManager().shutdown();
		
		return body;
	}
	
	
		
	
	private static String invoke(HttpClient httpclient,
			HttpUriRequest httpost) {
		
		HttpResponse response = sendRequest(httpclient, httpost);
		String body = paseResponse(response);
		
		return body;
	}

	private static String paseResponse(HttpResponse response) throws ParseException {
		HttpEntity entity = response.getEntity();
		
		String body = null;
		try {
			body = EntityUtils.toString(entity);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return body;
	}

	private static HttpResponse sendRequest(HttpClient httpclient,
			HttpUriRequest httpost) {
		HttpResponse response = null;
		
		try {
			response = httpclient.execute(httpost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	private static HttpPost postForm(String url, Map<String, String> params){
		
		HttpPost httpost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList <NameValuePair>();
		
		Set<String> keySet = params.keySet();
		for(String key : keySet) {
			nvps.add(new BasicNameValuePair(key, params.get(key)));
		}
		
		try {
			httpost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return httpost;
	}
}
