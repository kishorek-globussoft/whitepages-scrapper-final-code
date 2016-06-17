/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package whitepages.crawlers;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import whitepages.csvgenerator.CsvGenerator;
import whitepages.dao.PeopleDao;
import whitepages.entity.People;
import whitepages.entity.ProxyData;
import whitepages.utility.FetchPageSourceWithProxy;

/**
 *
 * @author GLB-029
 */
public class BusinessDataCrawler {

    String url;
    ProxyData proxyData;
    String searchName;
    int proxyType;

//    public ProfileDataCrawler(String url, int proxyType) {
//        this.url = url;
//        this.proxyType = proxyType;
//    }
//    public BusinessDataCrawler(String url, String searchName, int proxyType) {
//        this.searchName = searchName;
//        this.url = url;
//        this.proxyType = proxyType;
//    }
    public static void main(String[] args) {
        run("http://www.whitepages.com/business/holiday-inn-express-nyc-madison-square-garden-new-york-ny");
    }

    static int count = 0;

    public static void run(String url) {
        try {
//            PeopleDao dao = new PeopleDao();
//            People people = new People();
            String streetAddress = null;
            String stateAddress = null;
            String state = null;
            String city = null;
            String zipcode = null;
            String phone = null;
            String age = null;
            String name = null;
            String fax = null;
            String response = FetchPageSourceWithProxy.fetchPageSourceWithProxy(url);//.fetchPageSource(url, proxyType);
            Document document = Jsoup.parse(response);
            System.out.println(url);

            name = document.select("div[class=name-plate-wrapper] h1[class*=title] span[class=name block]").text();
            String category = document.select("div[class=name-plate-wrapper] h1[class*=title] span[class=subtitle-nonfloat block]").text();
            phone = document.select("div [class*=phones] ul").text();
            if(phone.contains(" ")){
                fax = phone.substring(phone.indexOf(" ")+1, phone.length());
                phone = phone.substring(0,phone.indexOf(" "));
            }
            streetAddress = document.select("address[class=address adr] span[class*=street-address]").text();
            stateAddress = document.select("address[class=address adr] span[class*=address-location block").text();

            String[] stateZipcode = stateAddress.split(",");
            city = stateZipcode[0].trim();
            state = stateZipcode[1].trim().substring(0, stateZipcode[1].trim().indexOf(" "));
            zipcode = stateZipcode[1].trim().substring(stateZipcode[1].trim().indexOf(" "),stateZipcode[1].trim().length()).trim();

            System.out.println("Name     : " + name);
            System.out.println("Category : " + category);
            System.out.println("Phone    : " + phone);
            System.out.println("Fax      : " + fax);
            System.out.println("Street   : " + streetAddress);
            System.out.println("City     : " + city);
            System.out.println("State    : " + state);
            System.out.println("ZipCode  : " + zipcode);
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
