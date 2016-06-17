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
import whitepages.dao.PeopleDao;
import whitepages.entity.PrimaryUrl;
import whitepages.entity.SecondaryUrl;
import whitepages.entity.TertiaryUrl;
import whitepages.gui.WhitepagesGui;

/**
 *
 * @author GLB-029
 */
public class StartAllDataCrawler extends Thread {

    int proxyType;
    List<StartAllThreads> list = new ArrayList<>();
    StartAllThreads startAllThreads;
    public static boolean running = false;
    SessionFactory factory;
    PeopleDao dao = new PeopleDao(factory);

    public StartAllDataCrawler(int proxyType, SessionFactory factory) {
        this.factory = factory;
        running = true;
        this.proxyType = proxyType;
    }

//    public static void main(String[] args) {
//        StartAllDataCrawler ref = new StartAllDataCrawler(0);
//        ref.start();
//        ref.stopAllThreads();
////    }
//    @Override
//    public void run() {
//        String[] alphabets = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
//        ExecutorService executor = Executors.newFixedThreadPool(1);
//        for (String letter : alphabets) {
//            if (!running) {
//                return;
//            }
//            String url = "http://www.whitepages.com/ind/" + letter;
//            System.out.println(url);
//            startAllThreads = new StartAllThreads(url, proxyType);
//            executor.execute(startAllThreads);
//            try {
//                Thread.sleep(180000);
//            } catch (InterruptedException ex) {
//            }
//        }
//    }
    @Override
    public void run() {
        String[] alphabets = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        ExecutorService executor = Executors.newFixedThreadPool(10);
        try {
            for (String letter : alphabets) {
                if (!running) {
                    WhitepagesGui.errorLogText.append("\nMain process stopped");
                    return;
                }
                String url = "http://www.whitepages.com/ind/" + letter;
                System.out.println(url);
                PrimaryUrlCrawler primaryUrlCrawler = new PrimaryUrlCrawler(url, proxyType, factory);
                executor.execute(primaryUrlCrawler);
            }
        } catch (Exception e) {
            executor.shutdown();
        }

        try {
            Thread.sleep(60000);
        } catch (InterruptedException ex) {
        }

        List<PrimaryUrl> primaryUrls = dao.getPrimaryUrls();
        ExecutorService secondaryExecutor = Executors.newFixedThreadPool(5);
        try {
            for (PrimaryUrl primaryUrl : primaryUrls) {
                if (running) {
                    SecondaryUrlCrawler secondaryUrlCrawler = new SecondaryUrlCrawler(proxyType, primaryUrl, factory);
                    secondaryExecutor.execute(secondaryUrlCrawler);
                } else {
                    WhitepagesGui.errorLogText.append("\nSecondary process stopped");
                    return;
                }
            }
        } catch (Exception e) {
            secondaryExecutor.shutdown();
        }

        try {
            Thread.sleep(120000);
        } catch (InterruptedException ex) {
            return;
        }

        List<SecondaryUrl> secondaryUrls = dao.getSecondaryUrls();
        ExecutorService tertiaryExecutor = Executors.newFixedThreadPool(5);
        try {
            for (SecondaryUrl secondaryUrl : secondaryUrls) {
                if (running) {
                    TertiaryUrlCrawler tertiaryUrlCrawler = new TertiaryUrlCrawler(proxyType, secondaryUrl, factory);
                    tertiaryExecutor.execute(tertiaryUrlCrawler);
                } else {
                    WhitepagesGui.errorLogText.append("\nTertiary process stopped");
                    return;
                }
            }
        } catch (Exception e) {
            tertiaryExecutor.shutdown();
        }

        try {
            Thread.sleep(120000);
        } catch (InterruptedException ex) {
            return;
        }

        List<TertiaryUrl> tertiaryUrls = dao.getTertiaryUrls();
        ExecutorService profileExecutor = Executors.newFixedThreadPool(5);
        try {
            for (TertiaryUrl tertiaryUrl : tertiaryUrls) {
                if (running) {
                    ProfileUrlCrawler profileUrlCrawler = new ProfileUrlCrawler(proxyType, tertiaryUrl, factory);
                    profileExecutor.execute(profileUrlCrawler);
                } else {
                    WhitepagesGui.errorLogText.append("\nProfile process stopped");
                    return;
                }
            }
        } catch (Exception e) {
            profileExecutor.shutdown();
        }
    }

    public void stopAllThreads() {
        running = false;
    }

}
