package TransactionService.Transaction_Service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@EnableDiscoveryClient
@SpringBootApplication
public class TransactionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionServiceApplication.class, args);
	}

}


//kafka operations


// starting kafka
// docker compose up -d running docker files
// docker ps  check kafka running
// docker exec -it transaction-service-kafka-1 /bin/bash

// /opt/bitnami/kafka/bin/kafka-topics.sh --create \         // to create a topic
//  --topic test-topic \
//  --bootstrap-server localhost:9092 \
//  --partitions 1 \
//  --replication-factor 1

// /opt/bitnami/kafka/bin/kafka-console-producer.sh \     // create a message
//  --broker-list localhost:9092 \
//  --topic test-topic

// /opt/bitnami/kafka/bin/kafka-console-consumer.sh \      // consume a message
//  --bootstrap-server localhost:9092 \
//  --topic test-topic \
//  --from-beginning

// /opt/bitnami/kafka/bin/kafka-server-stop.sh      stop kafka