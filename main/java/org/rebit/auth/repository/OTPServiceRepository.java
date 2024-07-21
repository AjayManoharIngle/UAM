package org.rebit.auth.repository;

import org.rebit.auth.entity.OTPLogsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OTPServiceRepository extends JpaRepository<OTPLogsEntity, Long>{
	OTPLogsEntity findByOtpValueAndKey(String otp, String key);	
}
