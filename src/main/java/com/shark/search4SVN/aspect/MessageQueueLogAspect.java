package com.shark.search4SVN.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Created by qinghualiu on 2017/8/26.
 */
@Aspect
@Component
public class MessageQueueLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(MessageQueueLogAspect.class);

    @Before("execution(* com.shark.search4SVN.service.disruptor.DisruptorScheduleService.produceEvent(..))")
    public void produceEvent(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        StringBuilder sb = new StringBuilder();
        for (Object arg : joinPoint.getArgs()) {
            if (arg != null) {
                sb.append("arg:" + arg.toString() + "|");
            }
        }
        logger.info(methodName + "before method:" + sb.toString());
    }

    @Before("execution(* com.shark.search4SVN.service.disruptor.*Worker.onEvent(..))")
    public void consumeEvent(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        StringBuilder sb = new StringBuilder();
        for (Object arg : joinPoint.getArgs()) {
            if (arg != null) {
                sb.append("arg:" + arg.toString() + "|");
            }
        }
        logger.info(methodName + "before method:" + sb.toString());
    }
}
