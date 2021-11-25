package www.orchome.com.kafka;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PartitionsDO {
    private String partition;
    private String logEndOffset;
}
