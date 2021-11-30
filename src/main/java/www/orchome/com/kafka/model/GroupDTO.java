package www.orchome.com.kafka.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.common.Node;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class GroupDTO {

    public GroupDTO() {
    }

    public GroupDTO(String name) {
        this.name = name;
    }

    private String name;
    private List<OffsetDTO> offsets;
    private List<NodeDTO> brokers;
    public void setBrokers(List<Node> nodes) {
        this.brokers = nodes.stream().map(node -> new NodeDTO(node)).collect(Collectors.toList());
    }
}
