package www.orchome.com.kafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        Map<String, String> globalMap =
                Arrays.asList(args).stream()
                        .collect(Collectors.toMap(
                                arg -> arg.split("=")[0],
                                arg -> arg.split("=")[1])
                        );
        globalMap.forEach((k, v) -> System.setProperty(k, v));

        SpringApplication.run(Application.class, args);
    }
}
