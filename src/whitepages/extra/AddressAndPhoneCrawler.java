package whitepages.extra;

///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package whitepages.crawlers;
//
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
//public class AddressAndPhoneCrawler implements Runnable {
//
//    People people;
//
//    AddressAndPhoneCrawler(People people) {
//        this.people = people;
//    }
//
//    @Override
//    public void run() {
//        System.out.println("Profile url : " + people.getUrl());
//        try {
//            PeopleDao dao = new PeopleDao();
//            String response = FetchPageSource.fetchPageSource(people.getUrl());
//            Document document = Jsoup.parse(response);
//            Elements elements = document.select("div[class=detail person vcard]");
//            String address = null;
//            String phone = null;
//            for (Element element : elements) {
//                phone = element.select("div[class=phones clear-all padded-top] ul[class=unstyled] a").text().trim();
//                address = element.select("address[class=address adr]").first().text();
//                System.out.println("Address : " + address);
//            }
//            if (phone != null && phone.contains(" ")) {
//                String[] phoneArr = phone.split(" ");
//                System.out.println("Landline : " + phoneArr[0]);
//                System.out.println("Mobile   : " + phoneArr[1]);
//                people.setLandline(phoneArr[0]);
//                people.setMobile(phoneArr[1]);
//            } else {
//                System.out.println("Phone Number : " + phone);
//                people.setLandline(phone);
//            }
//            System.out.println("----------------------------------------------------");
//            if(address != null){
//                address = address.replaceAll(",", " ");
//            }
//            people.setStreetAddress(address);
//            
//            dao.updatePeople(people);
//            
//        } catch (Exception ex) {
//        }
//    }
//}
