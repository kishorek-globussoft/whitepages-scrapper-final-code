/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package whitepages.utility;

import java.io.IOException;
import java.util.Random;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author GLB-132
 */
public class FetchPageSourceWithProxy {

//    public static String fetchPageSourceWithProxy(String pageurl) throws IOException {
//        CredentialsProvider credsprovider = new BasicCredentialsProvider();
//        int portNo = generateRandomPort();
//        credsprovider.setCredentials(
//                new AuthScope("95.85.29.99", portNo),
//                new UsernamePasswordCredentials("mongoose", "Fjh30fi"));
//        HttpHost proxy = new HttpHost("95.85.29.99", portNo);
//        // String userAgent = UserAgents.getRandomUserAgent();
//        CloseableHttpClient httpclient = HttpClients.custom()
//                .setDefaultCredentialsProvider(credsprovider)
//                // .setUserAgent(userAgent)
//                .setProxy(proxy)
//                .build();
//        String responsebody = "";
//        //    String responsestatus=null;
//        try {
//            HttpGet httpget = new HttpGet(pageurl);
//            //System.out.println("Response status" + httpget.getRequestLine());
//            CloseableHttpResponse resp = httpclient.execute(httpget);
//            //       responsestatus=resp.getStatusLine().toString();
//            HttpEntity entity = resp.getEntity();
//            //System.out.println(resp.getStatusLine());
//            if (entity != null) {
//                //System.out.println("Response content length: " + entity.getContentLength());
//                BufferedReader in = new BufferedReader(new InputStreamReader(entity.getContent()));
//                String inputLine;
//                while ((inputLine = in.readLine()) != null) {
//                    responsebody = new StringBuilder().append(responsebody).append(inputLine).toString();
//                }
//            }
//            EntityUtils.consume(entity);
//        } finally {
//            httpclient.close();
//        }
//        return responsebody;
//    }
  public static String fetchPageSourceWithProxy(String pageurl) throws IOException {
        //System.out.println("---------------Without Proxy-----------------");
        String responsebody = "";
        //String userAgent = UserAgents.getRandomUserAgent();

        int portno = 1606;  //1601 2730
        CredentialsProvider credsprovider = new BasicCredentialsProvider();
        credsprovider.setCredentials(
                new AuthScope("95.85.29.99", portno),
                new UsernamePasswordCredentials("mongoose", "Fjh30fi"));
        HttpHost proxy = new HttpHost("95.85.29.99", portno);   //95.85.29.99 198.199.126.6
        RequestConfig requestConfig = RequestConfig.copy(RequestConfig.DEFAULT)
                .setSocketTimeout(900000000)
                .setConnectTimeout(900000000)
                .setConnectionRequestTimeout(900000000)
                .build();
        try (CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCredentialsProvider(credsprovider)
                .setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:28.0) Gecko/20100101 Firefox/28.0")
                .setProxy(proxy)
                .setDefaultRequestConfig(requestConfig)
                .build()) {
            HttpGet httpget = new HttpGet(pageurl);

            httpget.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            httpget.setHeader("Accept-Encoding", "gzip, deflate");
            httpget.setHeader("Accept-Language", "en-US,en;q=0.5");
            //httpget.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpget.setHeader("Referer", pageurl);

            //System.out.println("executing request " + httpget.getURI());
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                @Override
                public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status == 200) {
                        //System.out.println("status code " + status);

                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        //  handleResponse(response);
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }
            };
            responsebody = httpclient.execute(httpget, responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
//            fetchGoogleIndex(pageurl);
        }
        return responsebody;
  }
  
  // NOTE ---->>>>> If you are running the glassdoor crawler then use this code 
  
  
//     public static String fetchPageSourceWithProxy(String pageurl) throws IOException {
//        CredentialsProvider credsprovider = new BasicCredentialsProvider();
//        int portNo = generateRandomPort();
//        credsprovider.setCredentials(
//                new AuthScope("95.85.29.99", portNo),
//                new UsernamePasswordCredentials("mongoose", "Fjh30fi"));
//        HttpHost proxy = new HttpHost("95.85.29.99", portNo);
//        // String userAgent = UserAgents.getRandomUserAgent();
//        CloseableHttpClient httpclient = HttpClients.custom()
//                .setDefaultCredentialsProvider(credsprovider)
//                // .setUserAgent(userAgent)
//                .setProxy(proxy)
//                .build();
//        String responsebody = "";
//        //    String responsestatus=null;
//        try {
//            HttpGet httpget = new HttpGet(pageurl);
//            //System.out.println("Response status" + httpget.getRequestLine());
//            CloseableHttpResponse resp = httpclient.execute(httpget);
//            //       responsestatus=resp.getStatusLine().toString();
//            HttpEntity entity = resp.getEntity();
//            //System.out.println(resp.getStatusLine());
//            if (entity != null) {
//                //System.out.println("Response content length: " + entity.getContentLength());
//                BufferedReader in = new BufferedReader(new InputStreamReader(entity.getContent()));
//                String inputLine;
//                while ((inputLine = in.readLine()) != null) {
//                    responsebody = new StringBuilder().append(responsebody).append(inputLine).toString();
//                }
//            }
//            EntityUtils.consume(entity);
//        } finally {
//            httpclient.close();
//        }
//        return responsebody;
//    }
  
  
  //  END CODE for glassdoor crawler

//        CredentialsProvider credsprovider = new BasicCredentialsProvider();
//        int portNo = generateRandomPort();
//        credsprovider.setCredentials(
//                new AuthScope("95.85.29.99", portNo),
//                new UsernamePasswordCredentials("mongoose", "Fjh30fi"));
//        HttpHost proxy = new HttpHost("95.85.29.99", portNo);
//        // String userAgent = UserAgents.getRandomUserAgent();
//        CloseableHttpClient httpclient = HttpClients.custom()
//                .setDefaultCredentialsProvider(credsprovider)
//                // .setUserAgent(userAgent)
//                .setProxy(proxy)
//                .build();
//        String responsebody = "";
//        //    String responsestatus=null;
//        try {
//            HttpGet httpget = new HttpGet(pageurl);
//            //System.out.println("Response status" + httpget.getRequestLine());
//            CloseableHttpResponse resp = httpclient.execute(httpget);
//            //       responsestatus=resp.getStatusLine().toString();
//            HttpEntity entity = resp.getEntity();
//            //System.out.println(resp.getStatusLine());
//            if (entity != null) {
//                //System.out.println("Response content length: " + entity.getContentLength());
//                BufferedReader in = new BufferedReader(new InputStreamReader(entity.getContent()));
//                String inputLine;
//                while ((inputLine = in.readLine()) != null) {
//                    responsebody = new StringBuilder().append(responsebody).append(inputLine).toString();
//                }
//            }
//            EntityUtils.consume(entity);
//        } finally {
//            httpclient.close();
//        }
//        return responsebody;
    
  
  

    public static int generateRandomPort() {

        int portNo;
        Random random = new Random();
        int[] portList = new int[98];
        int portBegin = 1601;   //1601

        for (int k = 0; k < portList.length; k++) {
            portList[k] = portBegin;
            portBegin = portBegin + 1;
        }

        int num = random.nextInt(98);
        portNo = portList[num];
        return portNo;
    }

    public String fetchsource(String url) throws IOException {
        {
            try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
                HttpGet httpget = new HttpGet(url);
                //System.out.println("Executing request " + httpget.getRequestLine());
                // Create a custom response handler
                ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                    public String handleResponse(
                            final HttpResponse response) throws ClientProtocolException, IOException {
                        int status = response.getStatusLine().getStatusCode();
                        if (status >= 200 && status < 300) {
                            HttpEntity entity = response.getEntity();
                            return entity != null ? EntityUtils.toString(entity) : null;
                        } else {
                            throw new ClientProtocolException("Unexpected response status: " + status);
                        }
                    }
                };
                String respBody = httpclient.execute(httpget, responseHandler);
                //System.out.println(httpclient.execute(httpget, responseHandler).getBytes());
                return respBody;
            }
        }
    }

    public static String fetchSourceWithHeaderBBB(String pageurl) throws IOException {

        //System.out.println("---------------Without Proxy-----------------");
        String responsebody;
        //String userAgent = UserAgents.getRandomUserAgent();

        // int portno = generateRandomPort();
        int portno = generateRandomPort();  //1601   2733
        CredentialsProvider credsprovider = new BasicCredentialsProvider();
        credsprovider.setCredentials(
                new AuthScope("95.85.29.99", portno),
                new UsernamePasswordCredentials("mongoose", "Fjh30fi"));
        HttpHost proxy = new HttpHost("95.85.29.99", portno);   //95.85.29.99 198.199.126.6
        // String userAgent = HttpClintCode.getRandomUserAgent();
        RequestConfig requestConfig = RequestConfig.copy(RequestConfig.DEFAULT)
                .setSocketTimeout(900000000)
                .setConnectTimeout(900000000)
                .setConnectionRequestTimeout(900000000)
                .build();
        try (CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCredentialsProvider(credsprovider)
                .setUserAgent("Mozilla/5.0 (Windows NT 6.1; WOW64; rv:28.0) Gecko/20100101 Firefox/28.0")
                .setProxy(proxy)
                .setDefaultRequestConfig(requestConfig)
                .build()) {
            HttpGet httpget = new HttpGet(pageurl);

            httpget.setHeader("Host", "www.bbb.org");
            httpget.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
            httpget.setHeader("Accept-Encoding", "gzip,deflate");
            httpget.setHeader("Accept-Language", "en-US,en;q=0.5");
            //httpget.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpget.setHeader("Referer", pageurl);
            httpget.setHeader("X-Requested-With", "XMLHttpRequest");

            System.setProperty("jsse.enableSNIExtension", "false");

            //System.out.println("executing request " + httpget.getURI());
            ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
                @Override
                public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
                    int status = response.getStatusLine().getStatusCode();
                    if (status == 200) {
                        //System.out.println("status code " + status);

                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        //  handleResponse(response);
                        throw new ClientProtocolException("Unexpected response status: " + status);
                    }
                }
            };
            responsebody = httpclient.execute(httpget, responseHandler);
        }
        return responsebody;
    }

}
