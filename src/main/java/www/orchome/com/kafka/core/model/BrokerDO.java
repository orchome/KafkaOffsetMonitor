package www.orchome.com.kafka.core.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class BrokerDO {
    private String id;
    private String host;
    private String port;
}
