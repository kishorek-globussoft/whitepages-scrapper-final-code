///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package whitepages.extra;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import whitepages.crawlers.ProfileDataCrawler;
//import whitepages.entity.ProxyData;
//import whitepages.gui.WhitepagesGui;
//import static whitepages.gui.WhitepagesGui.outputText;
//import whitepages.utility.FetchPageSource;
//
///**
// *
// * @author GLB-029
// */
//public class FinalDataCrawler extends Thread {
//
//    /**
//     * @param args the command line arguments
//     */
//    ProxyData proxyData = new ProxyData();
//    int proxyType;
//    String url;
//
//    public FinalDataCrawler(ProxyData proxyData, String url,int proxyType) {
//        this.proxyData = proxyData;
//        this.url = url;
//        this.proxyType = proxyType;
//    }
//
//    @Override
//    public void run() {
//        try {
//            WhitepagesGui.outputText.append("\nCrawling started... ");
//            outputText.append("\n--------------------------------------------------------------------------------");
//            WhitepagesGui.outputText.append("\nProxy Passed : " + proxyData.getIp());
//            WhitepagesGui.outputText.append("\nPort  Passed : " + proxyData.getPort());
//            WhitepagesGui.outputText.append("\nUrl   Passed : " + url);
//            String response = FetchPageSource.fetchPageSource(url,proxyType);
//            if (response == null) {
//            } else {
//                Document document = Jsoup.parse(response);
//                int pageCount = 1;
//                try {
//                    pageCount = Integer.parseInt(document.select("div[class=serp-pagination] ol[class=pagination unstyled no-margin] li").last().text());
//                } catch (NullPointerException e) {
//                }
//                if (pageCount > 1) {
//                    for (int i = 1; i <= pageCount; i++) {
//                        String pageUrl = url + "/" + i;
//                        getUrls(pageUrl);
//                    }
//                } else {
//                    getUrls(url);
//                }
//            }
//        } catch (IOException ex) {
//            Logger.getLogger(FinalDataCrawler.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    private void getUrls(String localUrl) {
//        try {
//            String response = FetchPageSource.fetchPageSource(localUrl,proxyType);
//            if (response == null) {
//                return;
//            }
//            Document document = Jsoup.parse(response);
//            Elements elements = document.select("div[class=serp-content] ul[class=serp-list unstyled] li[itemtype=http://schema.org/Person]");
//            String profileUrl = null;
//
//            String headingText = document.select("div[class=serp-options no-overflow] h1").text();
//            outputText.append("\n" + headingText);
//
//            List<String> urlList = new ArrayList<>();
//            for (Element element : elements) {
////                firstName = element.select("div[class=primary-content] p[class=name] span[itemprop=name] span[itemprop=givenName]").text();
////                middleName = element.select("div[class=primary-content] p[class=name] span[itemprop=name] span[itemprop=additionalName]").text();
////                lastName = element.select("div[class=primary-content] p[class=name] span[itemprop=name] span[itemprop=familyName]").text();
//                profileUrl = "http://www.whitepages.com" + element.select("a").attr("href");
////                age = element.select("div[class=secondary-content] span[itemprop=ageRange]").text();
////                System.out.println("Name : " + firstName + " " + middleName + " " + lastName);
////                System.out.println("Age  : " + age);
//                urlList.add(profileUrl);
//            }
//            getData(urlList);
//        } catch (IOException ex) {
//            Logger.getLogger(FinalDataCrawler.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//    private void getData(List<String> urlList) {
//        ExecutorService executor;
//        executor = Executors.newFixedThreadPool(3);
//        for (String url : urlList) {
////            ProfileDataCrawler ref = new ProfileDataCrawler(url,proxyType);
////            executor.execute(ref);
//        }
//    }
//}
