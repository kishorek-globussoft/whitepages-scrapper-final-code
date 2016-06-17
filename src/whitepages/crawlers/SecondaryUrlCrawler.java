/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package whitepages.crawlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.SessionFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import whitepages.dao.PeopleDao;
import whitepages.entity.PrimaryUrl;
import whitepages.entity.ProxyData;
import whitepages.gui.WhitepagesGui;
//import whitepages.extra.AllDataGui;
import whitepages.utility.FetchPageSource;

/**
 *
 * @author GLB-029
 */
public class SecondaryUrlCrawler extends Thread {

    int proxyType;
    PrimaryUrl primaryUrl = new PrimaryUrl();
    SessionFactory factory;

//    public SecondaryUrlCrawler(ThreadGroup threadGroup, String threadName, int proxyType) {
    public SecondaryUrlCrawler(int proxyType, PrimaryUrl primaryUrl,SessionFactory factory) {
//        super(threadGroup, threadName);
        this.proxyType = proxyType;
        this.factory = factory;
        this.primaryUrl = primaryUrl;
    }

    @Override
    public void run() {
        if (!StartAllDataCrawler.running) {
            return;
        }
        try {
            WhitepagesGui.outputText.append("\nSecondary Process started for url: " + primaryUrl.getUrl());
            WhitepagesGui.outputText.append("\n-----------------------------------------");
            String response = FetchPageSource.fetchPageSource(primaryUrl.getUrl(), proxyType,factory);
            Document document = Jsoup.parse(response);
            Elements elements = document.select("ul[class=unstyled site-map pull-left] li");
            List<String> urls = new ArrayList<>();
            for (Element element : elements) {
                String pageUrl = "http://www.whitepages.com" + element.select("a").attr("href");
                String name = element.select("a").text();
                WhitepagesGui.outputText.append("\nSecondary URL : " + pageUrl);
                System.out.println("Secondary URL : " + pageUrl + "  " + name);
                urls.add(pageUrl);
            }
            WhitepagesGui.errorLogText.append("\nSaving all Secondary Data to database..");
            PeopleDao dao = new PeopleDao(factory);
            dao.saveSecondaryUrls(urls);

            primaryUrl.setScraped(true);
            dao.updatePrimaryUrls(primaryUrl);
        } catch (Exception ex) {
            WhitepagesGui.errorLogText.append("\nUnexpected error in getting secondary urls.");
        }

    }

//     @Override
//    public void run() {
    //        try {
//            PeopleDao dao = new PeopleDao();
//            System.out.println("Secondary Url crawler started..");
//            System.out.println("-----------------------------------------");
//            WhitepagesGui.outputText.append("\nSecondary Url crawler started..");
//            WhitepagesGui.outputText.append("\n-----------------------------------------");
//            List<PrimaryUrl> primaryUrls = dao.getPrimaryUrls();
//            for (PrimaryUrl localPrimaryUrl : primaryUrls) {
//                if (Thread.interrupted() || !StartAllDataCrawler.running) {
//                    WhitepagesGui.errorLogText.append("\nStopping the process..");
//                    return;
//                }
//                getSecondaryUrls(localPrimaryUrl);
//            }
//        } catch (Exception e) {
//            WhitepagesGui.errorLogText.append("\nUnexpected error in getting secondary urls.");
//        }
}

//    private void getSecondaryUrls(PrimaryUrl primaryUrl) {
//        try {
//            String response = FetchPageSource.fetchPageSource(primaryUrl.getUrl(), proxyType);
//            Document document = Jsoup.parse(response);
//            Elements elements = document.select("ul[class=unstyled site-map pull-left] li");
//            List<String> urls = new ArrayList<>();
//            for (Element element : elements) {
//                String pageUrl = "http://www.whitepages.com" + element.select("a").attr("href");
//                String name = element.select("a").text();
//                WhitepagesGui.outputText.append("\nSecondary URL : " + pageUrl);
//                System.out.println("Secondary URL : " + pageUrl + "  " + name);
//                urls.add(pageUrl);
//            }
//            WhitepagesGui.errorLogText.append("\nSaving secondary Url to database");
//            PeopleDao dao = new PeopleDao();
//            dao.saveSecondaryUrls(urls);
//
//            primaryUrl.setScraped(true);
//            dao.updatePrimaryUrls(primaryUrl);
//        } catch (Exception ex) {
//            WhitepagesGui.errorLogText.append("\nUnexpected error in getting secondary urls.");
//        }
//    }

