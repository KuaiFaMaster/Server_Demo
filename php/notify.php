<?php
require_once 'config.inc';
require_once 'tool.php';

$_POST = array (
  'amount' => '6.00',
  'cp' => 'hjr',
  'extend' => '扩展',
  'game_orderno' => 'OS_06ZT2BR1HDME2ZG1C',
  'product_id' => '1',
  'product_num' => '1',
  'result' => '0',
  'serial_number' => '1504097600007001',
  'server' => '2033310190',
  'timestamp' => '1428508875',
  'sign' => '1f8b151517e392e29404e65863ba769e',
);

$params = $_POST;

$sign = $params['sign'];
unset($params['sign']);

if($sign == Util::makeSign($params, $secretKey)){
    //特别注意为了适配不定额充值，通知游戏的金额为渠道实际金额，可能与游戏的订单金额不一致，请游戏方根据实际清楚处理
    /**
     *游戏方处理自己的业务
     */
    echo json_encode(array('result' => 0, 'result_desc' => 'ok'));
}else{
    echo json_encode(array('result' => 1, 'result_desc' => 'sign error'));
}