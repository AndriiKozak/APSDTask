package com.mycompany.apsdtask.analytics;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TreadDumperDeamon implements Runnable {

    private boolean notInterrupted = true;
    private final long timeOut;
    private final Thread daemonThread;
    private final Path path;

    TreadDumperDeamon(@Value("300000") long timeout, @Value("dumps.txt") String dumppath) {
        this.timeOut = timeout;
        daemonThread = new Thread(this);
        daemonThread.setDaemon(true);
        path = Paths.get(dumppath);
    }

    @PostConstruct
    public void init() {
        daemonThread.start();
    }

    @Override
    public void run() {
        final StringBuilder s = new StringBuilder();
        while (notInterrupted) {

            try (BufferedWriter writer = Files.newBufferedWriter(path, CREATE, APPEND)) {
                Thread.sleep(timeOut);
                String result = Thread.getAllStackTraces()
                        .entrySet()
                        .stream()
                        .flatMap(entity -> Stream.concat(logThread(entity.getKey()), logStackTrace(entity.getValue())))
                        .collect(Collectors.joining(LocalDateTime.now().toString()+System.lineSeparator(), System.lineSeparator(), System.lineSeparator()));               
                writer.write(result);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            } catch (InterruptedException ex) {
                notInterrupted = false;
            }

        }
    }

    private static Stream<String> logThread(Thread thread) {
        return Stream.of(
                Stream.of(thread.getId(), thread.getName(), thread.isDaemon(), thread.getPriority(), thread.getState())
                        .map(Object::toString).collect(Collectors.joining(" ")));
    }

    private static Stream<String> logStackTrace(StackTraceElement[] array) {
        return Arrays.stream(array).map(StackTraceElement::toString);
    }

    @PreDestroy
    public void Destroy() {
        daemonThread.interrupt();
    }
}
