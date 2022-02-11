package com.personal.springframework.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @description:kafka消费监听,kafka消费者线程不安全，集群需要加锁解决重复消费
 * @author: anshaojun
 * @time: 2021-05-18 16:47
 */
@Component
@Slf4j
public class KafkaConsumer {

    /**
     * @MethodName: receiveASR
     * @Description: 自动提交模式消费
     * @Param: [record, topic]
     * @Return: void
     * @Author: anshaojun
     * @Date: 2021-05-19 08:38
     **/
    /*@KafkaListener(topics = "#{'${kafka.consumer.topic1}'}",groupId = "${spring.kafka.consumer.group-id}")
    public void receiveASR(ConsumerRecord<?, ?> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic) {
        Optional message = Optional.ofNullable(record.value());
        if (message.isPresent()) {
            Object msg = message.get();
            log.info("topic_test1 消费了： Topic:" + topic + ",Message:" + msg);
        }
    }*/

    /**
     * @MethodName: receiveASR
     * @Description: 手动提交模式消费
     * @Param: [record, topic]
     * @Return: void
     * @Author: anshaojun
     * @Date: 2021-05-19 09:20
     **/
    /*@KafkaListener(topics = "#{'${kafka.consumer.topic1}'}",groupId = "${spring.kafka.consumer.group-id}")
    public void receiveASR(ConsumerRecord<?, ?> record, @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,Acknowledgment ack) {
        Optional message = Optional.ofNullable(record.value());
        if (message.isPresent()) {
            Object msg = message.get();
            log.info("topic_test1 消费了： Topic:" + topic + ",Message:" + msg);
            ack.acknowledge();
        }
    }*/

    /**
     * @MethodName: consumer
     * @Description: 手动提交模式批量消费
     * @Param: [records, ack]
     * @Return: void
     * @Author: anshaojun
     * @Date: 2021-05-19 09:39
     **/
    /*@KafkaListener(topics = "#{'${kafka.consumer.topic1}'}", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "containerFactory")
    public void consumer(List<ConsumerRecord<?, ?>> records, Acknowledgment ack) {
        ack.acknowledge();
        records.forEach(record -> {
            Optional<?> kafkaMessage = Optional.ofNullable(record.value());
            kafkaMessage.ifPresent(u -> {
                System.out.println(Thread.currentThread().getName() + ":" + u);
            });
        });
    }*/
}
