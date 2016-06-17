/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package whitepages.crawlers;

import java.io.IOException;
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
public class SearchByFile extends Thread {

    /**
     * @param args the command line arguments
     */
    List<String> nameList;
    List<String> cityList;
    String name;
    String city;
    int proxyType;
    public static boolean running = false;
    SessionFactory factory;

    public SearchByFile(String name, String city, int proxyType, SessionFactory factory) {
        this.factory = factory;
        running = true;
        this.name = name;
        this.city = city;
        this.proxyType = proxyType;
    }

    @Override
    public void run() {
        try {
            if (!running) {
                return;
            }
            System.out.println("Name : " + name + " " + "City : " + city);
            String url = "http://www.whitepages.com/name/" + name + "/" + city;
            System.out.println("Url : " + url);
            outputText.append("\n--------------------------------------------------------------------------------");
            outputText.append("\nCrawling started Using data From file... ");
            outputText.append("\n--------------------------------------------------------------------------------");
            WhitepagesGui.outputText.append("\nSearch name  : " + name);
            WhitepagesGui.outputText.append("\nSearch city  : " + city);
            WhitepagesGui.outputText.append("\n" + url);
            String response = FetchPageSource.fetchPageSource(url, proxyType, factory);
            System.out.println("Response : " + response);
            if (response.isEmpty() || response == null) {
                WhitepagesGui.errorLogText.append("\nNo data received.. scraping failed for name :" + name + " city :" + city);
            } else {
                Document document = Jsoup.parse(response);
                int pageCount = 1;
                try {
                    Elements pageElements = document.select("div[class=serp-pagination] ol[class=pagination unstyled no-margin] li");
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
                        String pageUrl = url + "/" + i;
                        getUrls(pageUrl);
                    }
                } else {
                    getUrls(url);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            WhitepagesGui.errorLogText.append("\nSomething went wrong in getting data using file. Try Again");
        }
    }

    private void getUrls(String localUrl) {
        try {
            String response = FetchPageSource.fetchPageSource(localUrl, proxyType, factory);
            if (response.isEmpty() || response == null) {
                System.out.println("Get Urls : Empty response");
                return;
            }
            Document document = Jsoup.parse(response);
            Elements elements = document.select("div[class=serp-content] ul[class=serp-list unstyled] li[itemtype=http://schema.org/Person]");
            String profileUrl = null;
            List<String> urlList = new ArrayList<>();
            for (Element element : elements) {
                profileUrl = "http://www.whitepages.com" + element.select("a").attr("href");
                urlList.add(profileUrl);
            }
            getData(urlList);

        } catch (Exception ex) {
            WhitepagesGui.errorLogText.append("\n" + ex.getMessage());
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
            executor.execute(ref);
        }
    }

    public void stopSearchByFileThread() {
        WhitepagesGui.errorLogText.append("\nStopped scraping from file data");
        running = false;
    }
}
