package www.orchome.com.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

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
