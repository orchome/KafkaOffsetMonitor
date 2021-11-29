package www.orchome.com.kafka.core.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TreeRootDTO {

    public TreeRootDTO(String name) {
        this.name = name;
    }

    public String name;
    public List<TreeNodeDTO> children;

}
