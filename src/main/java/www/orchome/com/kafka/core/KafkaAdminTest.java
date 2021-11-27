package www.orchome.com.kafka.core;

import org.apache.kafka.clients.admin.*;

import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicPartition;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class KafkaAdminTest {

    public List<Node> listBroders(String bootstrapServers) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", bootstrapServers);
        properties.put("connections.max.idle.ms", 10000);
        properties.put("request.timeout.ms", 5000);

        try (AdminClient client = AdminClient.create(properties)) {
            try {
                DescribeClusterResult rs = client.describeCluster();
                return rs.nodes()
                        .get(10, TimeUnit.SECONDS)
                        .stream()
                        .collect(Collectors.toList());
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public Map<TopicPartition, OffsetAndMetadata> listConsumerGroupOffsets(String bootstrapServers, String group) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", bootstrapServers);
        properties.put("connections.max.idle.ms", 10000);
        properties.put("request.timeout.ms", 5000);
        try (AdminClient client = AdminClient.create(properties)) {
            try {
                ListConsumerGroupOffsetsResult rs = client.listConsumerGroupOffsets(group);
                return rs.partitionsToOffsetAndMetadata().get(10, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public Map<TopicPartition, ListOffsetsResult.ListOffsetsResultInfo> test(String bootstrapServers, TopicPartition topicPartition) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", bootstrapServers);
        properties.put("connections.max.idle.ms", 10000);
        properties.put("request.timeout.ms", 5000);

        try (AdminClient client = AdminClient.create(properties)) {
            try {
                Map<TopicPartition, OffsetSpec> topicPartitionOffsets = new HashMap<>();
                topicPartitionOffsets.put(topicPartition, new OffsetSpec());
                return client.listOffsets(topicPartitionOffsets, new ListOffsetsOptions()).all().get();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public List<Map.Entry<TopicPartition, OffsetAndMetadata>> listPartition(String bootstrapServers, String group) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", bootstrapServers);
        properties.put("connections.max.idle.ms", 10000);
        properties.put("request.timeout.ms", 5000);

        try (AdminClient client = AdminClient.create(properties)) {
            try {
                Collection<TopicPartition> partitions = new ArrayList<>();
                TopicPartition topicPartition = new TopicPartition("test3", 0);
                partitions.add(topicPartition);
                client.listPartitionReassignments();
                DescribeProducersResult rs = client.describeProducers(partitions);
                return null;
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }


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

    public Map<String, ConsumerGroupDescription> describeConsumer(String bootstrapServers, String group) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", bootstrapServers);
        properties.put("connections.max.idle.ms", 10000);
        properties.put("request.timeout.ms", 5000);

        try (AdminClient client = AdminClient.create(properties)) {
            try {
                Collection<String> groupIds = new ArrayList<>();
                groupIds.add(group);
                DescribeConsumerGroupsResult rs = client.describeConsumerGroups(groupIds);
                return rs.all().get();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public List<String> activeConsumerByTopic(String bootstrapServers, String topicName) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", bootstrapServers);
        properties.put("connections.max.idle.ms", 10000);
        properties.put("request.timeout.ms", 5000);

        List<String> lists = new ArrayList<>();
        try (AdminClient client = AdminClient.create(properties)) {
            try {
                // get all consumer groupId
                List<String> groupIds = client.listConsumerGroups().all().get().stream().map(s -> s.groupId()).collect(Collectors.toList());
                // Here you get all the descriptions for the groups
                Map<String, ConsumerGroupDescription> groups = client.describeConsumerGroups(groupIds).all().get();
                for (final String groupId : groupIds) {
                    ConsumerGroupDescription descr = groups.get(groupId);
                    // find if any description is connected to the topic with topicName
                    Optional<TopicPartition> tp = descr.members().stream().
                            map(s -> s.assignment().topicPartitions()).
                            flatMap(coll -> coll.stream()).
                            filter(s -> s.topic().equals(topicName)).findAny();
                    if (tp.isPresent()) {
                        // you found the consumer, so collect the group id somewhere
                        lists.add(descr.groupId());
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                throw new IllegalStateException(e);
            }
        }
        return lists;
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
