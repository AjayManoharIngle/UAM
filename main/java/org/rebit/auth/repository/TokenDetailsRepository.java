package org.rebit.auth.repository;

import java.util.List;

import org.rebit.auth.entity.TokenDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenDetailsRepository extends JpaRepository<TokenDetails, Long>{
	public List<TokenDetails> findByUserNameAndType(String userName,String type);
	public List<TokenDetails> findByTokenAndType(String token,String type);
}