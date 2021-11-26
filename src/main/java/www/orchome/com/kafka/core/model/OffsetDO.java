package www.orchome.com.kafka.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class OffsetDO {
    private String group;
    private String topic;
    private String partition;
    private Integer offset;
    private Integer logSize;
    private String owner;
    private Date creation;
    private Date modified;
    private Long timestamp;
}
