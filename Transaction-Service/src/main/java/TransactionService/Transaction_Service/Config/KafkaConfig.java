package TransactionService.Transaction_Service.Config;


import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic transactionCreatedTopic(@Value("${tx.kafka.topic.created}") String topic){
        // partitions/replication can be tuned in real env
        return new NewTopic(topic, 3, (short)1);
    }
}


