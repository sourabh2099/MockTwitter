package com.twitter.microservice.genStream.impl;

import com.twitter.microservice.config.TwitterStatusData;
import com.twitter.microservice.dto.TwitterStatus;
import com.twitter.microservice.genStream.StreamRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

@Component
@Slf4j
public class StreamRunnerImpl implements StreamRunner {

    private final TwitterStatusData twitterStatusData;
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

    public StreamRunnerImpl(TwitterStatusData twitterStatusData) {
        this.twitterStatusData = twitterStatusData;
    }

    @Override
    public void initStream() {
        int statusLen = RANDOM.nextInt(twitterStatusData.getMaxlength() - twitterStatusData.getMinLength() + 1)
                + twitterStatusData.getMinLength();
        String statusContent = getStatusContent(statusLen);
        TwitterStatus status = createTwitterStatus(statusContent);
        // now set up avro and kafka modules
        sendtoKafka(status);
    }

    private void sendtoKafka(TwitterStatus status) {
    }

    private TwitterStatus createTwitterStatus(String status) {
        String userId = "USR" + RANDOM.nextInt(101);
        return TwitterStatus.builder()
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
