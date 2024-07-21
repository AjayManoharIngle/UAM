package org.rebit.auth.repository;

import org.rebit.auth.entity.RegionalOfficeDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RegionalOfficeDetailsRepository extends JpaRepository<RegionalOfficeDetails, String>{
	RegionalOfficeDetails findByRoName(String roName);
}
