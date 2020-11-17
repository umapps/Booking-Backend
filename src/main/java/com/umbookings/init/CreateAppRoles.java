package com.umbookings.init;
/**
 * @author Shrikar Kalagi
 *
 */
import com.umbookings.enums.RoleName;
import com.umbookings.model.AppRole;
import com.umbookings.repository.AppRoleRepository;
import com.umbookings.security.JwtAuthenticationEntryPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CreateAppRoles implements ApplicationRunner {

    @Autowired
    AppRoleRepository appRoleRepository;

    private static final Logger logger = LoggerFactory.getLogger(CreateAppRoles.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {

        // Add here if new roles are added. This is needed to insert in AppRole Table.
       List<AppRole> appRoles =  appRoleRepository.findAll();
       if(!appRoles.stream().filter(o -> o.getName().equals(RoleName.ROLE_NORMAL_USER)).findFirst().isPresent())
       {
           AppRole normalUser = new AppRole();
           normalUser.setName(RoleName.ROLE_NORMAL_USER);
           normalUser.setDescription("Normal user");
           appRoleRepository.save(normalUser);
       }

       if (!appRoles.stream().filter(o -> o.getName().equals(RoleName.ROLE_ADMIN_USER)).findFirst().isPresent())
       {
           AppRole adminUser = new AppRole();
           adminUser.setName(RoleName.ROLE_ADMIN_USER);
           adminUser.setDescription("Admin user");
           appRoleRepository.save(adminUser);
       }
        logger.info("server started successfully");
    }
}
