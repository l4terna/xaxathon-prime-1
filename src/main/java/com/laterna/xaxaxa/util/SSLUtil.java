package com.laterna.xaxaxa.util;

import javax.net.ssl.*;
import java.security.cert.X509Certificate;

public class SSLUtil {
    public static void disableSSLVerification() {
        try {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }

                        public void checkClientTrusted(X509Certificate[] certs, String authType) {
                            // Не выполняем проверку
                        }

                        public void checkServerTrusted(X509Certificate[] certs, String authType) {
                            // Не выполняем проверку
                        }
                    }
            };

            // Устанавливаем наш trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Создаем hostname verifier, который принимает все хосты
            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            // Устанавливаем hostname verifier по умолчанию
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}