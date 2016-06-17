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
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import whitepages.entity.ProxyData;
//import whitepages.gui.WhitepagesGui;
//import whitepages.utility.FetchPageSource;
//
///**
// *
// * @author GLB-029
// */
//public class AllDataCrawler1 extends Thread {
//
//    String passedUrl;
//    ProxyData proxyData;
//    int proxyType;
//
//    public AllDataCrawler1(String passedUrl, int proxyType) {
//        this.passedUrl = passedUrl;
//        this.proxyType = proxyType;
//    }
//
//    @Override
//    public void run() {
//        try {
//            String response = FetchPageSource.fetchPageSource(passedUrl,proxyType);
//            Document document = Jsoup.parse(response);
//            Elements elements = document.select("ul[class=unstyled site-map pull-left] li");
//            List<String> urls = new ArrayList<>();
//            for (Element element : elements) {
//                String pageUrl = "http://www.whitepages.com" + element.select("a").attr("href");
////                String name = element.select("a").text();
//                WhitepagesGui.outputText.append("\nPrimary Url : " + pageUrl);
//                urls.add(pageUrl);
//            }
//            getSecondaryUrls(urls);
//        } catch (IOException ex) {
//            WhitepagesGui.errorLogText.append("\n" + ex.getMessage());
//        }
//    }
//
//    public void getSecondaryUrls(List<String> urlList) {
//        for (String url : urlList) {
//            try {
//                String response = FetchPageSource.fetchPageSource(url,proxyType);
//                Document document = Jsoup.parse(response);
//                Elements elements = document.select("ul[class=unstyled site-map pull-left] li");
//                List<String> urls = new ArrayList<>();
//                WhitepagesGui.outputText.append("\nUrl : " + url);
//                WhitepagesGui.outputText.append("\n------------------------------------------");
//                for (Element element : elements) {
//                    String pageUrl = "http://www.whitepages.com" + element.select("a").attr("href");
////                    String name = element.select("a").text();
//                    WhitepagesGui.outputText.append("\nSecondary Url : " + pageUrl);
////                    System.out.println(pageUrl);
//                    urls.add(pageUrl);
//                }
//                getTertiaryUrl(urlList);
////                PeopleDao dao = new PeopleDao();
////                dao.saveSecondaryUrls(urls);
//
////                primaryUrl.setScraped(true);
////                dao.updatePrimaryUrls(primaryUrl);
//            } catch (IOException ex) {
//                WhitepagesGui.errorLogText.append("\n" + ex.getMessage());
//            }
//        }
//    }
//
//    public void getTertiaryUrl(List<String> urlList) {
//        for (String url : urlList) {
//            try {
//                String response = FetchPageSource.fetchPageSource(url,proxyType);
//                Document document = Jsoup.parse(response);
//                Elements elements = document.select("ul[class=unstyled site-map pull-left] li");
//                List<String> urls = new ArrayList<>();
//                WhitepagesGui.outputText.append("\nUrl : " + url);
//                WhitepagesGui.outputText.append("\n------------------------------------------");
//                for (Element element : elements) {
//                    String pageUrl = "http://www.whitepages.com" + element.select("a").attr("href");
//                    WhitepagesGui.outputText.append("\nTertiary Url : " + pageUrl);
////                    System.out.println(pageUrl + "  " + name);
//                    urls.add(pageUrl);
//                }
//                getProfileUrl(urls);
//            } catch (Exception ex) {
//                WhitepagesGui.errorLogText.append("\n" + ex.getMessage());
//            }
//        }
//    }
//
//    public void getProfileUrl(List<String> urls) {
//        try {
//            String response = FetchPageSource.fetchPageSource(passedUrl,proxyType);
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
//                        String pageUrl = passedUrl + "/" + i;
//                        getUrls(pageUrl);
//                    }
//                } else {
//                    getUrls(passedUrl);
//                }
//            }
//        } catch (IOException ex) {
//            WhitepagesGui.errorLogText.append("\n" + ex.getMessage());
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
////            String headingText = document.select("div[class=serp-options no-overflow] h1").text();
//            List<String> urlList = new ArrayList<>();
//            for (Element element : elements) {
////                firstName = element.select("div[class=primary-content] p[class=name] span[itemprop=name] span[itemprop=givenName]").text();
////                middleName = element.select("div[class=primary-content] p[class=name] span[itemprop=name] span[itemprop=additionalName]").text();
////                lastName = element.select("div[class=primary-content] p[class=name] span[itemprop=name] span[itemprop=familyName]").text();
//                profileUrl = "http://www.whitepages.com" + element.select("a").attr("href");
////                age = element.select("div[class=secondary-content] span[itemprop=ageRange]").text();
////                System.out.println("Name : " + firstName + " " + middleName + " " + lastName);
////                System.out.println("Age  : " + age);
//                WhitepagesGui.outputText.append("\nProfileUrl : " + profileUrl);
//                urlList.add(profileUrl);
//            }
//            getData(urlList);
//        } catch (IOException ex) {
//            WhitepagesGui.errorLogText.append("\n" + ex.getMessage());
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
