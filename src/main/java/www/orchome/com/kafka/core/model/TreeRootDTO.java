package www.orchome.com.kafka.core.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TreeRootDTO {
    public String name;
    public List<TreeNodeDTO> children;

    public TreeRootDTO(String name) {
        this.name = name;
    }
}
