package www.orchome.com.kafka.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
public class OffsetDTO {
    private String group;
    private String topic;
    private int partition;
    private Long offset;
    private Long logSize;
    private String owner;
    private String ip;
    private String clientId;
//    private Date creation;
//    private Date modified;
//    private Long timestamp;
}
