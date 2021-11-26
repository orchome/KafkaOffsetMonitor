package www.orchome.com.kafka.core.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class TopicDO {

    public TopicDO(String name) {
        this.name = name;
    }

    private String name;
    private String group;
    private List<BrokerDO> brokers;
    private List<OffsetDO> offsets;
    private List<TopicDO> consumers;
    private List<TopicDO> children;
    private String topic;
    private String sumLogEndOffset;

    private List<TopicDO> active;
}
