package bytech.co.tz.matundasi.modules.users.repository;

import bytech.co.tz.matundasi.modules.users.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    @EntityGraph(attributePaths = {"branch", "roles"})
    List<User> findAllByDeletedAtIsNullOrderByFirstNameAscLastNameAsc();

    @EntityGraph(attributePaths = {"branch", "roles"})
    Optional<User> findByIdAndDeletedAtIsNull(UUID id);

    // Backs AuthService#login.
    @EntityGraph(attributePaths = {"branch", "roles"})
    Optional<User> findByUsernameIgnoreCaseAndDeletedAtIsNull(String username);

    boolean existsByUsernameIgnoreCaseAndDeletedAtIsNull(String username);

    boolean existsByUsernameIgnoreCaseAndDeletedAtIsNullAndIdNot(String username, UUID id);

    boolean existsByPhoneAndDeletedAtIsNull(String phone);

    boolean existsByPhoneAndDeletedAtIsNullAndIdNot(String phone, UUID id);

    // Backs BranchService#delete's "still has users" guard.
    boolean existsByBranch_IdAndDeletedAtIsNull(UUID branchId);

    // Backs RoleService#delete's "still assigned to a user" guard.
    boolean existsByRoles_IdAndDeletedAtIsNull(UUID roleId);
}
