package www.orchome.com.kafka.core;

import org.apache.kafka.clients.admin.AdminClient;

import java.util.Properties;

public class KafkaAdminFactory {

    public static AdminClient getInstance() {
        return getInstance(System.getProperty("--broker-list"));
    }

    public static AdminClient getInstance(String bootstrapServers) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", bootstrapServers);
        properties.put("connections.max.idle.ms", 10000);
        properties.put("request.timeout.ms", 5000);
        return AdminClient.create(properties);
    }
}
