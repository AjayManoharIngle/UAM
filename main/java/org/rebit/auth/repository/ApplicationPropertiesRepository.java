package org.rebit.auth.repository;

import org.rebit.auth.entity.ApplicationProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ApplicationPropertiesRepository  extends JpaRepository<ApplicationProperties, String>, JpaSpecificationExecutor<ApplicationProperties>{

}

