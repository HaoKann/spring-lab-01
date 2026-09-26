package kz.iitu.springlab.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Aspect
@Component
public class SlowMethodAspect {

    private static final Logger log = LoggerFactory.getLogger(SlowMethodAspect.class);

    @Value("${app.aspect.slow-threshold-ms:200}")
    private long thresholdMs;

    private final List<SlowCallInfo> slowCalls = new CopyOnWriteArrayList<>();

    public record SlowCallInfo(String methodName, long executionTimeMs, String timestamp) {}

    @Around("kz.iitu.springlab.aspect.Pointcuts.serviceOperation()")
    public Object logSlowCall(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.currentTimeMillis();
        try {
            return pjp.proceed();
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            if (duration > thresholdMs) {
                String methodName = pjp.getSignature().toShortString();
                SlowCallInfo callInfo = new SlowCallInfo(
                        methodName,
                        duration,
                        LocalDateTime.now().toString()
                );
                slowCalls.add(callInfo);
                log.warn("[VARIANT 2 - SLOW CALL] Method {} exceeded threshold ({} ms > {} ms)",
                        methodName, duration, thresholdMs);
            }
        }
    }

    public List<SlowCallInfo> getSlowCalls() {
        return Collections.unmodifiableList(slowCalls);
    }
}