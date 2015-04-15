<?php
require_once 'config.inc';
require_once 'tool.php';


$params = array(
    'openid' => '14a33489c6bc0e7bc3e1590d891b477e',
    'token' => '0cb4ad8c6102092c7841e66d3748f98e',
    'timestamp' => time(),
    'gamekey' => $gameKey,
);
$params['_sign'] = Util::makeSign($params, $secretKey);

$url = 'http://anyapi.mobile.youxigongchang.com/foreign/oauth/verification2.php';

$retval = Util::request($url, $params, 'POST');
$result = (array) json_decode($retval, true);

if(isset($result['result']) && $result['result'] == 0){
    //登陆成功
}else{
    //登陆失败
}
/*
$result = array (
  'result' => '0',//通过验证
  'result_desc' => 'ok',
);
 */
