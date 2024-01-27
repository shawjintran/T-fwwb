import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootTest(classes = TestApplication.class)
public class TestApplication {
  @Autowired
  KafkaTemplate<String,Object> kafkaTemplate;
  @Test
  void produce(){

  }
}
