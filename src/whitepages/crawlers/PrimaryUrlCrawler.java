/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package whitepages.crawlers;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.SessionFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import whitepages.dao.PeopleDao;
import whitepages.entity.ProxyData;
import whitepages.gui.WhitepagesGui;
import whitepages.utility.FetchPageSource;

/**
 *
 * @author GLB-029
 */
public class PrimaryUrlCrawler extends Thread {
    /*
     * To change this license header, choose License Headers in Project Properties.
     * To change this template file, choose Tools | Templates
     * and open the template in the editor.
     */

    String url;
    ProxyData proxyData;
    int proxyType;
    SessionFactory factory;

//    public PrimaryUrlCrawler(ThreadGroup threadGroup, String threadName, String url, int proxyType) {
//        super(threadGroup, threadName);
    public PrimaryUrlCrawler(String url, int proxyType, SessionFactory factory) {
        this.factory = factory;
        this.url = url;
        this.proxyType = proxyType;
    }

    @Override
    public void run() {
        try {
            WhitepagesGui.outputText.append("\nPrimary Process started for url: " + url);
            WhitepagesGui.outputText.append("\n-----------------------------------------");
            String response = FetchPageSource.fetchPageSource(url, proxyType,factory);
            if (response == null) {
                WhitepagesGui.errorLogText.append("\n No response for url : " + url);
                System.out.println("null primary response");
                return;
            }
            Document document = Jsoup.parse(response);
            Elements elements = document.select("ul[class=unstyled site-map pull-left] li");
            List<String> urls = new ArrayList<>();
            for (Element element : elements) {
                if (Thread.interrupted() || !StartAllDataCrawler.running) {
                    return;
                }
                String pageUrl = "http://www.whitepages.com" + element.select("a").attr("href");
                WhitepagesGui.outputText.append("\nPrimary Url: " + pageUrl);
                System.out.println("Primary Url: " + pageUrl);
                urls.add(pageUrl);
            }
            WhitepagesGui.errorLogText.append("\nSaving all Primary Data to database..");
            PeopleDao dao = new PeopleDao(factory);
            dao.savePrimaryUrls(urls);
        } catch (Exception ex) {
            WhitepagesGui.errorLogText.append("\nUnexpected error in getting primary urls.");
//            throw new RuntimeException("Thread interrupted..." + ex);
        }
    }
}
