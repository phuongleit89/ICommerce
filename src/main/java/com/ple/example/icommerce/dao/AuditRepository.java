package com.ple.example.icommerce.dao;

import com.ple.example.icommerce.dao.custom.BaseRepository;
import com.ple.example.icommerce.entity.Audit;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditRepository extends BaseRepository<Audit, Long> {
}
