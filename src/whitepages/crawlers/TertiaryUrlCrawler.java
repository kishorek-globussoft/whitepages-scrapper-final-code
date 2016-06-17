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
import whitepages.entity.SecondaryUrl;
import whitepages.entity.TertiaryUrl;
import whitepages.gui.WhitepagesGui;
//import whitepages.extra.AllDataGui;
import whitepages.utility.FetchPageSource;

/**
 *
 * @author GLB-029
 */
public class TertiaryUrlCrawler extends Thread {

    String url;
    int proxyType;
    SecondaryUrl secondaryUrl;
    SessionFactory factory;
//    public TertiaryUrlCrawler(ThreadGroup threadGroup, String threadName, int proxyType) {
//        super(threadGroup, threadName);

    public TertiaryUrlCrawler(int proxyType, SecondaryUrl secondaryUrl, SessionFactory factory) {
        this.factory = factory;
        this.proxyType = proxyType;
        this.secondaryUrl = secondaryUrl;
    }

    @Override
    public void run() {
        try {
            WhitepagesGui.outputText.append("\n Tertiary process started for url: " + secondaryUrl.getUrl());
            WhitepagesGui.outputText.append("\n-----------------------------------------");
            String response = FetchPageSource.fetchPageSource(secondaryUrl.getUrl(), proxyType,factory);
            if (response == null) {
                return;
            }
            Document document = Jsoup.parse(response);
            Elements elements = document.select("ul[class=unstyled site-map pull-left] li");
            String name = "";
            List<TertiaryUrl> list = new ArrayList<>();
            for (Element element : elements) {
                if (!StartAllDataCrawler.running) {
                    return;
                }
                TertiaryUrl tertiaryUrl = new TertiaryUrl();
                String pageUrl = "http://www.whitepages.com" + element.select("a").attr("href");
                try {
                    name = element.select("a").text();
                    String[] nameArr = name.split(" ");
                    if (nameArr.length == 3) {
                        name = nameArr[2].trim();
                    } else if (nameArr.length == 2) {
                        name = nameArr[1].trim();
                    }
                } catch (Exception e) {
                }
                System.out.println("Tertiary Url :" + pageUrl + "  " + name);
                WhitepagesGui.outputText.append("\nTertiary URL : " + pageUrl);
                tertiaryUrl.setUrl(pageUrl);
                tertiaryUrl.setName(name);
                list.add(tertiaryUrl);
            }
            WhitepagesGui.errorLogText.append("\nSaving all Tertiary Data to database..");
            PeopleDao dao = new PeopleDao(factory);
            dao.saveTertiaryUrls(list);

            secondaryUrl.setScraped(true);
            dao.updateSecondaryUrls(secondaryUrl);

        } catch (Exception ex) {
            WhitepagesGui.errorLogText.append("\nUnexpected error in getting tertiary urls.");
        }
    }

//    @Override
//    public void run() {
//        try {
//            PeopleDao dao = new PeopleDao();
//            System.out.println("Secondary Url crawler started..");
//            System.out.println("-----------------------------------------");
//            WhitepagesGui.outputText.append("\nTertiary Url crawler started..");
//            WhitepagesGui.outputText.append("\n-----------------------------------------");
//            List<SecondaryUrl> secondaryUrls = dao.getSecondaryUrls();
//            System.out.println(secondaryUrls.size());
//            for (SecondaryUrl localsecondaryUrl : secondaryUrls) {
//                if (Thread.interrupted() || !StartAllDataCrawler.running) {
//                    WhitepagesGui.errorLogText.append("\nStopping the process..");
//                    return;
////                    throw new RuntimeException("Thread interrupted...");
//                }
//                getTertiaryUrls(localsecondaryUrl);
//            }
//        } catch (Exception e) {
//            WhitepagesGui.errorLogText.append("\nUnexpected error in getting tertiary urls.");
//        }
//    }
//    private void getTertiaryUrls(SecondaryUrl secondaryUrl) {
//        try {
//            System.out.println("Secondary URL : " + secondaryUrl.getUrl());
//            System.out.println("-----------------------------------------");
//            String response = FetchPageSource.fetchPageSource(secondaryUrl.getUrl(), proxyType);
//            if (response == null) {
//                return;
//            }
//            Document document = Jsoup.parse(response);
//            Elements elements = document.select("ul[class=unstyled site-map pull-left] li");
//            String name = "";
//            List<TertiaryUrl> list = new ArrayList<>();
//            for (Element element : elements) {
//                TertiaryUrl tertiaryUrl = new TertiaryUrl();
//                String pageUrl = "http://www.whitepages.com" + element.select("a").attr("href");
//                try {
//                    name = element.select("a").text();
//                    String[] nameArr = name.split(" ");
//                    if (nameArr.length == 3) {
//                        name = nameArr[2].trim();
//                    } else if (nameArr.length == 2) {
//                        name = nameArr[1].trim();
//                    }
//                } catch (Exception e) {
//                }
//                System.out.println("Tertiary Url :" + pageUrl + "  " + name);
//                WhitepagesGui.outputText.append("\nTertiary URL : " + pageUrl);
//                tertiaryUrl.setUrl(pageUrl);
//                tertiaryUrl.setName(name);
//                list.add(tertiaryUrl);
//            }
//            WhitepagesGui.errorLogText.append("\nSaving tertiary Url to database");
//            PeopleDao dao = new PeopleDao();
//            dao.saveTertiaryUrls(list);
//
//            secondaryUrl.setScraped(true);
//            dao.updateSecondaryUrls(secondaryUrl);
//
//        } catch (Exception ex) {
//            WhitepagesGui.errorLogText.append("\nUnexpected error in getting tertiary urls.");
//        }
//    }
}
