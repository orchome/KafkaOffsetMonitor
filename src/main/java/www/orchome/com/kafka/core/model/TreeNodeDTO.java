package www.orchome.com.kafka.core.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TreeNodeDTO {
    public String name;
    public String children;

    public TreeNodeDTO(String name) {
        this.name = name;
    }
}
