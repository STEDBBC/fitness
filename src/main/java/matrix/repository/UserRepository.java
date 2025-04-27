package matrix.repository;

import java.util.List;
import model.Role;
import model.Data.UserData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserData, Integer> {
    List<UserData> findByRole(Role role);
}
