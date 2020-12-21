package test.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import test.model.User;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByName(String name);

    User getById(int id);

}
