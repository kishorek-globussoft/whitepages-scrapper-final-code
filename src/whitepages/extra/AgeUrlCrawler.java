package whitepages.extra;

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package whitepages.crawlers;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import whitepages.dao.PeopleDao;
//import whitepages.entity.People;
//import whitepages.utility.FetchPageSource;
//
///**
// *
// * @author GLB-029
// */
//public class AgeUrlCrawler implements Runnable {
//
//    /**
//     * @param args the command line arguments
//     */
//    String url;
//
//    public AgeUrlCrawler(String url) {
//        this.url = url;
//    }
//
////    public static void main(String[] args) {
//    @Override
//    public void run() {
//        System.out.println("Main URL : " + url);
//        String response = null;
//        try {
//            response = FetchPageSource.fetchPageSource(url);
//            if (response == null) {
//            } else {
//                Document document = Jsoup.parse(response);
//                int pageCount = 1;
//                try {
//                    pageCount = Integer.parseInt(document.select("div[class=serp-pagination] ol[class=pagination unstyled no-margin] li").last().text());
//                    System.out.println("PAge count : " + pageCount);
//                } catch (NullPointerException e) {
//                }
//                if (pageCount > 1) {
//                    for (int i = 1; i <= pageCount; i++) {
//                        String pageUrl = url + "/" + i;
//                        System.out.println("Paging url : " + pageUrl);
//                        getUrls(pageUrl);
//                    }
//                } else {
//                    getUrls(url);
//                }
//            }
//        } catch (IOException | NumberFormatException e) {
//        }
//    }
//
//    private static void getUrls(String localUrl) {
//        try {
//            String response = FetchPageSource.fetchPageSource(localUrl);
//            Document document = Jsoup.parse(response);
//            Elements elements = document.select("div[class=serp-content] ul[class=serp-list unstyled] li[itemtype=http://schema.org/Person]");
//            List<People> peopleList = new ArrayList<>();
//            String firstName = null;
//            String middleName = null;
//            String lastName = null;
//            String profileUrl = null;
//            String age = null;
//            for (Element element : elements) {
//                People people = new People();
//                firstName = element.select("div[class=primary-content] p[class=name] span[itemprop=name] span[itemprop=givenName]").text();
//                middleName = element.select("div[class=primary-content] p[class=name] span[itemprop=name] span[itemprop=additionalName]").text();
//                lastName = element.select("div[class=primary-content] p[class=name] span[itemprop=name] span[itemprop=familyName]").text();
//                profileUrl = "http://www.whitepages.com" + element.select("a").attr("href");
//                age = element.select("div[class=secondary-content] span[itemprop=ageRange]").text();
//                System.out.println("Name : " + firstName + " " + middleName + " " + lastName);
//                System.out.println("Age  : " + age);
//                System.out.println("Profile Url : " + profileUrl);
//                System.out.println("--------------------------------------------------------------------------");
//                people.setFirstname(firstName);
//                people.setLastname(lastName);
//                people.setMiddlename(middleName);
//                people.setAge(age);
//                people.setUrl(profileUrl);
//                peopleList.add(people);
//            }
//            PeopleDao dao = new PeopleDao();
//            dao.savePeopleList(peopleList);
//        } catch (IOException ex) {
//        }
//    }
//}
