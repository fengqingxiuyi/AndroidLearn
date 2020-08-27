package com.example.social.pay;

import java.io.Serializable;

/**
 * 微信支付的请求数据结构
 */
public class WXPayBean implements Serializable {

    //商户号           微信支付分配的商户号
    public String partnerId;
    //预支付交易会话ID  微信返回的支付交易会话ID
    public String prepayId;
    //扩展字段         暂填写固定值Sign=WXPay
    public String nonceStr;
    //随机字符串       不长于32位。推荐随机数生成算法：https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=4_3
    public String timeStamp;
    //时间戳          请见接口规则-参数规定 https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=4_2
    public String packageValue;
    //签名            详见签名生成算法注意：签名方式一定要与统一下单接口使用的一致 https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=4_3
    public String sign;

}
