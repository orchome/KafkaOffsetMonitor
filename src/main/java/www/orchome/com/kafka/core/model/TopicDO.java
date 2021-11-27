package www.orchome.com.kafka.core.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.kafka.common.Node;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
public class TopicDO {

    public TopicDO(String name) {
        this.name = name;
    }

    private String name;
    private String group;
    private List<NodeDTO> brokers;
    private List<OffsetDTO> offsets;
    private List<TopicDO> consumers;
    private List<TopicDO> children;
    private String topic;
    private String sumLogEndOffset;

    private List<TopicDO> active;

    public void setBroders(List<Node> nodes) {
        this.brokers = nodes.stream().map(node -> new NodeDTO(node)).collect(Collectors.toList());
    }
}
