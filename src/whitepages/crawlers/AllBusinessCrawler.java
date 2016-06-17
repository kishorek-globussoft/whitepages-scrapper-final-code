/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package whitepages.crawlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import whitepages.dao.BusinessDao;
import whitepages.entity.BusinessUrl;
import whitepages.utility.FetchPageSourceWithProxy;

/**
 *
 * @author GLB-029
 */
public class AllBusinessCrawler {

    public static void main(String[] args) {
        try {
            String url = "http://www.whitepages.com/business";
            String response = FetchPageSourceWithProxy.fetchPageSourceWithProxy(url);
            Document document = Jsoup.parse(response);
            Elements elements = document.select("div[class=home-secondary-content no-overflow clear-all] ul[class=category-list unstyled] li");
//            System.out.println(elements);
//            for (Element element : elements) {
//                String category = element.text();
//                System.out.println("Category : " + category);
//            }
//            System.out.println("\n\n\n");
//            System.out.println("===========================================");

            Elements elements1 = document.select("div[class=home-secondary-content no-overflow clear-all] ul[class=unstyled link-list] li");
//            System.out.println(elements);
            for (Element element : elements1) {
                String cityUrl = "http://www.whitepages.com" + element.select("a").attr("href");
                System.out.println("City : " + cityUrl);
                AllBusinessCrawler ref = new AllBusinessCrawler();
                ref.getCategoryUrls(cityUrl);
            }
        } catch (IOException ex) {
            Logger.getLogger(AllBusinessCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    BusinessDao dao = new BusinessDao(1);
    private void getCategoryUrls(String localUrl) {
        try {
            String response = FetchPageSourceWithProxy.fetchPageSourceWithProxy(localUrl);
            Document document = Jsoup.parse(response);
            Elements elements = document.select("div[id=main] ul[class=category-list unstyled] li");
            List<BusinessUrl> businessUrlList = new ArrayList<>();
            for (Element element : elements) {
                BusinessUrl businessUrl = new BusinessUrl();
                String url = "http://www.whitepages.com" + element.select("a").attr("href");
                businessUrl.setUrl(url);
                businessUrl.setScraped(false);
                System.out.println("Category : " + url);
                businessUrlList.add(businessUrl);
            }
            dao.saveBusinessUrlList(businessUrlList);
            
            System.out.println("===========================================");
        } catch (IOException ex) {
            Logger.getLogger(AllBusinessCrawler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
