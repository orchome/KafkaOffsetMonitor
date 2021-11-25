package www.orchome.com.kafka.core;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class TopicDO {
    private String topic;
    private String sumLogEndOffset;
    private List<PartitionsDO> partitions;
}
