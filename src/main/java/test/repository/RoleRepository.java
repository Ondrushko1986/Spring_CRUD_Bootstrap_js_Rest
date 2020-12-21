package test.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.model.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

    Role findRoleByRole(String name);
}
