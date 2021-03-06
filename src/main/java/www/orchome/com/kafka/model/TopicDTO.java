package www.orchome.com.kafka.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TopicDTO {
    private String name;
    private List<GroupDTO> consumers;
}
