package com.skillbox.socialnetwork.shedule;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Component
public class DataGenerator {

    @Scheduled(fixedDelay = 10000)
    public void generateData() {
        StringBuilder builder = new StringBuilder();
        builder.append("databaseChangeLog:\n");
        builder.append("  - changeSet:\n");
        builder.append("      id: 2021101402\n");
        builder.append("      author: m.gutorov\n");
        builder.append("      changes:\n");
        for (int i = 1; i <= 1000; i++) {
            for (int j = 0; j < 4; j++) {
                builder.append("        - insert:\n");
                builder.append("            tableName: post2tag\n");
                builder.append("            columns:\n");
                builder.append("              - column:\n");
                builder.append("                  name: post_id\n");
                builder.append("                  value: ");
                builder.append(i);
                builder.append("\n");
                builder.append("              - column:\n");
                builder.append("                  name: tag_id\n");
                builder.append("                  value: ");
                builder.append(ThreadLocalRandom.current().nextInt(1,33));
                builder.append("\n");
            }
        }
        try (PrintWriter out = new PrintWriter("filename.yml")) {
            out.println(builder);
            log.info("All right");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
