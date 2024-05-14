package com.twitter.microservice.twitter.to.kafka.runner.impl;

import com.twitter.microservice.config.TwitterStatusData;
import com.twitter.microservice.dto.TwitterStatus;
import com.twitter.microservice.twitter.to.kafka.listener.TwitterStatusListener;
import com.twitter.microservice.twitter.to.kafka.runner.StreamRunner;
import com.twitter.microservice.twitter.to.kafka.utils.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class TwitterStreamRunner implements StreamRunner {
    private final TwitterStatusData twitterStatusData;
    private final TwitterStatusListener twitterStatusListener;
    private static final String SPACE = " ";

    private static final Random RANDOM = new Random();
    private static final String[] WORDS = new String[]{
            "congue",
            "massa",
            "Fusce",
            "posuere",
            "magna",
            "sed",
            "pulvinar",
            "ultricies",
            "purus",
            "lectus",
            "malesuada",
            "libero",
            "Lorem",
            "ipsum",
            "dolor",
            "sit",
            "amet",
            "consectetuer",
            "adipiscing",
            "elit",
            "Maecenas",
            "porttitor"
    };

    public TwitterStreamRunner(TwitterStatusData twitterStatusData,
                               TwitterStatusListener twitterStatusListener) {

        this.twitterStatusData = twitterStatusData;
        this.twitterStatusListener = twitterStatusListener;
    }

    @Override
    public void start() {
        // now set up avro and kafka modules

            try {
                while (true) {
                    int statusLen = RANDOM.nextInt(twitterStatusData.getMaxlength() - twitterStatusData.getMinLength() + 1)
                            + twitterStatusData.getMinLength();
                    String statusContent = getStatusContent(statusLen);
                    TwitterStatus status = createTwitterStatus(statusContent);
                    log.info("status --> {} {}",status.getStatus(),status.getId());
                    twitterStatusListener.onStatus(status);
                    Thread.sleep(10000);
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("Found error while and sending status ");
            }
    }

    private TwitterStatus createTwitterStatus(String status) {
        String userId = String.valueOf(RANDOM.nextInt(101));
        return TwitterStatus.builder()
                .id(AppUtils.genRandomLong())
                .createdAt(LocalDateTime.now())
                .userId(userId)
                .status(status)
                .build();
    }


    private String getStatusContent(int statusLen) {
        StringBuilder sb = new StringBuilder();
        int keywordsListSize = twitterStatusData.getStringList().size();
        for (int i = 0; i < statusLen; i++) {
            sb.append(WORDS[RANDOM.nextInt(WORDS.length)]).append(SPACE);
            if (i == statusLen / 2) {
                sb.append(twitterStatusData.getStringList().get(RANDOM.nextInt(keywordsListSize))).append(SPACE);
            }
        }
        return sb.toString().trim();
    }
}
