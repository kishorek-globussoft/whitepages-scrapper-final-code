/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package whitepages.crawlers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.hibernate.SessionFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import whitepages.gui.WhitepagesGui;
import static whitepages.gui.WhitepagesGui.outputText;
import whitepages.utility.FetchPageSource;

/**
 *
 * @author GLB-029
 */
public class SearchByName extends Thread {

    /**
     * @param args the command line arguments
     */
    String name;
    String city;
    int proxyType = 0;
    public static boolean running = false;
    SessionFactory factory;

    public SearchByName(String name, String city, int proxyType, SessionFactory factory) {
        this.factory = factory;
        running = true;
        this.name = name;
        this.proxyType = proxyType;
        this.city = city;
    }

    @Override
    public void run() {
        try {
            String url = "http://www.whitepages.com/name/" + name + "/" + city;
            WhitepagesGui.outputText.append("\nScraping started... ");
            outputText.append("\n--------------------------------------------------------------------------------");
            WhitepagesGui.outputText.append("\nSearch name  : " + name);
            WhitepagesGui.outputText.append("\nSearch city  : " + city);
            WhitepagesGui.outputText.append("\nUrl          : " + url);
            String response = FetchPageSource.fetchPageSource(url, proxyType, factory);
            if (response.isEmpty() || response == null) {
                WhitepagesGui.errorLogText.append("\nReceived Empty data from source. Try again later");
            } else {
                Document document = Jsoup.parse(response);
                int pageCount = 1;
                try {
                    Elements pageElements = document.select("div[class=serp-pagination] ol[class*=pagination unstyled] li");
                    System.out.println(pageElements);
                    if (pageElements.isEmpty() || pageElements == null) {
                        System.out.println("No page count");
                    } else {
                        pageCount = Integer.parseInt(document.select("div[class=serp-pagination] ol[class=pagination unstyled no-margin] li").last().text());
                        WhitepagesGui.outputText.append("\nPage Count  : " + pageCount);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    WhitepagesGui.errorLogText.append("\nRecieved Wrong information from Source. Try Again Later.");
                    return;
                }
                if (pageCount > 1) {
                    for (int i = 1; i <= pageCount; i++) {
                        if (!running) {
                            return;
                        }
                        String pageUrl;
                        if (url.endsWith("/")) {
                            pageUrl = url + i;
                        } else {
                            pageUrl = url + "/" + i;
                        }

                        WhitepagesGui.outputText.append("\nPage url : " + pageUrl);
                        getUrls(pageUrl);
                    }
                } else {
                    getUrls(url);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            WhitepagesGui.errorLogText.append("\nName Search Failed , Status : " + ex.getMessage());
        }
    }

    private void getUrls(String localUrl) {
        try {
            if (!running) {
                return;
            }
            String response = FetchPageSource.fetchPageSource(localUrl, proxyType, factory);
            if (response.isEmpty() || response == null) {
                System.out.println("geturls empty reponse");
                return;
            }
            Document document = Jsoup.parse(response);
            Elements elements = document.select("div[class=serp-content] ul[class*=serp-list] li[class*=serp-list-item]");
            String profileUrl = null;
            List<String> urlList = new ArrayList<>();
            for (Element element : elements) {
                profileUrl = "http://www.whitepages.com" + element.select("a").attr("href");
                System.out.println("Profile URL : " + profileUrl);
                urlList.add(profileUrl);
            }
            getData(urlList);
        } catch (Exception ex) {
            WhitepagesGui.errorLogText.append("\nName Search Failed , Status : " + ex.getMessage());
        }
    }

    private void getData(List<String> urlList) {
        ExecutorService executor;
        executor = Executors.newFixedThreadPool(3);

        for (String url : urlList) {
            if (!running) {
                return;
            }
            ProfileDataCrawler ref = new ProfileDataCrawler(url, name, proxyType, factory);
//            WhitepagesGui.threadList.add(ref);
            executor.execute(ref);
        }
        executor.shutdown();
    }

    public void stopSearchByName() {
        running = false;
    }
}
