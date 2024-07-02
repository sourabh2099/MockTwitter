package com.twitter.microservice.kafka.streams.service.runner.impl;

import com.twitter.microservice.config.KafkaConfigData;
import com.twitter.microservice.config.KafkaStreamsConfigData;
import com.twitter.microservice.kafka.model.TwitterAnalyticsAvroModel;
import com.twitter.microservice.kafka.model.TwitterStatusModel;
import com.twitter.microservice.kafka.streams.service.runner.StreamRunner;
import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueStore;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

@Component
public class KafkaStreamRunner implements StreamRunner<String, Long> {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaStreamRunner.class);
    private final KafkaStreamsConfigData kafkaStreamsConfigData;
    private final KafkaConfigData kafkaConfigData;
    private KafkaStreams kafkaStreams;
    private volatile ReadOnlyKeyValueStore<String, Long> keyValueStore;
    private final Properties streamConfiguration;

    private static final String REGEX = "\\W+";

    public KafkaStreamRunner(KafkaStreamsConfigData kafkaStreamsConfigData,
                             KafkaConfigData kafkaConfigData,
                             @Qualifier("streamConfiguration") Properties streamConfiguration) {
        this.kafkaStreamsConfigData = kafkaStreamsConfigData;
        this.kafkaConfigData = kafkaConfigData;
        this.streamConfiguration = streamConfiguration;
    }

    @Override
    public void start() {
        final Map<String, String> serdeConfig = Collections.singletonMap(
                kafkaConfigData.getSchemaRegistryUrlKey(), kafkaConfigData.getSchemaRegistryUrl());
        final StreamsBuilder streamBuilder = new StreamsBuilder();
        final KStream<Long, TwitterStatusModel> twitterAvroModelKStream =
                getTwitterAvroModelKStream(serdeConfig, streamBuilder);
        createTopology(twitterAvroModelKStream, serdeConfig);
        startStreaming(streamBuilder);
    }

    private void startStreaming(StreamsBuilder streamBuilder) {
        final Topology topology = streamBuilder.build();
        LOG.info("Defined Topology: {}",topology.describe());
        kafkaStreams = new KafkaStreams(topology,streamConfiguration);
        kafkaStreams.start();

    }

    @Override
    public Long getValueByKey(String key) {
        return StreamRunner.super.getValueByKey(key);
    }


    private void createTopology(KStream<Long, TwitterStatusModel> twitterAvroModelKStream,
                                Map<String, String> serdeConfig) {
        Pattern pattern = Pattern.compile(REGEX,Pattern.UNICODE_CHARACTER_CLASS);
        Serde<TwitterAnalyticsAvroModel> serdeTwitterAnalyticsAvroModel = getSerdeAnalyticsModel(serdeConfig);
        twitterAvroModelKStream
                .flatMapValues(value -> Arrays.asList(pattern.split(value.getText().toLowerCase())))
                .groupBy((key,word) -> word)
                .count(Materialized.
                        <String, Long, KeyValueStore<Bytes, byte[]>>as(kafkaStreamsConfigData.getWordCountStoreName()))
                .toStream()
                .map(mapToAnalyticsModel())
                .to(kafkaStreamsConfigData.getOutputTopicName(),
                        Produced.with(Serdes.String(),serdeTwitterAnalyticsAvroModel));
    }

    private KeyValueMapper<String,Long,KeyValue<? extends String, ? extends TwitterAnalyticsAvroModel>>
    mapToAnalyticsModel() {
        return (word,count) -> {
            LOG.info("Sending to topic {} word {} - count {} ",
                    kafkaStreamsConfigData.getOutputTopicName(),word,count);
            return new KeyValue<>(word,TwitterAnalyticsAvroModel
                    .newBuilder()
                    .setWord(word)
                    .setWordCount(count)
                    .setCreatedAt(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)).build()
            );
        };
    }

    private Serde<TwitterAnalyticsAvroModel> getSerdeAnalyticsModel(Map<String, String> serdeConfig) {
        Serde<TwitterAnalyticsAvroModel> serdeTwitterAnalytics = new SpecificAvroSerde<>();
        serdeTwitterAnalytics.configure(serdeConfig,false); // register the schema on schema registry for the output type
        return serdeTwitterAnalytics;
    }

    private KStream<Long, TwitterStatusModel> getTwitterAvroModelKStream(Map<String, String> serdeConfig,
                                                                         StreamsBuilder streamBuilder) {
        final Serde<TwitterStatusModel> serdeTwitterAvroModel = new SpecificAvroSerde<>();
        serdeTwitterAvroModel.configure(serdeConfig, false); ; // register the schema on schema registry for the input type
        return streamBuilder.stream(kafkaStreamsConfigData.getInputTopicName(), Consumed.with(Serdes.Long(),
                serdeTwitterAvroModel));
    }
}
