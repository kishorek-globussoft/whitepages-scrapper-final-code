/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package whitepages.crawlers;

import org.hibernate.SessionFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import whitepages.csvgenerator.CsvGenerator;
import whitepages.dao.PeopleDao;
import whitepages.entity.People;
import whitepages.entity.ProxyData;
import whitepages.gui.WhitepagesGui;
import static whitepages.gui.WhitepagesGui.outputText;
import whitepages.utility.FetchPageSource;

/**
 *
 * @author GLB-029
 */
public class ProfileDataCrawler extends Thread {

    String url;
    ProxyData proxyData;
    String searchName;
    int proxyType;
    SessionFactory factory;

//    public ProfileDataCrawler(String url, int proxyType) {
//        this.url = url;
//        this.proxyType = proxyType;
//    }
    public ProfileDataCrawler(String url, String searchName, int proxyType, SessionFactory factory) {
        this.factory = factory;
        this.searchName = searchName;
        this.url = url;
        this.proxyType = proxyType;
    }

//    public static void main(String[] args) {
////        String refUrl = "http://www.whitepages.com/name/Greg-C-Paul/Fresno-CA/69icruh";
//        String refUrl = "http://www.whitepages.com/name/Asia-S-John/Flint-MI/2wavqsq";
//        ProfileDataCrawler ref = new ProfileDataCrawler(refUrl, "mathews", 0,null);
//        ref.run();
//    }
    static int count = 0;

    @Override
    public void run() {
        try {
            PeopleDao dao = new PeopleDao(factory);
            People people = new People();
            String streetAddress = null;
            String stateAddress = null;
            String state = null;
            String city = null;
            String zipcode = null;
            String phone = null;
            String age = null;
            String name = null;
            String fileName = System.getProperty("user.home") + "\\Desktop\\Whitepages-scraper\\" + searchName + ".csv";
            CsvGenerator generator = null;
            String response = FetchPageSource.fetchPageSource(url, proxyType, factory);
            if (!response.isEmpty() || response != null) {
                Document document = Jsoup.parse(response);
                if (url.contains("whitepages.com/people")) {
                    try {
                        phone = document.select("span[class=number] a[class=clickstream-link]").text().trim();
                    } catch (Exception e) {
                    }
                    try {
                        streetAddress = document.select("span[class*=address]").text();
                    } catch (Exception e) {
                    }
                    try {
                        name = document.select("div[class=name-plate] span[class=name block]").first().text();
                    } catch (Exception e) {
                    }
                    try {
                        age = document.select("div[class=name-plate] span[class=subtitle block] span[itemprop=ageRange]").first().text();
                    } catch (Exception e) {
                    }
                    System.out.println("PEOPLE :" + name + " " + phone + " " + age + " " + streetAddress + " " + url);
                    if (streetAddress != null) {
                        String[] splitAddress = streetAddress.split(",");
                        if (splitAddress.length > 1) {
                            city = splitAddress[0];
                            state = splitAddress[1];
                            people.setCity(city);
                            people.setState(state);
                            people.setStreetAddress(" ");
                        } else {
                            people.setStreetAddress(streetAddress);
                        }
                    } else {
                        people.setStreetAddress(" ");
                    }
                    if (name != null) {
                        String[] nameArr = name.split(" ");
                        if (nameArr.length == 2) {
                            people.setFirstname(nameArr[0]);
                            people.setMiddlename(" ");
                            people.setLastname(nameArr[1]);
                        } else if (nameArr.length == 3) {
                            people.setFirstname(nameArr[0]);
                            people.setMiddlename(nameArr[1]);
                            people.setLastname(nameArr[2]);
                        } else {
                            people.setFirstname(name);
                        }
                    } else {
                        return;
                    }
                    if (phone != null && phone.contains(" ")) {
                        String[] phoneArr = phone.split(" ");
                        if (phoneArr.length > 1) {
                            people.setLandline(phoneArr[0]);
                            people.setMobile(phoneArr[1]);
                        } else {
                            people.setLandline(phone);
                        }
                    } else {
                        people.setLandline(" ");
                    }
                    if (age != null) {
                        people.setAge(age);
                    } else {
                        people.setAge(" ");
                    }
                    people.setUrl(url);
                    people.setZipcode(" ");
                    outputText.append("\nName    : " + name);
                    outputText.append("\nAge     : " + age);
                    outputText.append("\nState   : " + state);
                    outputText.append("\nCity    : " + city);
                    outputText.append("\nPhone   : " + phone);
                    outputText.append("\n-----------------------------------------");

                    dao.savePeople(people, fileName);
                    generator = new CsvGenerator(people, fileName);
                    generator.start();
                } else {
                    try {
                        name = document.select("div[class=name-plate] span[class=name block]").first().text();
                    } catch (Exception e) {
                    }
                    try {
                        age = document.select("div[class=name-plate] span[class*=subtitle] span[itemprop=ageRange]").first().text();
                    } catch (Exception e) {
                    }
                    try {
                        phone = document.select("div[class=detail person vcard] div[class=phones clear-all padded-top] ul[class=unstyled] a").text().trim();
                    } catch (Exception e) {

                    }
                    try {
                        streetAddress = document.select("address[class=address adr] span").first().text();
                    } catch (Exception e) {
                    }
                    try {
                        stateAddress = document.select("address[class=address adr] span").last().text();
                    } catch (Exception e) {
                    }

                    if (stateAddress != null) {
                        String[] cityStateArr = stateAddress.split(",");
                        city = cityStateArr[0];
                        String[] stateZip = cityStateArr[1].trim().split(" ");
                        if (stateZip.length > 1) {
                            state = stateZip[0];
                            zipcode = stateZip[1];
                        }
                    }
                    if (name != null) {
                        String[] nameArr = name.split(" ");
                        if (nameArr.length > 1) {
                            if (nameArr.length == 2) {
                                people.setFirstname(nameArr[0]);
                                people.setMiddlename(" ");
                                people.setLastname(nameArr[1]);
                            } else if (nameArr.length == 3) {
                                people.setFirstname(nameArr[0]);
                                people.setMiddlename(nameArr[1]);
                                people.setLastname(nameArr[2]);
                            }
                        } else {
                            people.setFirstname(name);
                        }
                    } else {
                        return;
                    }
                    if (phone != null) {
                        if (phone.contains(" ")) {
                            String[] phoneArr = phone.split(" ");
                            if (phoneArr.length > 1) {
                                people.setLandline(phoneArr[0]);
                                people.setMobile(phoneArr[1]);
                            }
                        } else {
                            people.setLandline(phone);
                        }
                    } else {
                        people.setLandline(" ");
                    }
                    if (age != null) {
                        people.setAge(age);
                    } else {
                        people.setAge(" ");
                    }

                    people.setUrl(url);

                    if (streetAddress != null) {
                        people.setStreetAddress(streetAddress);
                    } else {
                        people.setStreetAddress(" ");
                    }
                    if (city != null) {
                        people.setCity(city);
                    } else {
                        people.setCity(" ");
                    }
                    if (state != null) {
                        people.setState(state);
                    } else {
                        people.setState(" ");
                    }
                    if (zipcode != null) {
                        people.setZipcode(zipcode);
                    } else {
                        people.setZipcode(" ");
                    }

                    System.out.println("Name : " + name + " " + age + " " + phone + " " + streetAddress + " " + city + " " + state + " " + zipcode + " " + url);
                    outputText.append("\nName    : " + name);
                    outputText.append("\nAge     : " + age);
                    outputText.append("\nAddress : " + streetAddress);
                    outputText.append("\nState   : " + state);
                    outputText.append("\nCity    : " + city);
                    outputText.append("\nZipCode : " + zipcode);
                    outputText.append("\nPhone   : " + phone);
                    outputText.append("\n-----------------------------------------");

                    dao.savePeople(people);
                    generator = new CsvGenerator(people, fileName);
                    generator.start();
                }
            } else {
                System.out.println("No profile response");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            WhitepagesGui.errorLogText.append("\nUnexpected error getting profile data.. \n Status : " + ex.getMessage());
        }
    }
}
