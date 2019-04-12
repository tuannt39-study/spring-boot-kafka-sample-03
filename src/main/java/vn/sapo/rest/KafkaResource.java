package vn.sapo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.sapo.domain.Foo1;

@RestController
@RequestMapping(value = "/api/kafka")
public class KafkaResource {

    @Autowired
    private KafkaTemplate<Object, Object> template;

    @PostMapping(path = "/send")
    public void sendFoo(@RequestParam("message") String message) {
        this.template.executeInTransaction(kafkaTemplate -> {
            StringUtils.commaDelimitedListToSet(message).stream()
                    .map(s -> new Foo1(s))
                    .forEach(foo -> kafkaTemplate.send("topic2", foo));
            return null;
        });
    }

}
