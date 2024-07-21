package org.rebit.auth.repository;

import org.rebit.auth.entity.SmsToken;
import org.springframework.data.jpa.repository.JpaRepository;


public interface SmsTokenRepository extends JpaRepository<SmsToken, String> {

}