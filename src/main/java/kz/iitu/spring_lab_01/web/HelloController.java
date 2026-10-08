package kz.iitu.spring_lab_01.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
public class HelloController {

    @Value("${app.owner:unknown}")
    private String owner;

    @GetMapping("/api/hello")
    public String hello() {
        return "Hello, Spring Boot!";
    }

    @GetMapping("/api/info")
    public Map<String, Object> info(@RequestParam(value = "name", defaultValue = "Student") String name) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("greeting", "Welcome, " + name + "!");
        response.put("owner", owner);
        response.put("serverTime", LocalDateTime.now().toString());
        return response;
    }
    // Индивидуальное задание (Вариант 1)
    // Метод теперь находится внутри класса
    @GetMapping("/api/calc")
    public Map<String, Object> calculate(
            @RequestParam(value = "a", defaultValue = "0") double a,
            @RequestParam(value = "b", defaultValue = "0") double b,
            @RequestParam(value = "op", defaultValue = "add") String op) {

        // Обновленный современный switch для Java 21
        double result = switch (op.toLowerCase()) {
            case "sub" -> a - b;
            case "mul" -> a * b;
            case "div" -> (b != 0) ? (a / b) : Double.NaN;
            default -> a + b; // "add" и любые другие значения
        };

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("operation", op);
        response.put("a", a);
        response.put("b", b);
        response.put("result", Double.isNaN(result) ? "Division by zero error" : result);
        return response;
    }
}