package org.rebit.auth.repository;

import java.util.List;

import org.rebit.auth.entity.ApiMasterDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApiMasterDetailsRepository extends JpaRepository<ApiMasterDetails, Long>{ 
public ApiMasterDetails findByUriAndMethod(String uri,String method);
public List<ApiMasterDetails> findByMethod(String method);
}
