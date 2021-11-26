package www.orchome.com.kafka.core;

import com.sun.scenario.effect.Offset;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import www.orchome.com.kafka.core.model.*;

import java.util.*;

@RestController
public class KafkaTest {

    @GetMapping("group")
    public List<String> groups() {
        return new KafkaAdminTest().listConsumerGroup("172.30.114.30:9092");
    }

    @GetMapping("group/{name}")
    public TopicDO groupsByName(@PathVariable String name) {
        TopicDO main = new TopicDO();
        List<BrokerDO> brokerDOS = new ArrayList<>();
        brokerDOS.add(new BrokerDO("1", "172.0.0.1", "9092"));
        brokerDOS.add(new BrokerDO("2", "172.0.0.2", "9092"));
        main.setBrokers(brokerDOS);

        List<PartitionsDO> partitions = new ArrayList<>();
//        partitions.add(new PartitionsDO("0", "0", "10", "w1", "just do", "last day"));
//        partitions.add(new PartitionsDO("1", "1", "10", "w2", "just do", "last day"));

        List<OffsetDO> offsets = new ArrayList<>();
        offsets.add(new OffsetDO("myself", "t1", "0", 0, 1, "owner", new Date(), new Date(), null));
        offsets.add(new OffsetDO("myself", "t1", "1", 0, 1, "owner", new Date(), new Date(), null));
        offsets.add(new OffsetDO("myself", "t2", "2", 1, 1, "owner", new Date(), new Date(), null));
        offsets.add(new OffsetDO("myself", "t2", "3", 1, 1, "owner", new Date(), new Date(), null));
        main.setOffsets(offsets);
        return main;
    }

    @GetMapping("group/{name}/{topicName}")
    public TopicConsumerDO groupsByName(@PathVariable String name, @PathVariable String topicName) {
        TopicConsumerDO main = new TopicConsumerDO();
        main.setTopic("topic");
        main.setGroup("myself");
        List<TopicConsumerDO> active = new ArrayList<>();
        active.add(new TopicConsumerDO("c2"));

        TopicConsumerDO c1 = new TopicConsumerDO("myself");
        List<OffsetDO> offsets = new ArrayList<>();
        for(int i=0;i<=1000;i++){
            offsets.add(new OffsetDO(null, null, "0", i, 1200+i, null, null, null, t((1000-i)).getTime()));
            offsets.add(new OffsetDO(null, null, "1", i, 1+i, null, null, null, t((1000-i)).getTime()));
        }
        main.setOffsets(offsets);
        main.setActive(active);
        return main;
    }

    public Date t(int second){
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.SECOND, -second);
        date = c.getTime();
        return date;
    }

    @GetMapping("topic/{name}/consumer")
    public TopicConsumerDO topic() {
        TopicConsumerDO main = new TopicConsumerDO();
        TopicConsumerDO consumers = new TopicConsumerDO();
        List<TopicConsumerDO> active = new ArrayList<>();
        List<OffsetDO> offsets = new ArrayList<>();
        offsets.add(new OffsetDO("myself", "t1", "0", 0, 1, "owner", new Date(), new Date(), null));
        offsets.add(new OffsetDO("myself", "t1", "1", 0, 1, "owner", new Date(), new Date(), null));

        TopicConsumerDO c1 = new TopicConsumerDO("c1");
        c1.setOffsets(offsets);

        active.add(c1);
        active.add(new TopicConsumerDO("c2"));
        consumers.setActive(active);
        main.setConsumers(consumers);
        return main;
    }

    @GetMapping("topiclist")
    public Set<String> topiclist() {
        return new KafkaAdminTest().listTopics("172.30.114.30:9092");
    }

    @GetMapping("topicdetails/{topicName}")
    public TopicDO topicdetails(@PathVariable String topicName) {
        TopicDO main = new TopicDO();
        List<TopicDO> c1 = new ArrayList<>();
        c1.add(new TopicDO("myself"));
        c1.add(new TopicDO("c1"));
        main.setConsumers(c1);
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
    public TopicDO clusterlist() {
        TopicDO main = new TopicDO();
        main.setName("KafkaCluster");
        List<TopicDO> list = new ArrayList<>();
        list.add(new TopicDO("172.30.114.67:9092"));
        list.add(new TopicDO("172.30.114.68:9092"));
        main.setChildren(list);
        return main;
    }
}
