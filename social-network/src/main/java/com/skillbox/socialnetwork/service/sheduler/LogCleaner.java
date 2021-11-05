package com.skillbox.socialnetwork.service.sheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Component
public class LogCleaner {

    private static final String pattern = "yyyy-MM-dd";
    private static final Pattern date = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
    private final int LOGS_KEEP_EXIST = 5;

    @Scheduled(cron = "0 0 4 * * *")
    public void cleanOldLogs() {
        log.info("Запустили процесс");
        try {
            Files.walk(Paths.get("./logs/"))
                    .map(Path::toFile)
                    .filter(x -> x.getName().endsWith(".log"))
                    .forEach(x -> {
                        if (getDateFromName(x).isBefore(LocalDate.now().minusDays(LOGS_KEEP_EXIST))) {
                            if (x.delete()) {
                                log.info("Нашли старый файл {} и удалили его", x.getName());
                            } else {
                                log.warn("Нашли старый файл {} но удалить его не вышло", x.getName());
                            }
                        }
                    });
        } catch (IOException e) {
            log.error(Arrays.toString(e.getStackTrace()));
        }
    }

    public static LocalDate getDateFromName(File file) {
        Matcher matcher = date.matcher(file.getName());
        if (matcher.find()) {
            return LocalDate.parse(matcher.group(), formatter);
        } else return LocalDate.now();
    }
}
