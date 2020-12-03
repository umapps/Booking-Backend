package com.umbookings.repository;
import java.util.List;
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

	@Query("select new com.umbookings.dto.request.UserSignUpDTO(u.id, u.firstName, u.lastName, u.emailId, u.password, u.mobileNumber, u.countryCode, u.deviceToken) from User u   where u.mobileNumber=:userId or u.emailId=:userId")
	public Optional<UserSignUpDTO> findUserDTOByUserId(@Param("userId") String userId);

	@Query("select new com.umbookings.dto.request.UserSignUpDTO(u.id, u.firstName, u.lastName, u.emailId, u.password, u.mobileNumber, u.countryCode, u.deviceToken) from User u where u.id=:id")
	public Optional<UserSignUpDTO> findUserDTOById(@Param("id") Long id);

	@Query("select ar from AppRole ar where ar.id in ( select ur.roleId from UserRole ur where ur.user.id = :id)")
	public Set<AppRole> findRolesById(@Param("id") Long id);

	@Query(value = "select case when exists (select 1 from um_user u where u.email_identifier=:emailId) then 'true' else 'false' end from dual", nativeQuery = true)
	public boolean isExistsUserByEmail(String emailId);

	@Query(value = "select case when exists (select 1 from um_user u where u.mobile_number=:mobileNumber) then 'true' else 'false' end from dual", nativeQuery = true)
	Boolean isExistsUserByMobile(String mobileNumber);

	@Query(value = "select CONCAT(u.country_code,u.mobile_number) as mobile_number FROM um_user u where 1=1", nativeQuery = true)
	List<String> getAllMobileNbrs();

	@Query(value = "select u.email_identifier from um_user u where 1=1", nativeQuery = true)
	List<String> getAllEmailId();

	@Query(value = "select u.device_token from um_user u where 1=1", nativeQuery = true)
	List<String> getAllDeviceToken();

}
