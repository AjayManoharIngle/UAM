package org.rebit.auth.repository;

import org.rebit.auth.entity.SmsTokenGenerationLogs;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author amit.hazare
 * 03-Jun-2021
 */
public interface SmsTokenGenerationLogsRepository extends JpaRepository<SmsTokenGenerationLogs, Integer> {

}