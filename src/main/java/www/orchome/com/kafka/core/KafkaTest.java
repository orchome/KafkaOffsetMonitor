package www.orchome.com.kafka.core;

import org.apache.kafka.clients.admin.ConsumerGroupDescription;
import org.apache.kafka.clients.admin.ListOffsetsResult;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.TopicPartition;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import www.orchome.com.kafka.core.model.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@RestController
public class KafkaTest {

    @GetMapping("group")
    public List<String> groups() {
        return new KafkaAdminTest().listConsumerGroup("172.30.114.30:9092");
    }

    @GetMapping("group/{name}")
    public TopicDO groupsByName(@PathVariable String name) {
        TopicDO main = new TopicDO();

        // brokers
        List<Node> nodes = new KafkaAdminTest().listBroders("172.30.114.30:9092");
        main.setBroders(nodes);

        List<OffsetDTO> offsets = new ArrayList<>();
        Map<TopicPartition, OffsetAndMetadata> maps = new KafkaAdminTest().listConsumerGroupOffsets("172.30.114.30:9092", name);
        Map<String, ConsumerGroupDescription> consumerDesc = new KafkaAdminTest().describeConsumer("172.30.114.30:9092", name);
        AtomicReference<String> clientID = new AtomicReference<>("");
        consumerDesc.get(name).members().forEach(m -> {
            clientID.set(m.clientId() + "_" + m.host());
        });
        maps.forEach((key, value) -> {
            Map<TopicPartition, ListOffsetsResult.ListOffsetsResultInfo> partitionListOffsetsResultInfoMap = new KafkaAdminTest().test("172.30.114.30:9092", key);
            offsets.add(new OffsetDTO(name, key.topic(), key.partition(), value.offset(), partitionListOffsetsResultInfoMap.get(key).offset(), clientID.get(), null, null, null));
        });
        main.setOffsets(offsets);
        return main;
    }

    public List<OffsetDTO> getOffset(String groupName, String topicName) {
        List<OffsetDTO> offsets = new ArrayList<>();
        Map<TopicPartition, OffsetAndMetadata> maps = new KafkaAdminTest().listConsumerGroupOffsets("172.30.114.30:9092", groupName);
        maps.forEach((key, value) -> {
            if (!key.topic().equals(topicName)) return;
            Map<TopicPartition, ListOffsetsResult.ListOffsetsResultInfo> partitionListOffsetsResultInfoMap = new KafkaAdminTest().test("172.30.114.30:9092", key);
            offsets.add(new OffsetDTO(groupName, key.topic(), key.partition(), value.offset(), partitionListOffsetsResultInfoMap.get(key).offset(), "", null, null, null));
        });
        return offsets;
    }

    @GetMapping("group/{name}/{topicName}")
    public TopicConsumerDO groupsByName(@PathVariable String name, @PathVariable String topicName) { // todo
        TopicConsumerDO main = new TopicConsumerDO();
        main.setTopic("topic");
        main.setGroup("myself");
        List<GroupDTO> active = new ArrayList<>();
        active.add(new GroupDTO("c2"));

        List<OffsetDO> offsets = new ArrayList<>();
        for (int i = 0; i <= 1000; i++) {
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
     *
     * @param topicName
     * @return
     */
    @GetMapping("topic/{topicName}/consumer")
    public TopicConsumerDO topic(@PathVariable String topicName) {
        TopicConsumerDO main = new TopicConsumerDO();
        List<GroupDTO> active = new ArrayList<>();
        TopicConsumerDO consumers = new TopicConsumerDO();

        // topic下的活跃的group
        List<String> _groups = new KafkaAdminTest().activeConsumerByTopic("172.30.114.30:9092", topicName);
        List<GroupDTO> groups = _groups.stream().map(g -> new GroupDTO(g)).collect(Collectors.toList());

        groups.forEach(c -> {
                    c.setOffsets(getOffset(c.getName(), topicName));
                    active.add(c);
                }
        );
        consumers.setActive(active);
        main.setConsumers(consumers);
        return main;
    }

    @GetMapping("topiclist")
    public Set<String> topiclist() {
        return new KafkaAdminTest().listTopics("172.30.114.30:9092");
    }

    @GetMapping("topicdetails/{topicName}")
    public TopicDTO topicdetails(@PathVariable String topicName) {
        TopicDTO main = new TopicDTO();
        List<String> _groups = new KafkaAdminTest().activeConsumerByTopic("172.30.114.30:9092", topicName);
        List<GroupDTO> groups = _groups.stream().map(g -> new GroupDTO(g)).collect(Collectors.toList());
        main.setConsumers(groups);
        return main;
    }

    @GetMapping("activetopics")
    public TopicDO activetopics() {
        TopicDO main = new TopicDO();
        main.setName("ActiveTopics");

        TopicDO t1 = new TopicDO();
        t1.setName("t1");

        TopicDO t2 = new TopicDO();
        t2.setName("t2");

        TopicDO c1 = new TopicDO();
        c1.setName("c1");

        TopicDO c2 = new TopicDO();
        c2.setName("c2");

        List<TopicDO> list = new ArrayList<>();
        list.add(t1);
        list.add(t2);

        List<TopicDO> lists = new ArrayList<>();
        lists.add(c1);
        lists.add(c2);
        t1.setChildren(lists);
        t2.setChildren(lists);

        main.setChildren(list);
        return main;
    }

    @GetMapping("clusterlist")
    public TreeRootDTO clusterlist() {
        TreeRootDTO root = new TreeRootDTO("KafkaCluster");
        List<TreeNodeDTO> list = new ArrayList<>();
        List<Node> nodes = new KafkaAdminTest().listBroders("172.30.114.30:9092");
        nodes.forEach(node -> list.add(new TreeNodeDTO(node.host() + ":" + node.port())));
        root.setChildren(list);
        return root;
    }
}
