package com.umbookings.repository;

import com.umbookings.dto.request.UserSignUpDTO;
import com.umbookings.dto.response.NotificationDTO;
import com.umbookings.model.Notification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * @author Shrikar Kalagi
 *
 */
@Repository
public interface NotificationRepository  extends JpaRepository<Notification, Long> {

    @Query("select new com.umbookings.dto.response.NotificationDTO(n.subject, n.text, n.isEmail, n.isPush, n.isSms, n.isNotification, n.createdTimestamp) from Notification n order by n.createdTimestamp desc")
    public List<NotificationDTO> getNotifications(Pageable pageable);

}
