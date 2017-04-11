/**
 * 
 */
package com.haojieru.javaservice;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;



import com.haojieru.utilDemo.HttpClientUtil;
import com.haojieru.utilDemo.SecurityUtil;


/**   
*    
* 项目名称：JavaServiceDemo   
* 类名称：LoginVerify   
* 类描述：   
* 创建人：haojieu 
* 创建时间：2015年4月9日 下午2:42:42   
* 修改人：admin   
* 修改时间：2015年4月9日 下午2:42:42      
* @version    
*    
*/
public class LoginVerify {
	private static final String security_key = "abcdef";
	private static final String url = "http://z.kuaifazs.com/foreign/oauth/verification2.php";
	
	
	public static void loginCheck() throws UnsupportedEncodingException {
		Map<String, String> map = new TreeMap<String, String>(new Comparator<String>() {
			public int compare(String obj1, String obj2) {
				// 降序排序
				return obj1.compareTo(obj2);
			}
		});
		//初始化
		map.put("token", "0cb4ad8c6102092c7841e66d3748f98e");  //放真实token
		map.put("openid", "14a33489c6bc0e7bc3e1590d891b477e"); //放真实openid
		map.put("timestamp", String.valueOf(new Date().getTime()/1000));
		map.put("gamekey", "d128360c051b94077118048bf92457fd"); //放真实gamekey
		
		StringBuffer data = new StringBuffer();
		Map<String,String> params = new HashMap<String,String>(); //存储post提交的参数
		
		Set<Entry<String,String>> entries = map.entrySet();
		for (Entry<String, String> entry : entries) { 
			params.put(entry.getKey(), entry.getValue()); //为post设置参数
			data.append("&"+entry.getKey()+"="+URLEncoder.encode(entry.getValue(),"UTF-8"));//拼接sign需要的字符串
		}
		String sign = SecurityUtil.getMD5Str(SecurityUtil.getMD5Str(data.toString().substring(1))+security_key);//生成sign
		params.put("_sign", sign); //将sign放入post参数中
		
		String result = HttpClientUtil.post(params, url); //请求接口，并得到返回的结果。
		System.out.println(result);
	}
}
