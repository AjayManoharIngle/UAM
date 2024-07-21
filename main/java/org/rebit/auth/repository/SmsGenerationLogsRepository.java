package org.rebit.auth.repository;

import org.rebit.auth.entity.SmsGenerationLogs;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author amit.hazare
 * 03-Jun-2021
 */
public interface SmsGenerationLogsRepository extends JpaRepository<SmsGenerationLogs, Integer> {

}
