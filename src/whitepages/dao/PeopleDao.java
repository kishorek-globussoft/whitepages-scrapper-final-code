/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package whitepages.dao;

import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import whitepages.crawlers.StartAllDataCrawler;
import whitepages.csvgenerator.CsvGenerator;
import whitepages.entity.PrimaryUrl;
import whitepages.entity.People;
import whitepages.entity.ProxyData;
import whitepages.entity.SecondaryUrl;
import whitepages.entity.TertiaryUrl;
import whitepages.gui.WhitepagesGui;

/**
 *
 * @author GLB-029
 */
public class PeopleDao {

//    CsvGenerator generator;

    SessionFactory factory = new AnnotationConfiguration().
            configure().setProperty("hibernate.connection.url", "jdbc:sqlite:" + System.getProperty("user.home") + "\\appdata\\Whitepages-scraper\\whitepages.db").
            buildSessionFactory();

    public PeopleDao(SessionFactory factory) {
        
    }

    
    public void savePrimaryUrls(List<String> urlList) {

        Session session = null;
        Transaction transaction;
        for (String url : urlList) {
            if (!StartAllDataCrawler.running) {
                return;
            }
            try {
                session = factory.openSession();
                transaction = session.beginTransaction();
                PrimaryUrl primaryUrl = new PrimaryUrl();
                primaryUrl.setUrl(url);
                primaryUrl.setScraped(false);
                int id = (int) session.save(primaryUrl);
                transaction.commit();
                System.out.println("Primary Url ID : " + id);
            } catch (HibernateException ex) {
            } finally {
                session.close();
                factory.close();
            }
        }
        WhitepagesGui.errorLogText.append("\nSaved all Primary Data to database");
    }

    public void saveSecondaryUrls(List<String> urlList) {
        Session session = null;
        Transaction transaction;
        for (String url : urlList) {
            if (!StartAllDataCrawler.running) {
                return;
            }
            try {
                session = factory.openSession();
                transaction = session.beginTransaction();
                SecondaryUrl first = new SecondaryUrl();
                first.setUrl(url);
                int id = (int) session.save(first);
                transaction.commit();
                System.out.println("Secondary Url ID : " + id);
            } catch (HibernateException ex) {
            } finally {
                session.close();
                factory.close();
            }
        }
        WhitepagesGui.errorLogText.append("\nSaved all Secondary Data to database..");
    }

    public void saveTertiaryUrls(List<TertiaryUrl> List) {
        Session session = null;
        Transaction transaction;
        for (TertiaryUrl tertiaryUrl : List) {
            if (!StartAllDataCrawler.running) {
                return;
            }
            try {
                session = factory.openSession();
                transaction = session.beginTransaction();
                tertiaryUrl.setScraped(false);
                int id = (int) session.save(tertiaryUrl);
                transaction.commit();
                System.out.println("Tertiary Url ID : " + id);
            } catch (HibernateException ex) {
            } finally {
                session.close();
                factory.close();
            }
        }
        WhitepagesGui.errorLogText.append("\nSaved all Secondary Data to database..");
    }

    public void savePeopleList(List<People> peopleList) {
        Session session = null;
        Transaction transaction;
        for (People people : peopleList) {
            try {
                session = factory.openSession();
                transaction = session.beginTransaction();
                int id = (int) session.save(people);
                transaction.commit();
                System.out.println("People ID : " + id);
            } catch (HibernateException ex) {
                System.out.println(ex.getMessage());
            } finally {
                session.close();
                factory.close();
            }
        }
    }

    public void savePeople(People people) {
        Session session = null;
        Transaction transaction;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            int id = (int) session.save(people);
            transaction.commit();
            System.out.println("People ID : " + id);
        } catch (HibernateException ex) {
        } finally {
            session.close();
            factory.close();
        }
    }

    public void savePeople(People people, String fileName) {
        Session session = null;
        Transaction transaction;
        try {
            session = factory.openSession();
            transaction = session.beginTransaction();
            int id = (int) session.save(people);
            transaction.commit();
            System.out.println("People ID : " + id);

        } catch (HibernateException ex) {
        } finally {
            session.close();
            factory.close();
        }
    }

    public List<String> getFirstUrl(int first, int last) {
        Session session;
        session = factory.openSession();
        List<String> list = session.createSQLQuery("Select url from FirstRound where id >= " + first + " and id <= " + last).list();
        session.close();
        factory.close();
        return list;
    }

    public void deleteAllData() {
        Session session;
        session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        int deletedRowCount = session.createQuery("delete from People").executeUpdate();
        transaction.commit();
        WhitepagesGui.outputText.append("\n" + deletedRowCount + " records deleted successfully");
        session.close();
        factory.close();
    }

    public List<String> getSecondUrl(int first, int last) {
        Session session;
        session = factory.openSession();
        List<String> list = session.createSQLQuery("Select url from SecondRound where id >= " + first + " and id <= " + last).list();
        session.close();
        factory.close();
        return list;
    }

    public List<People> getProfileUrl(int first, int last) {
        Session session;
        session = factory.openSession();
        List<People> list = session.createQuery("From People where id >= " + first + " and id <= " + last).list();
        session.close();
        factory.close();
        return list;
    }

    public List<ProxyData> getProxyData() {
        Session session;
        session = factory.openSession();
        List<ProxyData> list = session.createQuery("From ProxyData where working = 1").list();
        session.close();
        factory.close();
        return list;
    }

    public void updatePeople(People people) {
        Session session = null;
        Transaction tx = null;
        try {
            session = factory.openSession();
            tx = session.beginTransaction();
            session.update(people);
            System.out.println("Update ID : " + people.getId());
            tx.commit();
        } catch (HibernateException e) {
            System.out.println("Update Failed..");
        } finally {
            session.close();
            factory.close();
        }
    }

    public void deleteNonWorkingProxies() {
        Session session;
        session = factory.openSession();
        Transaction transaction = session.beginTransaction();
        int deletedRowCount = session.createQuery("delete from ProxyData where working = 0").executeUpdate();
        transaction.commit();
        WhitepagesGui.outputText.append("\n" + deletedRowCount + " proxies were not working and deleted successfully");
        session.close();
        factory.close();
    }

    public void saveProxyData(List<ProxyData> proxyList) {
        Session session = null;
        Transaction tx = null;
        for (ProxyData proxyData : proxyList) {
            try {
                session = factory.openSession();
                tx = session.beginTransaction();
                session.save(proxyData);
                tx.commit();
                session.close();
            } catch (HibernateException e) {
//                e.printStackTrace();
                WhitepagesGui.errorLogText.append("\nThis record already exists : " + proxyData.getIp() + " : " + proxyData.getPort());
                session.close();
                factory.close();
            }
        }
        WhitepagesGui.outputText.append("\nProxies Uploaded Successfully");
        try {
            factory.close();
        } catch (Exception e) {
        }
    }

    public void updateProxyData(String ip, int port) {
        Session session = null;
        Transaction tx = null;
        try {
            session = factory.openSession();
            tx = session.beginTransaction();
            List<ProxyData> list = session.createQuery("from ProxyData where ip = '" + ip + "' and port = " + port).list();
            if (!list.isEmpty()) {
                ProxyData proxyData = list.get(0);
                proxyData.setWorking(0);
                session.update(proxyData);
                tx.commit();
                WhitepagesGui.errorLogText.append("\nProxy " + ip + "updated :" + ip);
            }
        } catch (HibernateException e) {
            System.out.println("Primary url Update Failed..");
        } finally {
            session.close();
            factory.close();
        }
    }

    public void updatePrimaryUrls(PrimaryUrl primaryUrl) {
        Session session = null;
        Transaction tx = null;
        try {
            session = factory.openSession();
            tx = session.beginTransaction();
            session.update(primaryUrl);
            System.out.println("Update ID : " + primaryUrl.getId());
            tx.commit();
        } catch (HibernateException e) {
            System.out.println("Primary url Update Failed..");
        } finally {
            session.close();
            factory.close();
        }
    }

    public void updateSecondaryUrls(SecondaryUrl secondaryUrl) {
        Session session = null;
        Transaction tx = null;
        try {
            session = factory.openSession();
            tx = session.beginTransaction();
            session.update(secondaryUrl);
            System.out.println("Update ID : " + secondaryUrl.getId());
            tx.commit();
        } catch (HibernateException e) {
            System.out.println("Update Failed..");
        } finally {
            session.close();
            factory.close();
        }
    }

    public void updateTertiaryUrls(TertiaryUrl tertiaryUrl) {
        Session session = null;
        Transaction tx = null;
        try {
            session = factory.openSession();
            tx = session.beginTransaction();
            session.update(tertiaryUrl);
            System.out.println("Update ID : " + tertiaryUrl.getId());
            tx.commit();
        } catch (HibernateException e) {
            System.out.println("Update Failed..");
        } finally {
            session.close();
            factory.close();
        }
    }

    public List<People> getAllPeople() {
        Session session = null;
        Transaction tx = null;
        List<People> peopleList = null;
        try {
            session = factory.openSession();
            peopleList = session.createQuery("From People").list();
            session.close();
            factory.close();
        } catch (HibernateException e) {
            session.close();
            factory.close();
        }
        return peopleList;
    }

    public List<PrimaryUrl> getPrimaryUrls() {
        Session session = null;
        Transaction tx = null;
        List<PrimaryUrl> primaryUrlList = null;
        try {
            session = factory.openSession();
            primaryUrlList = session.createQuery("From PrimaryUrl where scraped = 0").list();
            session.close();
            factory.close();
        } catch (HibernateException e) {
            session.close();
            factory.close();
        }
        return primaryUrlList;
    }

    public List<SecondaryUrl> getSecondaryUrls() {
        Session session = null;
        Transaction tx = null;
        List<SecondaryUrl> secondaryUrlList = null;
        try {
            session = factory.openSession();
            secondaryUrlList = session.createQuery("From SecondaryUrl where scraped = 0").list();
            session.close();
            factory.close();
        } catch (HibernateException e) {
            session.close();
            factory.close();
        }
        return secondaryUrlList;
    }

    public List<TertiaryUrl> getTertiaryUrls() {
        Session session = null;
        Transaction tx = null;
        List<TertiaryUrl> tertiaryUrlList = null;
        try {
            session = factory.openSession();
            tertiaryUrlList = session.createQuery("From TertiaryUrl where scraped = 0").list();
            session.close();
            factory.close();
        } catch (HibernateException e) {
            session.close();
            factory.close();
        }
        return tertiaryUrlList;
    }
}
