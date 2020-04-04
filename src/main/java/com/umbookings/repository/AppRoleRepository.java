package com.umbookings.repository;
/**
 * @author Shrikar Kalagi
 *
 */
import com.umbookings.enums.RoleName;
import com.umbookings.model.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AppRoleRepository extends JpaRepository<AppRole, Long> {

    @Query("select apr from AppRole apr where name = :roleName")
    public Optional<AppRole> findRoleByRoleName(@Param("roleName") RoleName roleName);

}
