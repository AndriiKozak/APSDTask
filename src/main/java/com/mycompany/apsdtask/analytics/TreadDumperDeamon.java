/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.apsdtask.analytics;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 *
 * @author Andrii_Kozak1
 */
@Component
public class TreadDumperDeamon implements Runnable {
        boolean notInterrupted = true;
        long timeOut;
        Thread daemonThread;
        Path path;
    TreadDumperDeamon(@Value("10000")long timeout, @Value("dumps.txt")String dumppath){
        this.timeOut = timeout;
        System.out.println("on guard");
        daemonThread = new Thread(this);
        daemonThread.setDaemon(true);
        path = Paths.get(dumppath);
    }
    @PostConstruct
    public void init(){
        daemonThread.start();
    }
    @Override
    public void run() {
        while(notInterrupted){
            try (BufferedWriter writer = Files.newBufferedWriter(path)){
                Thread.sleep(timeOut);
                Map<Thread, StackTraceElement[]> stackTrace = Thread.getAllStackTraces();
                stackTrace.forEach((thread,element) ->{
                    try {
                        writer.newLine();
                        writer.write(thread.toString());                        
                        Arrays.stream(element).forEach((trace) -> {
                            try {
                                writer.write(trace.toString());
                            } catch (IOException ex) {
                                Logger.getLogger(TreadDumperDeamon.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                    } catch (IOException ex) {
                        Logger.getLogger(TreadDumperDeamon.class.getName()).log(Level.SEVERE, null, ex);
                    }
                            });
            } catch (InterruptedException ex) {
                notInterrupted = false;
            } catch (IOException ex) {
                Logger.getLogger(TreadDumperDeamon.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
