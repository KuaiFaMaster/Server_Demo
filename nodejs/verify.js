/**
 * 登陆验证demo
 * 注意：请开发方根据自己的代码需求进行优化
 */

var http = require('http');
var querystring = require('querystring');
var util = require('util');
var crypto = require('crypto');
var url = require("url");

var makeSign = function (params, secretKey)
{
    var keys = [];
    for ( key in params ){
        keys.push(key);
    }
    keys = keys.sort();
    var obj = {};
    for (var i=0; i < keys.length; i++){
        obj[keys[i]] = params[keys[i]];
    }
    var str = querystring.stringify(obj);
    return getMd5( getMd5(str) + secretKey );
};

var getMd5 = function (str)
{
    var content = new Buffer(str).toString("binary");//处理中文问题
    var md5 = crypto.createHash('md5');
    md5.update(content);
    return md5.digest('hex').toLowerCase();
};

var secretKey = '4wt34g54yu46';
var requestUrl = 'http://anyapi.mobile.youxigongchang.com/foreign/oauth/verification2.php';

var urlData = url.parse(requestUrl);
console.log(urlData);



var data = {
    'gamekey' : 'd128360c051b94077118048bf92457fd',
    'openid' : '14a33489c6bc0e7bc3e1590d891b477e',
    'timestamp' : parseInt(Date.now()/1000),
    'token' : '0cb4ad8c6102092c7841e66d3748f98e',
};

data['_sign'] = makeSign(data, secretKey);
console.log(data);
var qs = querystring.stringify(data);
var opt = {  
    method: "POST",
    host: urlData.host,
    port: urlData.port,
    path: urlData.path,
    headers: {  
        "Content-Type": 'application/x-www-form-urlencoded',
        "Content-Length": qs.length
    }
};

var req = http.request(opt, function(res){ 
    var body = '';
    res.on('data', function(buffer){
        body += buffer;
        console.log("Partial body: " + body);
    });
    res.on('end', function () {
        console.log("Body: " + body);
        //{"result":"0", "result_desc":"ok"} 登陆成功，其他登陆失败，请自行
        var result = JSON.parse(body);
        if(result.result == '0'){
            console.log('login success');
        }else{
            console.log('login fail');
        }
    });

}).on('error', function(e) {
    console.log("Got error: " + e.message);
});

req.write(qs);
req.end();
