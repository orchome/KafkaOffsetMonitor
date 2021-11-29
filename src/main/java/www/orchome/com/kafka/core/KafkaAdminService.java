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

public class KafkaAdminService {

    public List<String> listConsumerGroup() {
        try (AdminClient client = KafkaAdminFactory.getInstance()) {
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

    public List<Node> listBrokers() {
        try (AdminClient client = KafkaAdminFactory.getInstance()) {
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

    public Map<TopicPartition, OffsetAndMetadata> listConsumerGroupOffsets(String group) {
        try (AdminClient client = KafkaAdminFactory.getInstance()) {
            try {
                ListConsumerGroupOffsetsResult rs = client.listConsumerGroupOffsets(group);
                return rs.partitionsToOffsetAndMetadata().get(10, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public Map<TopicPartition, ListOffsetsResult.ListOffsetsResultInfo> listOffsets(TopicPartition topicPartition) {
        try (AdminClient client = KafkaAdminFactory.getInstance()) {
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

    public Set<String> listTopics() {
        try (AdminClient client = KafkaAdminFactory.getInstance()) {
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

    public Map<String, ConsumerGroupDescription> describeConsumer(String groupName) {
        try (AdminClient client = KafkaAdminFactory.getInstance()) {
            try {
                Collection<String> groupIds = new ArrayList<>();
                groupIds.add(groupName);
                DescribeConsumerGroupsResult rs = client.describeConsumerGroups(groupIds);
                return rs.all().get();
            } catch (Exception e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public List<String> activeConsumerByTopic(String topicName) {
        List<String> lists = new ArrayList<>();
        try (AdminClient client = KafkaAdminFactory.getInstance()) {
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

    public List<String> inactiveConsumerByTopic(String topicName) {
        List<String> lists = new ArrayList<>();
        try (AdminClient client = KafkaAdminFactory.getInstance()) {
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
                    if (!tp.isPresent()) {
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
}
