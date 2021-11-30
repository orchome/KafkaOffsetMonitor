package www.orchome.com.kafka.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class TopicConsumerDO {
    private String name;
    private TopicConsumerDO consumers;
    private List<GroupDTO> active;
    private List<GroupDTO> inactive;
    private String topic;
    private String group;
}
