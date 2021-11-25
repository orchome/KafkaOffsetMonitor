package www.orchome.com.kafka;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class KafkaTest {

    @GetMapping("group")
    public List<String> group() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        return list;
    }

    @GetMapping("topiclist")
    public List<TopicDO> topiclist() {
        List<TopicDO> list = new ArrayList<>();
        TopicDO topicDO = new TopicDO();
        topicDO.setTopic("t1");
        topicDO.setSumLogEndOffset("000001");
        PartitionsDO partitionsDO = new PartitionsDO();
        partitionsDO.setPartition("0");
        partitionsDO.setLogEndOffset("1");
        List<PartitionsDO> partitions = new ArrayList<>();
        partitions.add(partitionsDO);
        topicDO.setPartitions(partitions);
        list.add(topicDO);
        return list;
    }
}
