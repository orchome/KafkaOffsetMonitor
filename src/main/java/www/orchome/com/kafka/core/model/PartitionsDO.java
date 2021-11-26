package www.orchome.com.kafka.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
public class PartitionsDO {
    private String partition;
    private String offset;
    private String logSize;
    private String owner;
    private Date creation;
    private Date modified;
}
