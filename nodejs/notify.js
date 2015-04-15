/**
 * 充值回调通知demo
 * 注意：请开发方根据自己的代码需求进行优化
 */

var http = require('http');
var querystring = require('querystring');
var util = require('util');
var crypto = require('crypto');


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
    console.log('aastr:' + str);
    var content = new Buffer(str).toString("binary");//处理中文问题
    var md5 = crypto.createHash('md5');
    md5.update(content);
    return md5.digest('hex').toLowerCase();
};

var checkSign = function(data, secretKey)
{
    var sign = data.sign;
    delete data.sign;
    var mysign = makeSign(data, secretKey);
    if(sign == mysign){
        return true;
    }
    return false;
}


var secretKey = '4wt34g54yu46';
var notify = function(req, res){
    var post ='';
    req.addListener('data', function(chunk){
        post += chunk;
    });
    req.addListener('end', function(){
        console.log(post+"\n");
        post = querystring.parse(post);
        if(checkSign(post, secretKey)){
            //异步处理游戏支付发放道具逻辑
            //注通知的游戏金额会出现和游戏订单的金额不相同问题，主要原因是接入的渠道支持非定额支付造成，请接入方注意
            res.end(JSON.stringify({"result":0, "result_desc": "success"}));
        }else{
            res.end(JSON.stringify({"result":10001, "result_desc": "check Sign Error"}));
        }
    });
};

var server = http.createServer(notify);
server.listen(8888);
console.log('Server running at http://127.0.0.1:8888/');
