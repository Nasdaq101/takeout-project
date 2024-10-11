package com.sky.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "sky.wechat")
@Data
public class WeChatProperties {

    private String appid; //appid
    private String secret; //app secret
    private String mchid; //merchant id
    private String mchSerialNo; //merchant serial number
    private String privateKeyFilePath; //merchant private key
    private String apiV3Key; //decrypted key for certificate
    private String weChatPayCertFilePath; //certificate
    private String notifyUrl; //successful payment notification url
    private String refundNotifyUrl; //successful refund notification url

}
