package org.rebit.auth.repository;

import org.rebit.auth.entity.ApplicationProperties;
import org.rebit.auth.entity.AuthManagementProperties;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface AuthManagementPropertiesRepository  extends JpaRepository<AuthManagementProperties, String>, JpaSpecificationExecutor<ApplicationProperties>{

}

