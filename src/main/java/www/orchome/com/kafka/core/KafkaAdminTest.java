package www.orchome.com.kafka.core;

import org.apache.kafka.clients.admin.*;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class KafkaAdminTest {

    public List<String> listConsumerGroup(String bootstrapServers) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", bootstrapServers);
        properties.put("connections.max.idle.ms", 10000);
        properties.put("request.timeout.ms", 5000);

        try (AdminClient client = AdminClient.create(properties)) {
            ListConsumerGroupsResult rs = client.listConsumerGroups();
            try {
                return rs.valid()
                        .get(10, TimeUnit.SECONDS)
                        .stream()
                        .map(ConsumerGroupListing::groupId)
                        .collect(Collectors.toList());
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public Set<String> listTopics(String bootstrapServers) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", bootstrapServers);
        properties.put("connections.max.idle.ms", 10000);
        properties.put("request.timeout.ms", 5000);
        try (AdminClient client = AdminClient.create(properties)) {
            ListTopicsResult result = client.listTopics();
            try {
                result.listings().get().forEach(topic -> {
                    System.out.println(topic);
                });
                return result.names().get();
            } catch (InterruptedException | ExecutionException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    private void createTopics(String bootstrapServers) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", bootstrapServers);
        properties.put("connections.max.idle.ms", 10000);
        properties.put("request.timeout.ms", 5000);
        try (AdminClient client = AdminClient.create(properties)) {
            CreateTopicsResult result = client.createTopics(Arrays.asList(
                    new NewTopic("topic1", 1, (short) 1),
                    new NewTopic("topic2", 1, (short) 1),
                    new NewTopic("topic3", 1, (short) 1)
            ));
            try {
                result.all().get();
            } catch (InterruptedException | ExecutionException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    private void newP(String bootstrapServers) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", bootstrapServers);
        properties.put("connections.max.idle.ms", 10000);
        properties.put("request.timeout.ms", 5000);

        try (AdminClient client = AdminClient.create(properties)) {
            Map newPartitions = new HashMap<>();
            newPartitions.put("topic1", NewPartitions.increaseTo(2));
            CreatePartitionsResult rs = client.createPartitions(newPartitions);
            try {
                rs.all().get();
            } catch (InterruptedException | ExecutionException e) {
                throw new IllegalStateException(e);
            }
        }
    }

//    private void newP(String bootstrapServers) {
//        Properties properties = new Properties();
//        properties.put("bootstrap.servers", bootstrapServers);
//        properties.put("connections.max.idle.ms", 10000);
//        properties.put("request.timeout.ms", 5000);
//
//        try (AdminClient client = AdminClient.create(properties)) {
//            List<ConfigEntry> entities = new ArrayList<>();
//            for (String key : topicParam.getConfig().keySet()) {
//                ConfigEntry entity = new ConfigEntry(key, topicParam.getConfig().get(key));
//                entities.add(entity);
//            }
//            Map<ConfigResource, Config> configs = new HashMap<>();
//            configs.put(new ConfigResource(ConfigResource.Type.TOPIC, "topic", new Config(entities));
//            AlterConfigsResult rs = client.alterConfigs(configs);
//            try {
//                rs.all().get();
//            } catch (InterruptedException | ExecutionException e) {
//                throw new IllegalStateException(e);
//            }
//        }
//    }

    public static void main(String argv[]) {
//        AdminClientConfig config = null;
//        AdminClient adminClient = new KafkaAdminClient(config);
//        Map newPartitions = new HashMap<>();
//        newPartitions.put(topic, NewPartitions.increaseTo(getConcurrency(topic)));
//        CreatePartitionsResult result = client.createPartitions(newPartitions);
//        System.out.println("topic修改分区结果：" + result.all().get());

        // 创建主题
//        new KafkaAdminTest().createTopics("172.30.98.36:9093");
//        new KafkaAdminTest().newP("172.30.98.36:9093");
        new KafkaAdminTest().listConsumerGroup("172.30.114.30:9092");
    }
}
