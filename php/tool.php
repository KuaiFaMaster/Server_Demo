<?php
class Util{

    /**
     * 发起一个HTTP/HTTPS的请求
     * @param $url 接口的URL 
     * @param $params 接口参数   array('content'=>'test', 'format'=>'json');
     * @param $method 请求类型    GET|POST
     * @param $multi 图片信息
     * @param $extheaders 扩展的包头信息
     * @return string
     */
    public static function request( $url , $params = array(), $method = 'GET' , $multi = false, $extheaders = array())
    {
        if(!function_exists('curl_init')) exit('Need to open the curl extension');
        $method = strtoupper($method);
        $ci = curl_init();
        curl_setopt($ci, CURLOPT_USERAGENT, 'PHP-SDK OAuth2.0');
        curl_setopt($ci, CURLOPT_CONNECTTIMEOUT, 3);
        curl_setopt($ci, CURLOPT_TIMEOUT, 3);
        curl_setopt($ci, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ci, CURLOPT_SSL_VERIFYPEER, false);
        curl_setopt($ci, CURLOPT_SSL_VERIFYHOST, false);
        curl_setopt($ci, CURLOPT_HEADER, false);
        $headers = (array)$extheaders;
        switch ($method)
        {
            case 'POST':
                curl_setopt($ci, CURLOPT_POST, TRUE);
                if (!empty($params))
                {
                    if($multi)
                    {
                        foreach($multi as $key => $file)
                        {
                            $params[$key] = '@' . $file;
                        }
                        curl_setopt($ci, CURLOPT_POSTFIELDS, $params);
                        $headers[] = 'Expect: ';
                    }
                    else
                    {
                        curl_setopt($ci, CURLOPT_POSTFIELDS, self::buildQuery($params));
                    }
                }
                break;
            case 'DELETE':
            case 'GET':
                $method == 'DELETE' && curl_setopt($ci, CURLOPT_CUSTOMREQUEST, 'DELETE');
                if (!empty($params))
                {
                    $url = $url . (strpos($url, '?') ? '&' : '?')
                        . (is_array($params) ? self::buildQuery($params) : $params);
                }
                break;
        }
        curl_setopt($ci, CURLINFO_HEADER_OUT, TRUE );
        curl_setopt($ci, CURLOPT_URL, $url);
        if($headers)
        {
            curl_setopt($ci, CURLOPT_HTTPHEADER, $headers );
        }

        $response = curl_exec($ci);
        curl_close ($ci);
        return $response;
    }

    /**
     * 数组转成字符串
     * @param array $data
     * @param string $prefix
     * @param string $sep
     * @param string $key
     */
    public static function buildQuery($data, $prefix = '', $sep = '', $key = '')
    {
        $ret = array();
        foreach ((array) $data as $k => $v)
        {
            if (is_int($k) && $prefix != null){
                $k = rawurlencode($prefix . $k);
            }
            if ((!empty($key)) || ($key === 0))
                $k = $key . '[' . rawurlencode($k) . ']';
            if (is_array($v) || is_object($v))
            {
                array_push($ret, call_user_func(__METHOD__, $v, '', $sep, $k));
            }
            else
            {
                array_push($ret, $k . '=' . rawurlencode($v));
            }
        }
        if (empty($sep))
            $sep = '&';
        return implode($sep, $ret);
    }


    /**
     * 生成签名
     * @param array $data
     * @param string $key
     */
    public static function makeSign($data, $key)
    {
        ksort($data);
        $queryString = self::buildQuery($data);
        $signStr = md5($queryString);
        return md5($signStr . $key);
    }
}
