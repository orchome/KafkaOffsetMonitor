package www.orchome.com.kafka.core;

import org.apache.kafka.clients.admin.ConsumerGroupDescription;
import org.apache.kafka.clients.admin.ListOffsetsResult;
import org.apache.kafka.clients.admin.MemberDescription;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicPartition;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import www.orchome.com.kafka.core.model.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class KafkaTest {

    public KafkaAdminService service = new KafkaServiceFactory().getInstance();

    @GetMapping("group")
    public List<String> groups() {
        return service.listConsumerGroup();
    }

    @GetMapping("group/{groupName}")
    public TopicDO groupsByName(@PathVariable String groupName) {
        TopicDO main = new TopicDO();
        // brokers
        main.setBroders(service.listBrokers());
        // offsets
        main.setOffsets(getOffset(groupName, null));
        return main;
    }

    public List<OffsetDTO> getOffset(String groupName, String topicName) {

        Map<String, MemberDescription> topicPartitionMaps = new HashMap<>();
        Map<String, ConsumerGroupDescription> consumerDesc = service.describeConsumer(groupName);
        consumerDesc.get(groupName).members().forEach(m -> {
            m.assignment().topicPartitions().forEach(k -> {
                topicPartitionMaps.put(k.toString(), m);
            });
        });

        List<OffsetDTO> offsets = new ArrayList<>();
        Map<TopicPartition, OffsetAndMetadata> maps = service.listConsumerGroupOffsets(groupName);
        maps.forEach((key, value) -> {
            if (topicName != null && !key.topic().equals(topicName)) return;
            Map<TopicPartition, ListOffsetsResult.ListOffsetsResultInfo> partitionListOffsetsResultInfoMap = service.listOffsets(key);
            String consumerId = "", clientId = "", host = "";
            if (topicPartitionMaps.get(key.topic() + "-" + key.partition()) != null) {
                consumerId = topicPartitionMaps.get(key.topic() + "-" + key.partition()).consumerId();
                clientId = topicPartitionMaps.get(key.topic() + "-" + key.partition()).clientId();
                host = topicPartitionMaps.get(key.topic() + "-" + key.partition()).host();
            }
            offsets.add(new OffsetDTO(groupName, key.topic(), key.partition(), value.offset(), partitionListOffsetsResultInfoMap.get(key).offset(), consumerId, host, clientId));
        });
        return offsets;
    }

    @GetMapping("group/{groupName}/{topicName}")
    public TopicConsumerDO groupsByName(@PathVariable String groupName, @PathVariable String topicName) { // todo
        TopicConsumerDO main = new TopicConsumerDO();
        main.setTopic("topic");
        main.setGroup("myself");
        List<GroupDTO> active = new ArrayList<>();
        active.add(new GroupDTO("c2"));

        List<OffsetDO> offsets = new ArrayList<>();
        for (int i = 0; i <= 500; i++) {
            offsets.add(new OffsetDO(null, null, "0", i, 1200 + i, null, null, null, t((1000 - i)).getTime()));
            offsets.add(new OffsetDO(null, null, "1", i, 1 + i, null, null, null, t((1000 - i)).getTime()));
        }
        main.setOffsets(offsets);
        main.setActive(active);
        return main;
    }

    public Date t(int second) {
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.SECOND, -second);
        date = c.getTime();
        return date;
    }

    /**
     * 1. 活跃的消费者（某个主题的）
     * 2. 消费者明细
     */
    @GetMapping("topic/{topicName}/consumer")
    public TopicConsumerDO topic(@PathVariable String topicName) {
        TopicConsumerDO main = new TopicConsumerDO();
        TopicConsumerDO consumers = new TopicConsumerDO();

        List<GroupDTO> active = new ArrayList<>();
        List<GroupDTO> inactive = new ArrayList<>();

        // 活跃的
        List<GroupDTO> actionG = service.activeConsumerByTopic(topicName).stream().map(g -> new GroupDTO(g)).collect(Collectors.toList());
        actionG.forEach(c -> {
            c.setOffsets(getOffset(c.getName(), topicName));
            active.add(c);
        });

        // 不活跃
        List<GroupDTO> inactionG = service.inactiveConsumerByTopic(topicName).stream().map(g -> new GroupDTO(g)).collect(Collectors.toList());
        inactionG.forEach(c -> {
            c.setOffsets(getOffset(c.getName(), topicName));
            inactive.add(c);
        });

        consumers.setActive(active);
        consumers.setInactive(inactive);
        main.setConsumers(consumers);
        return main;
    }

    @GetMapping("topiclist")
    public Set<String> topiclist() {
        return service.listTopics();
    }

    @GetMapping("topicdetails/{topicName}")
    public TopicDTO topicDetails(@PathVariable String topicName) {
        TopicDTO main = new TopicDTO();
        List<GroupDTO> groups = service.activeConsumerByTopic(topicName).stream().map(g -> new GroupDTO(g)).collect(Collectors.toList());
        main.setConsumers(groups);
        return main;
    }

    @GetMapping("activetopics")
    public TreeRootDTO activetopics() {
        TreeRootDTO main = new TreeRootDTO("ActiveTopics");
        List<TreeNodeDTO> topicList = new ArrayList<>();
        service.listTopics().forEach(t -> {
            TreeNodeDTO topicDO = new TreeNodeDTO(t);
            topicDO.setChildren(service.activeConsumerByTopic(t).stream().map(c -> new TreeNodeDTO(c)).collect(Collectors.toList()));
            topicList.add(topicDO);
        });
        main.setChildren(topicList);
        return main;
    }

    @GetMapping("clusterlist")
    public TreeRootDTO clusterlist() {

        List<Node> nodes = service.listBrokers();

        // convert to a tree structure
        TreeRootDTO root = new TreeRootDTO("KafkaCluster");
        List<TreeNodeDTO> treeNode = new ArrayList<>();
        nodes.forEach(node -> treeNode.add(new TreeNodeDTO(node.host() + ":" + node.port())));
        root.setChildren(treeNode);
        return root;
    }
}
