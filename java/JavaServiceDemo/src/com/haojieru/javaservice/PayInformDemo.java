package com.haojieru.javaservice;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.haojieru.utilDemo.SecurityUtil;

/**
 * Servlet implementation class JavaServiceDemo
 */
/**   
*    
* 项目名称：JavaServiceDemo   
* 类名称：PayInformDemo   
* 类描述：   
* 创建人：haojieru 
* 创建时间：2015年4月9日 下午2:43:01   
* 修改人：haojieru   
* 修改时间：2015年4月9日 下午2:43:01      
* @version    
*    
*/
public class PayInformDemo extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static final String security_key = "abcdefghijk";
  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	}

	//这是一个支付通知的demo，游戏方发送数据，服务器验证的demo。
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String serial_number = request.getParameter("serial_number");
		String cp = request.getParameter("cp");
		String timestamp = request.getParameter("timestamp");
		String result = request.getParameter("result");
		String extend = request.getParameter("extend");
		String server = request.getParameter("server");
		String product_id = request.getParameter("product_id");
		String product_num = request.getParameter("product_num");
		String game_orderno = request.getParameter("game_orderno");
		String amount = request.getParameter("amount");
		String sign = request.getParameter("sign");
		
		Map<String, String> map = new TreeMap<String, String>(new Comparator<String>() {
			public int compare(String obj1, String obj2) {
				// 降序排序
				return obj1.compareTo(obj2);
			}
		});
		
		map.put("serial_number", serial_number);
		map.put("cp", cp);
		map.put("timestamp", timestamp);
		map.put("result", result);
		map.put("extend", extend);
		map.put("server", server);
		map.put("product_id", product_id);
		map.put("product_num", product_num);
		map.put("game_orderno", game_orderno);
		map.put("amount", amount);
		
		StringBuffer data = new StringBuffer();
		Set<Entry<String,String>> entries = map.entrySet();
		for (Entry<String, String> entry : entries) { 
			data.append("&"+entry.getKey()+"="+URLEncoder.encode(entry.getValue(),"UTF-8"));//拼接sign需要的字符串
		}
		System.out.println("data : "+data.toString().substring(1));
		String mysign = SecurityUtil.getMD5Str(SecurityUtil.getMD5Str(data.toString().substring(1))+security_key);//生成sign
		
		if(mysign.equals(sign)) {
			request.setAttribute("result", "{“result”:“0”,”result_desc”:“ok”}");
		}else {
			request.setAttribute("result", "{“result”:“1”,”result_desc”:“fail reason”}");
		}
		
	}
}
