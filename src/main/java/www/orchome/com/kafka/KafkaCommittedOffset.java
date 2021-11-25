package www.orchome.com.kafka;

import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

public class KafkaCommittedOffset {

    @GetMapping("hello")
    public List<String> index() {
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        return list;
    }
}
