package kz.iitu.spring_lab_01.notify;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("noop")
@Order(99)
public class NoopNotifier implements Notifier {

    @Override
    public String send(String message) {
        return "noop";
    }

    @Override
    public String channel() {
        return "noop";
    }
}