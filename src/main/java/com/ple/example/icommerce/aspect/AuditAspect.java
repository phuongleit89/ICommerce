package com.ple.example.icommerce.aspect;

import com.ple.example.icommerce.annotation.AuditField;
import com.ple.example.icommerce.annotation.Auditable;
import com.ple.example.icommerce.entity.Audit;
import com.ple.example.icommerce.entity.AuditActionType;
import com.ple.example.icommerce.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
@Slf4j
public class AuditAspect {

    private final AuditService auditService;

    public AuditAspect(AuditService auditService) {
        this.auditService = auditService;
    }


    @After(value = "@annotation(com.ple.example.icommerce.annotation.Auditable)", argNames = "joinPoint")
    public void audit(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Signature signature = joinPoint.getSignature();
        log.info("Called {} with arguments {}", signature.getName(), args);

        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        Auditable auditMethod = method.getAnnotation(Auditable.class);
        AuditActionType actionType = auditMethod.action();

        Parameter[] parameters = method.getParameters();

        List<String> descFields = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            Parameter parameter = parameters[i];
            if (!parameter.isAnnotationPresent(AuditField.class)) {
                log.debug("Ignore Audit Field: {}", parameter.getName());
                continue;
            }

            AuditField auditField = parameter.getAnnotation(AuditField.class);
            String fieldName = auditField.name();
            String desc = fieldName.concat("=").concat(String.valueOf(args[i]));
            descFields.add(desc);
        }

        String description = StringUtils.join(descFields, ",");
        log.debug("Called {} with input description: {}", signature.getName(), description);

        Audit audit = Audit.builder()
                .actionType(actionType)
                .inputDescription(description).build();
        auditService.create(audit);
    }

    @Around("execution(* com.ple.example.icommerce.service.AuditService.create(..))")
    public void auditAround(final ProceedingJoinPoint joinPoint) {
        log.debug("Before invoking audit create() method");
        try {
            joinPoint.proceed();
        } catch (Throwable ex) {
            log.error("Audit Information Failed", ex);
        }
    }

}
