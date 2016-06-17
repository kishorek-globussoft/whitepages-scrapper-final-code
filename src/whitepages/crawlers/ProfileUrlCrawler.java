/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package whitepages.crawlers;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.hibernate.SessionFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import whitepages.dao.PeopleDao;
import whitepages.entity.TertiaryUrl;
import whitepages.gui.WhitepagesGui;
import whitepages.utility.FetchPageSource;

/**
 *
 * @author GLB-029
 */
public class ProfileUrlCrawler extends Thread {

    /**
     * @param args the command line arguments
     */
    int proxyType = 0;
    TertiaryUrl tertiaryUrl;
    SessionFactory factory;

    public ProfileUrlCrawler(int proxyType, TertiaryUrl tertiaryUrl, SessionFactory factory) {
        this.factory = factory;
        this.proxyType = proxyType;
        this.tertiaryUrl = tertiaryUrl;
    }

    @Override
    public void run() {
        try {
            PeopleDao dao = new PeopleDao(factory);
            if (!StartAllDataCrawler.running) {
                WhitepagesGui.errorLogText.append("\nProfile process stopped..");
                return;
            }
            if (tertiaryUrl.getUrl().contains("whitepages.com/people/")) {
                List<String> urlList = new ArrayList<>();
                urlList.add(tertiaryUrl.getUrl());
                tertiaryUrl.setScraped(true);
                dao.updateTertiaryUrls(tertiaryUrl);
                getData(urlList);
                return;
            }
            String response = FetchPageSource.fetchPageSource(tertiaryUrl.getUrl(), proxyType,factory);
            if (response == null) {
                WhitepagesGui.errorLogText.append("\nNo data available from website..");
            } else {
                Document document = Jsoup.parse(response);
                int pageCount = 1;
                try {
                    Element element = document.select("div[class=serp-pagination] ol[class=pagination unstyled no-margin] li").last();
                    if (element != null) {
                        pageCount = Integer.parseInt(element.text());
                    }
                } catch (Exception e) {
                }
                if (pageCount > 1) {
                    tertiaryUrl.setScraped(true);
                    dao.updateTertiaryUrls(tertiaryUrl);
                    for (int i = 1; i <= pageCount; i++) {
                        if (!StartAllDataCrawler.running) {
                            System.out.println("Profile url crawler is interrupted.");
                            return;
                        }
                        String pageUrl = tertiaryUrl.getUrl() + "/" + i;
                        WhitepagesGui.outputText.append("\nPage url : " + pageUrl);
                        getUrls(pageUrl);
                    }
                } else {
                    tertiaryUrl.setScraped(true);
                    dao.updateTertiaryUrls(tertiaryUrl);
                    getUrls(tertiaryUrl.getUrl());
                }
            }
        } catch (Exception ex) {
            WhitepagesGui.errorLogText.append("\nUnexpected error in getting profile urls.");
        }
    }

    private void getUrls(String localUrl) {
        try {
            String response = FetchPageSource.fetchPageSource(localUrl, proxyType,factory);
            if (response == null) {
                return;
            }
            Document document = Jsoup.parse(response);
            Elements elements = document.select("div[class=serp-content] ul[class=serp-list unstyled] li[itemtype=http://schema.org/Person]");
            String profileUrl = null;
            List<String> urlList = new ArrayList<>();
            for (Element element : elements) {
                profileUrl = "http://www.whitepages.com" + element.select("a").attr("href");
                System.out.println("Profile URL : " + profileUrl);
                urlList.add(profileUrl);
            }
            getData(urlList);
        } catch (Exception ex) {
            WhitepagesGui.errorLogText.append("\nUnexpected error in getting profile urls.");
        }
    }

    private void getData(List<String> urlList) {
        ExecutorService executor;
        executor = Executors.newFixedThreadPool(2);
        for (String url : urlList) {
            ProfileDataCrawler ref = new ProfileDataCrawler(url, "data", proxyType,factory);
            if (!StartAllDataCrawler.running) {
                return;
            }
            executor.execute(ref);
        }
        executor.shutdown();
    }

}

//    private void getProfileUrls(TertiaryUrl tertiaryUrl) {
//        try {
//            PeopleDao dao = new PeopleDao();
//            if (!StartAllDataCrawler.running) {
//                WhitepagesGui.errorLogText.append("\nStopping the process..");
//                return;
//            }
//            if (tertiaryUrl.getUrl().contains("whitepages.com/people/")) {
//                List<String> urlList = new ArrayList<>();
//                urlList.add(tertiaryUrl.getUrl());
//                tertiaryUrl.setScraped(true);
//                dao.updateTertiaryUrls(tertiaryUrl);
//                getData(urlList);
//            }
//            String response = FetchPageSource.fetchPageSource(tertiaryUrl.getUrl(), proxyType);
//            if (response == null) {
//                WhitepagesGui.errorLogText.append("\nNo data available from website..");
//            } else {
//                Document document = Jsoup.parse(response);
//                int pageCount = 1;
//                try {
//                    Element element = document.select("div[class=serp-pagination] ol[class=pagination unstyled no-margin] li").last();
//                    if (element != null) {
//                        pageCount = Integer.parseInt(element.text());
//                    }
//                } catch (Exception e) {
//                }
//                if (pageCount > 1) {
//                    tertiaryUrl.setScraped(true);
//                    dao.updateTertiaryUrls(tertiaryUrl);
//                    for (int i = 1; i <= pageCount; i++) {
//                        if (!StartAllDataCrawler.running) {
//                            System.out.println("Profile url crawler is interrupted.");
//                            return;
//                        }
//                        String pageUrl = tertiaryUrl.getUrl() + "/" + i;
//                        WhitepagesGui.outputText.append("\nPage url : " + pageUrl);
//                        getUrls(pageUrl);
//                    }
//                } else {
//                    tertiaryUrl.setScraped(true);
//                    dao.updateTertiaryUrls(tertiaryUrl);
//                    getUrls(tertiaryUrl.getUrl());
//                }
//            }
//        } catch (Exception ex) {
//            WhitepagesGui.errorLogText.append("\nUnexpected error in getting profile urls.");
//        }
//    }
//@Override
//    public void run() {
//        PeopleDao dao = new PeopleDao();
//        List<TertiaryUrl> list = dao.getTertiaryUrls();
//        for (TertiaryUrl tertiaryUrl : list) {
//            if (!StartAllDataCrawler.running) {
//                WhitepagesGui.errorLogText.append("\nStopping the process..");
//                return;
//            }
//            getProfileUrls(tertiaryUrl);
//        }
//    }
