package ru.jawaprog.test_task.apects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import ru.jawaprog.test_task.exceptions.DataBaseException;
import ru.jawaprog.test_task.exceptions.ForeignKeyException;


@Aspect
@Component
public class DataBaseExceptionAspect {
    @Pointcut("execution(public * ru.jawaprog.test_task.repositories.dao.*.*(..))")
    public void callAtDataBaseAccess() {
    }

    @Around("callAtDataBaseAccess()")
    public Object whenTrowingCallAt(ProceedingJoinPoint call) throws Throwable {
        try {
            return call.proceed();
        } catch (DataIntegrityViolationException ex) {
            if (ex.getCause().getMessage().contains("внешнего ключа")) {
                String str = call.getSignature().toShortString();
                if (str.startsWith("Contracts")) {
                    throw new ForeignKeyException("Клиент");
                } else if (str.startsWith("Accounts")) {
                    throw new ForeignKeyException("Контракт");
                } else {
                    throw new ForeignKeyException("Счёт");
                }
            } else {
                ex.printStackTrace();
                throw new DataBaseException("Ошибка обращения к базе данных");
            }
        }
    }
}
