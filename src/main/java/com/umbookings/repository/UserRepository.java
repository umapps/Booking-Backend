package com.umbookings.repository;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.umbookings.dto.request.UserSignUpDTO;
import com.umbookings.model.User;
import com.umbookings.model.AppRole;
/**
 * @author Shrikar Kalagi
 *
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("select new com.umbookings.dto.request.UserSignUpDTO(u.id, u.firstName, u.lastName, u.emailId, u.password, u.mobileNumber) from User u   where u.mobileNumber=:emailId or u.emailId=:emailId")
	public Optional<UserSignUpDTO> findUserDTOByUserId(@Param("emailId") String emailId);
	
	@Query("select new com.umbookings.dto.request.UserSignUpDTO(u.id, u.firstName, u.lastName, u.emailId, u.password, u.mobileNumber) from User u where u.id=:id")
	public Optional<UserSignUpDTO> findUserDTOById(@Param("id") Long id);
	
	@Query("select ar from AppRole ar where ar.id in ( select ur.roleId from UserRole ur where ur.user.id = :id)")
	public Set<AppRole> findRolesById(@Param("id") Long id);

	@Query(value = "select exists (select 1 from um_user u where u.email_identifier=:emailId)", nativeQuery = true)
	public boolean findByEmailId(String emailId);

	@Query(value = "select exists (select 1 from um_user u where u.mobile_number=:mobileNumber)", nativeQuery = true)
	Boolean findByMobile(String mobileNumber);
}
