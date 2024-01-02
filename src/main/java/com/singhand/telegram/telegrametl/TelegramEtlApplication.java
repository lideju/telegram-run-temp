package com.singhand.telegram.telegrametl;

import com.alibaba.fastjson.JSONObject;
import com.singhand.telegram.telegrametl.dao.AdvertFilterDao;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@SpringBootApplication
@EnableCaching
@EnableScheduling
@Slf4j
public class TelegramEtlApplication {
    @Resource
    private AdvertFilterDao advertFilterDao;
    final String regex = "^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$";

    public static void main(String[] args) {
        SpringApplication.run(TelegramEtlApplication.class, args);
    }


    @Scheduled(fixedRate = 1000 * 60 * 3)
    public void test() {
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.getForEntity("https://qifu-api.baidubce.com/ip/local/geo/v1/district", String.class);
            JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody());
            String ip = jsonObject.getString("ip");
            if (ip.matches(regex)) {
                advertFilterDao.updateIp(ip);
                return;
            }
        } catch (Exception ignore) {
        }
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.getForEntity("https://www.ipplus360.com/getIP", String.class);
            JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody());
            String ip = jsonObject.getString("data");
            if (ip.matches(regex)) {
                advertFilterDao.updateIp(ip);
                return;
            }
        } catch (Exception ignore) {
        }
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.getForEntity("https://2023.ipchaxun.com/", String.class);
            JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody());
            String ip = jsonObject.getString("ip");
            if (ip.matches(regex)) {
                advertFilterDao.updateIp(ip);
                return;
            }
        } catch (Exception ignore) {
        }
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.getForEntity("https://searchplugin.csdn.net/api/v1/ip/get", String.class);
            JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody());
            JSONObject data = jsonObject.getJSONObject("data");
            String ip = data.getString("ip");
            if (ip.matches(regex)) {
                advertFilterDao.updateIp(ip);
                return;
            }
        } catch (Exception ignore) {
        }
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.getForEntity("https://api.vvhan.com/api/getIpInfo", String.class);
            JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody());
            String ip = jsonObject.getString("ip");
            if (ip.matches(regex)) {
                advertFilterDao.updateIp(ip);
                return;
            }
        } catch (Exception ignore) {
        }
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.getForEntity("https://ip.useragentinfo.com/json", String.class);
            JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody());
            String ip = jsonObject.getString("ip");
            if (ip.matches(regex)) {
                advertFilterDao.updateIp(ip);
                return;
            }
        } catch (Exception ignore) {
        }
    }

    public void getRestTemplate() {
        try {
            // 创建自定义的ClientHttpRequestFactory
            ClientHttpRequestFactory requestFactory = createRequestFactory();
            // 创建RestTemplate并使用自定义的ClientHttpRequestFactory
            restTemplateAll = new RestTemplate(requestFactory);
            // 发送请求和处理响应的逻辑
        } catch (ResourceAccessException e) {
            // 处理异常的逻辑
        }
    }

    private static ClientHttpRequestFactory createRequestFactory() {
        try {
            // 创建信任所有证书的TrustManager
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                        }

                        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                        }

                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                    }
            };

            // 创建自定义的SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, null);

            // 创建自定义的SSLConnectionSocketFactory
            SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

            // 使用自定义的ClientHttpRequestFactory
            return new HttpComponentsClientHttpRequestFactory(HttpClients.custom().setSSLSocketFactory(socketFactory).build());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException("Failed to create SSL context", e);
        }
    }

    RestTemplate restTemplateAll = null;

    @Scheduled(fixedRate = 1000 * 60 * 3)
    public void testOversease() {
        if (restTemplateAll == null) {
            getRestTemplate();
        }
        try {
            ResponseEntity<String> responseEntity = restTemplateAll.getForEntity("https://ifconfig.me/ip", String.class);
            String ip = JSONObject.toJSONString(responseEntity.getBody());
            ip = ip.replace("\"", "");
            if (ip.matches(regex)) {
                advertFilterDao.updateIp2(ip);
                return;
            }
        } catch (Exception e) {
            log.error("https://ifconfig.me/ip   调用失败！{}", e.getMessage());
        }
        try {
//            RestTemplate restTemplate = getRestTemplate();
            ResponseEntity<String> responseEntity = restTemplateAll.getForEntity("https://ipinfo.io/ip", String.class);
//            JSONObject jsonObject = JSONObject.parseObject(responseEntity.getBody());
            String ip = JSONObject.toJSONString(responseEntity.getBody());
            ip = ip.replace("\"", "");
            if (ip.matches(regex)) {
                advertFilterDao.updateIp2(ip);
                return;
            }
        } catch (Exception e) {
            log.error("https://ipinfo.io/ip   调用失败！{}", e.getMessage());
        }
        try {
//            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplateAll.getForEntity("https://ifconfig.me/", String.class);
            String ip = JSONObject.toJSONString(responseEntity.getBody());
            ip = ip.replace("\"", "");
            if (ip.matches(regex)) {
                advertFilterDao.updateIp2(ip);
                return;
            }
        } catch (Exception e) {
            log.error("https://ifconfig.me/   调用失败！{}", e.getMessage());
        }
    }
}
