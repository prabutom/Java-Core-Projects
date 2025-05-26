package com.logging.framework.specialized;

import com.logging.framework.core.BaseLogger;
import com.logging.framework.core.LogWriter;
import com.logging.framework.context.AppContext;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;

public class KafkaLogger extends BaseLogger<KafkaLogger> {

    public KafkaLogger(String name, LogWriter logWriter, AppContext appContext) {
        super(name, logWriter, appContext);
    }

    public void logMessagePublished(ProducerRecord<?, ?> record) {
        this.with("topic", record.topic())
                .with("partition", record.partition())
                .with("key", record.key())
                .info("Message published to Kafka");
    }

    public void logMessageReceived(ConsumerRecord<?, ?> record) {
        this.with("topic", record.topic())
                .with("partition", record.partition())
                .with("offset", record.offset())
                .with("key", record.key())
                .info("Message received from Kafka");
    }

    public void logConsumerCommit(boolean success) {
        if (success) {
            this.info("Consumer commit successful");
        } else {
            this.error("Consumer commit failed");
        }
    }

    public void logConsumerError(Throwable error) {
        this.error("Consumer error occurred", error);
    }
}
