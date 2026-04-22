package spring_lab3_notifications.org.example.repository;

import spring_lab3_notifications.org.example.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}