package guru.springframework.spring5webapp;

import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class RestTemplateUtil {

    public static RestTemplate getRestTemplate(){
        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setConnectionRequestTimeout(10000);
        requestFactory.setConnectTimeout(10000);
        RestTemplate template = new RestTemplate(requestFactory);
        template.setErrorHandler(new AccepResErrorHandler());
        return template;
    }

    public static RestTemplate getRestTemplate2()
            throws Exception {
        TrustStrategy acceptingTrustStrategy = new TrustStrategy() {
            @Override
            public boolean isTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                return true;
            }
        };

        SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
                .loadTrustMaterial(null, acceptingTrustStrategy)
                .build();

        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

        CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLSocketFactory(csf)
                .build();

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        requestFactory.setConnectionRequestTimeout(10000);
        requestFactory.setConnectTimeout(10000);

        RestTemplate template = new RestTemplate(requestFactory);
        template.setErrorHandler(new AccepResErrorHandler());
        return template;
    }

    static class AccepResErrorHandler implements ResponseErrorHandler{

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            return false;
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {

        }
    }

    public static void main(String[] args) throws Exception{
        RestTemplate t = getRestTemplate2();
        String url = "https://shop.vivo.com.cn/wap/fbApi/v1/comment/query-list?type=3&spuId=10009608&sortOrder=hot";
        String resp = t.getForObject(url, String.class);
        System.out.println(resp);
    }
}
