package www.orchome.com.kafka.core.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class GroupDTO {
    public GroupDTO(String name) {
        this.name = name;
    }

    private String name;
    private List<OffsetDTO> offsets;
}
