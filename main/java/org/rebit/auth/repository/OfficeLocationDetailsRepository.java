package org.rebit.auth.repository;

import java.util.List;

import org.rebit.auth.entity.OfficeLocationDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfficeLocationDetailsRepository extends JpaRepository<OfficeLocationDetails, Long>{
	public OfficeLocationDetails findByOfficeLocationIdAndStatus(Long officeLocationId,Integer status);
	public OfficeLocationDetails findByLocationLongNameAndStatus(String locationLongName,Integer status);
	public OfficeLocationDetails findByLocationShortNameAndStatus(String locationShortName,Integer status);
	public List<OfficeLocationDetails> findByStatus(Integer status);
	public OfficeLocationDetails findByLocationLongName(String officeLocationName);
	public OfficeLocationDetails findByOfficeLocationId(long decryptId);
}
