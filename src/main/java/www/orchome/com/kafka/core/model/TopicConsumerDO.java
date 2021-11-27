package www.orchome.com.kafka.core.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class TopicConsumerDO {
    public TopicConsumerDO(String name) {
        this.name = name;
    }

    private String name;
    private TopicConsumerDO consumers;
    private List<GroupDTO> active;
    private List<OffsetDO> offsets;
    private String topic;
    private String group;
}
