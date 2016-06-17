/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package whitepages.crawlers;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.SessionFactory;

/**
 *
 * @author GLB-029
 */
public class StartAllThreads extends Thread {

    static List<Thread> threadList = new ArrayList<>();
    String url;
    int proxyType;
//    ThreadGroup threadGroup = new ThreadGroup("Crawlers Group");
    SessionFactory factory;

    public StartAllThreads(String url, int proxyType, SessionFactory factory) {
//        super(group, threadName);
        this.factory = factory;
        this.url = url;
        this.proxyType = proxyType;
    }

    @Override
    public void run() {
        if (StartAllDataCrawler.running) {
            try {
                PrimaryUrlCrawler primaryUrlCrawler = new PrimaryUrlCrawler(url, proxyType,factory);
                primaryUrlCrawler.start();
                primaryUrlCrawler.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(StartAllThreads.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            return;
        }

//        if (StartAllDataCrawler.running) {
//            try {
////                SecondaryUrlCrawler secondaryUrlCrawler = new SecondaryUrlCrawler(proxyType);
////                secondaryUrlCrawler.start();
////                secondaryUrlCrawler.join();
//            } catch (InterruptedException ex) {
//                Logger.getLogger(StartAllThreads.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        } else {
//            return;
//        }
//        if (StartAllDataCrawler.running) {
//            TertiaryUrlCrawler tertiaryUrlCrawler = new TertiaryUrlCrawler(proxyType);
//            tertiaryUrlCrawler.start();
//        } else {
//            return;
//        }
        try {
            Thread.sleep(120000);
        } catch (InterruptedException ex) {
            this.stopThread();
            return;
        }

//        if (StartAllDataCrawler.running) {
//            try {
//                ProfileUrlCrawler profileUrlCrawler = new ProfileUrlCrawler(proxyType);
//                profileUrlCrawler.start();
//                profileUrlCrawler.join();
//            } catch (InterruptedException ex) {
//                Logger.getLogger(StartAllThreads.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
    }

    public void stopThread() {
        StartAllDataCrawler.running = false;
        this.interrupt();
//        threadGroup.interrupt();
    }
}
