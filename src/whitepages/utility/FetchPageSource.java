/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package whitepages.utility;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
import org.hibernate.SessionFactory;
import whitepages.dao.PeopleDao;
import whitepages.entity.ProxyData;
import whitepages.gui.WhitepagesGui;

/**
 *
 * @author GLB-302
 */
public class FetchPageSource {

    static int tryCount = 0;

    public static String fetchPageSource(final String pageurl, final int proxyType, final SessionFactory factory) throws IOException {
        String responsebody = null;
        if (proxyType == 0) {
            BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.home") + "\\Desktop\\Whitepages-scraper\\proxy.txt"));
            String line = reader.readLine();
            List<String> lines = new ArrayList<>();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            reader.close();
            Random r = new Random();

            String[] randomLine = (lines.get(r.nextInt(lines.size()))).split(":");
            String ip = randomLine[0];
            int port = Integer.parseInt(randomLine[1]);
            String username = "";
            String password = "";

            if (randomLine.length == 4) {
                username = randomLine[2];
                password = randomLine[3];
            }
//            WhitepagesGui.outputText.append("\nProxy Details : " + randomLine[0] + " " + randomLine[1]);
            CredentialsProvider credsprovider = new BasicCredentialsProvider();
            credsprovider.setCredentials(
                    new AuthScope(ip, port),
                    new UsernamePasswordCredentials(username, password));
            HttpHost proxy = new HttpHost(ip, port);   //95.85.29.99 198.199.126.6
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
                            HttpEntity entity = response.getEntity();
                            return entity != null ? EntityUtils.toString(entity) : null;
                        } else {
//                            WhitepagesGui.errorLogText.append("\nProxy Failed. Trying again");
                            String result = secondTry(pageurl, proxyType, factory);
                            return result;
                        }
                    }
                };
                responsebody = httpclient.execute(httpget, responseHandler);

            } catch (Exception e) {
                WhitepagesGui.errorLogText.append("\nProxy Failure : " + ip + " " + port);
//                PeopleDao dao = new PeopleDao(factory);
//                dao.updateProxyData(ip, port);
                String result = secondTry(pageurl, proxyType, factory);
                return result;
            }
            return responsebody;

        } else {
            //scrape using uploaded proxy data
            PeopleDao dao = new PeopleDao(factory);
            List<ProxyData> proxyList = dao.getProxyData();
            Random r = new Random();

            ProxyData proxyData = (proxyList.get(r.nextInt(proxyList.size())));
            String ip = proxyData.getIp();
            int port = proxyData.getPort();
            String username = proxyData.getUserName();
            String password = proxyData.getPassword();
//            WhitepagesGui.outputText.append("\nProxy Details : " + proxyData.getIp() + " " + proxyData.getPort());
            CredentialsProvider credsprovider = new BasicCredentialsProvider();
            credsprovider.setCredentials(
                    new AuthScope(ip, port),
                    new UsernamePasswordCredentials(username, password));
            HttpHost proxy = new HttpHost(ip, port);   //95.85.29.99 198.199.126.6
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
                            System.out.println(status);
                            HttpEntity entity = response.getEntity();
                            return entity != null ? EntityUtils.toString(entity) : null;
                        } else {
//                            WhitepagesGui.errorLogText.append("\nProxy Failed. Trying again");
                            System.out.println("no data");
                            String result = secondTry(pageurl, proxyType, factory);
                            return result;
                        }
                    }
                };
                responsebody = httpclient.execute(httpget, responseHandler);

            } catch (Exception e) {
                while (tryCount < 5) {
                    proxyList = dao.getProxyData();
                    proxyData = (proxyList.get(r.nextInt(proxyList.size())));
                    String result = retry(pageurl, proxyData, factory);
                    if (result != null || !result.isEmpty()) {
                        tryCount = 0;
                        return result;
                    } else {
                        tryCount++;
                        dao.updateProxyData(proxyData.getIp(), proxyData.getPort());
                    }
                }
            }
            return responsebody;
        }
    }

    private static String secondTry(final String pageurl, final int proxyType, SessionFactory factory) throws IOException {
        String responsebody = null;
        if (proxyType == 0) {
            BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.home") + "\\Desktop\\Whitepages-scraper\\proxy.txt"));
            String line = reader.readLine();
            List<String> lines = new ArrayList<>();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            System.out.println(line);
            reader.close();
            Random r = new Random();

            String[] randomLine = (lines.get(r.nextInt(lines.size()))).split(":");
            String ip = randomLine[0];
            int port = Integer.parseInt(randomLine[1]);
            String username = "";
            String password = "";

            if (randomLine.length == 4) {
                username = randomLine[2];
                password = randomLine[3];
            }
//            WhitepagesGui.outputText.append("\nProxy Details : " + randomLine[0] + " " + randomLine[1]);
            CredentialsProvider credsprovider = new BasicCredentialsProvider();
            credsprovider.setCredentials(
                    new AuthScope(ip, port),
                    new UsernamePasswordCredentials(username, password));
            HttpHost proxy = new HttpHost(ip, port);   //95.85.29.99 198.199.126.6
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
                            HttpEntity entity = response.getEntity();
                            return entity != null ? EntityUtils.toString(entity) : null;
                        } else {
                            return null;
                        }
                    }
                };
                responsebody = httpclient.execute(httpget, responseHandler);

            } catch (Exception e) {
                e.printStackTrace();
                WhitepagesGui.errorLogText.append("\nProxy Failure : " + ip + " " + port);
                PeopleDao dao = new PeopleDao(factory);
                dao.updateProxyData(ip, port);
                return null;
            }
            return responsebody;

        } else {
            Random r = new Random();
            PeopleDao dao = new PeopleDao(factory);
            while (tryCount < 5) {
                List<ProxyData> proxyList = dao.getProxyData();
                ProxyData proxyData = (proxyList.get(r.nextInt(proxyList.size())));
                String result = retry(pageurl, proxyData, factory);
                if (result != null || !result.isEmpty()) {
                    tryCount = 0;
                    responsebody = result;
                    return result;
                } else {
                    tryCount++;
                    dao.updateProxyData(proxyData.getIp(), proxyData.getPort());
                }
            }
        }
        return responsebody;
    }

    private static String retry(final String pageurl, ProxyData proxyData, SessionFactory factory) throws IOException {
        String responsebody;
        String ip = proxyData.getIp();
        int port = proxyData.getPort();
        String username = proxyData.getUserName();
        String password = proxyData.getPassword();
//            WhitepagesGui.outputText.append("\nProxy Details : " + proxyData.getIp() + " " + proxyData.getPort());
        CredentialsProvider credsprovider = new BasicCredentialsProvider();
        credsprovider.setCredentials(
                new AuthScope(ip, port),
                new UsernamePasswordCredentials(username, password));
        HttpHost proxy = new HttpHost(ip, port);   //95.85.29.99 198.199.126.6
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
                        HttpEntity entity = response.getEntity();
                        return entity != null ? EntityUtils.toString(entity) : null;
                    } else {
                        return null;
                    }
                }
            };
            responsebody = httpclient.execute(httpget, responseHandler);

        } catch (Exception e) {
            e.printStackTrace();
            WhitepagesGui.errorLogText.append("\nProxy Failure : " + ip + " " + port);
            return null;
        }
        return responsebody;
    }
}
