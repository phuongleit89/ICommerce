package com.ple.example.icommerce.service.impl;

import com.ple.example.icommerce.dao.AuditRepository;
import com.ple.example.icommerce.entity.Audit;
import com.ple.example.icommerce.service.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;

    public AuditServiceImpl(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @Override
    public Audit create(Audit audit) {
        log.debug("Create audit ...");
        return auditRepository.save(audit);
    }

}
