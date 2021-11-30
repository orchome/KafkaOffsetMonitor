package www.orchome.com.kafka.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TreeNodeDTO {

    public TreeNodeDTO(String name) {
        this.name = name;
    }

    public String name;
    public List<TreeNodeDTO> children;

}
