package www.orchome.com.kafka.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.common.Node;

@Getter
@Setter
public class NodeDTO {
    private int id;
    private String host;
    private int port;

    public NodeDTO(Node node) {
        this(node.id(), node.host(), node.port());
    }

    public NodeDTO(int id, String host, int port) {
        this.id = id;
        this.host = host;
        this.port = port;
    }
}
