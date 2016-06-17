/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package whitepages.extra;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import whitepages.dao.PeopleDao;
import whitepages.entity.People;

/**
 *
 * @author GLB-029
 */
public class StartCrawler {

    public void run(){
//    public static void main(String[] args) {
//        PeopleDao dao = new PeopleDao();
        Scanner scanner = new Scanner(System.in);
        System.out.println("1. For running Name and Age crawler");
        System.out.println("2. For running primary Url crawler");
        System.out.println("3. For running Secondary Url crawler");
        System.out.println("4. For running Address Phone crawler");
        System.out.print("Enter you choice : ");
        int choice = scanner.nextInt();
        ExecutorService executor;
//        switch (choice) {
//            case 1:
//                executor = Executors.newFixedThreadPool(3);
////                List<String> urlList = dao.getSecondUrl(5001, 10000);
////                for (String url : urlList) {
//            {
//                String url = "http://www.whitepages.com/name/john";
//                    AgeUrlCrawler ref = new AgeUrlCrawler(url);
//                    executor.execute(ref);
//            }
////                }
//                executor.shutdown();
//                break;
//            case 2:
//                executor = Executors.newFixedThreadPool(10);
//                List<String> urls = dao.getFirstUrl(1001, 3217);
//                for (String url : urls) {
//                    PrimaryUrlCrawler ref = new PrimaryUrlCrawler(url);
//                    executor.execute(ref);
//                }
//                executor.shutdown();
//                break;
//
//            case 3:
//                executor = Executors.newFixedThreadPool(10);
////                till 12-001
//                List<String> primaryUrls = dao.getFirstUrl(4838, 4972);
//                for (String url : primaryUrls) {
//                    SecondaryUrlCrawler ref = new SecondaryUrlCrawler(url);
//                    executor.execute(ref);
//                }
//                executor.shutdown();
//                break;
//
//            case 4:
//                executor = Executors.newFixedThreadPool(10);
//                List<People> peopleList = dao.getProfileUrl(17205, 20347);
//                for (People people : peopleList) {
//                    AddressAndPhoneCrawler ref = new AddressAndPhoneCrawler(people);
//                    executor.execute(ref);
//                }
//                executor.shutdown();
//                break;
//        }

    }

}
