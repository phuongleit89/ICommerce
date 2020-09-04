package com.ple.example.icommerce.service.impl;

import com.ple.example.icommerce.dao.AuditRepository;
import com.ple.example.icommerce.entity.Audit;
import com.ple.example.icommerce.service.AuditService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditServiceImpl implements AuditService {

    private final AuditRepository auditRepository;

    public AuditServiceImpl(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @Override
    @Transactional(
            propagation = Propagation.REQUIRES_NEW,
            isolation = Isolation.SERIALIZABLE,
            rollbackFor = Exception.class)
    public Audit create(Audit audit) {
        return auditRepository.save(audit);
    }

}
