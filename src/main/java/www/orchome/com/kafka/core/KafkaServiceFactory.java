package www.orchome.com.kafka.core;

public class KafkaServiceFactory {

    public static KafkaAdminService getInstance() {
        return new KafkaAdminService();
    }

}
