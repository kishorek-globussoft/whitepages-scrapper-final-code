///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package whitepages.extra;
//
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import whitepages.crawlers.StartAllThreads;
//
///**
// *
// * @author GLB-029
// */
//public class Demo extends Thread {
//
////    Runnable r = new Runnable() {
////        public void run() {
////            try {
////                while (true) {
////                    Thread.sleep(2000L);
////                    System.out.println("Hello, world!");
////                }
////            } catch (InterruptedException iex) {
////                System.err.println("Message printer interrupted");
////            }
////        }
////    };
////    public void stat() {
////        try {
////            Thread thr = new Thread(r);
////            thr.start();
////            thr.sleep(5000);
////            thr.interrupt();
////        } catch (InterruptedException ex) {
////            Logger.getLogger(Demo.class.getName()).log(Level.SEVERE, null, ex);
////        }
////    }
//    public static void main(String[] args) {
//
//        try {
//            StartAllThreads ref = new StartAllThreads(" ",0);
//            ref.start();
//            Thread.sleep(20000);
//            ref.stopThread();
//        } catch (InterruptedException ex) {
//            Logger.getLogger(Demo.class.getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//
//}
