/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package whitepages.crawlers;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import whitepages.utility.FetchPageSourceWithProxy;

/**
 *
 * @author GLB-029
 */
public class BusinessCrawler {

    public static void main(String[] args) {
        try {
            String url = "http://www.whitepages.com/business/NY/New-York/restaurants";
            String response = FetchPageSourceWithProxy.fetchPageSourceWithProxy(url);
            Document document = Jsoup.parse(response);
            Elements elements = document.select("ul[id=serp-list] li[id*=-neighbors-li]");
            for (Element element : elements) {
                String businessUrl = "http://www.whitepages.com" + element.select("div[class*=serp-list-item organic no-overflow] a[class*=link-wrapper]").attr("href");
                System.out.println("businessUrl : " + businessUrl);
                BusinessDataCrawler ref = new BusinessDataCrawler();
                ref.run(businessUrl);
            }
            System.out.println("---------------------------------------------------");
            int pageCount = 0;
            try {
                Element element = document.select("div[class=serp-pagination] ol[class=pagination unstyled no-margin] li").last();
                if (element != null) {
                    pageCount = Integer.parseInt(element.text());
                }
            } catch (Exception e) {
            }
            if (pageCount > 1) {
                for (int i = 2; i <= pageCount; i++) {
                    String pageUrl = url + "?page=" + i;
                    System.out.println("Paging url : " + pageUrl);

                    getNextPageData(pageUrl);
                }
            } else {
                getNextPageData(url);
            }
        } catch (IOException ex) {
            Logger.getLogger(BusinessCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void getNextPageData(String url) {
        try {
            String response = FetchPageSourceWithProxy.fetchPageSourceWithProxy(url);
            Document document = Jsoup.parse(response);
            Elements elements = document.select("ul[id=serp-list] li[id*=-neighbors-li]");
            for (Element element : elements) {
                String businessUrl = "http://www.whitepages.com" + element.select("div[class*=serp-list-item organic no-overflow] a[class*=link-wrapper]").attr("href");
                System.out.println("businessUrl : " + businessUrl);
            }
            System.out.println("---------------------------------------------------");
        } catch (IOException ex) {
            Logger.getLogger(BusinessCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
