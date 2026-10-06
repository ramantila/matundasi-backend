package bytech.co.tz.matundasi.common.seeder;

import bytech.co.tz.matundasi.modules.branch.entity.Branch;
import bytech.co.tz.matundasi.modules.branch.repository.BranchRepository;
import bytech.co.tz.matundasi.modules.permissions.entity.Permission;
import bytech.co.tz.matundasi.modules.permissions.repository.PermissionRepository;
import bytech.co.tz.matundasi.modules.rolepermissions.service.RolePermissionService;
import bytech.co.tz.matundasi.modules.roles.entity.Role;
import bytech.co.tz.matundasi.modules.roles.repository.RoleRepository;
import bytech.co.tz.matundasi.modules.users.entity.User;
import bytech.co.tz.matundasi.modules.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

// Runs on every startup and only creates what is missing, so it is safe to
// leave on. Existing records (including the admin's password) are never changed,
// except that the Super Admin role is topped up with every active permission.
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.seed.enabled", havingValue = "true", matchIfMissing = true)
public class DataSeeder implements ApplicationRunner {
    // Permission names are <RESOURCE>_<ACTION>, e.g. COLLAR_CREATE. Must match
    // PERMISSION_KEYS in the frontend's lib/resources.js.
    private static final List<String> RESOURCES = List.of(
            "PROJECT", "PROSPECT", "GRID", "PIT",
            "COLLAR", "RAB_COLLAR", "MW_S", "DRILL_SURVEY", "CORE_SIZE", "PER_AREA_HOLE",
            "DRILL", "SAMPLE", "COMPOSITE", "COMPOS_AU", "LITHOLOGY", "STD_REFERENCE",
            "DISPATCH", "DIFFERENCE", "INVOICE",
            "BRANCH", "ELEMENT", "COLOUR", "ROCK", "QUALIFIER", "SCHISTOSITY", "SIZE", "WEATHERING",
            "USER", "ROLE", "PERMISSION", "ROLE_PERMISSION"
    );
    private static final List<String> ACTIONS = List.of("VIEW", "CREATE", "UPDATE", "DELETE");

    // Names from the first version of this seeder (e.g. VIEW_COLLARS); soft-deleted on startup.
    private static final List<String> LEGACY_SLUGS = List.of(
            "projects", "prospects", "grids", "pits",
            "collars", "rab-collars", "mw-s", "drill-surveys", "core-sizes", "per-area-holes",
            "drills", "samples", "composites", "compos-au", "lithology", "std-references",
            "dispatches", "differences", "invoices",
            "branches", "elements", "colours", "rocks", "qualifiers", "schistosity", "sizes", "weathering",
            "users", "roles", "permissions", "role-permissions"
    );
    private static final List<String> LEGACY_ACTIONS = List.of("VIEW", "ADD", "EDIT", "DELETE");

    private final PermissionRepository permissionRepository;
    private final BranchRepository branchRepository;
    private final RoleRepository roleRepository;
    private final RolePermissionService rolePermissionService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.seed.branch-name}")
    private String branchName;

    @Value("${app.seed.role-name}")
    private String roleName;

    @Value("${app.seed.admin.username}")
    private String adminUsername;

    @Value("${app.seed.admin.password}")
    private String adminPassword;

    @Value("${app.seed.admin.first-name}")
    private String adminFirstName;

    @Value("${app.seed.admin.last-name}")
    private String adminLastName;

    @Value("${app.seed.admin.phone}")
    private String adminPhone;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        int createdPermissions = seedPermissions();
        Branch branch = seedBranch();
        Role role = seedSuperAdminRole();
        boolean createdAdmin = seedAdminUser(branch, role);

        log.info("Seeder finished: {} permission(s) created, admin user {}",
                createdPermissions, createdAdmin ? "created" : "already present");
    }

    private int seedPermissions() {
        Set<String> legacy = new HashSet<>();
        for (String slug : LEGACY_SLUGS) {
            for (String action : LEGACY_ACTIONS) {
                legacy.add(action + "_" + slug.replace('-', '_').toUpperCase());
            }
        }
        for (Permission permission : permissionRepository.findAllByDeletedAtIsNullOrderByNameAsc()) {
            if (legacy.contains(permission.getName())) {
                permission.softDelete();
                permissionRepository.save(permission);
            }
        }

        int created = 0;
        for (String name : permissionNames()) {
            if (!permissionRepository.existsByNameIgnoreCaseAndDeletedAtIsNull(name)) {
                permissionRepository.save(Permission.builder().name(name).build());
                created++;
            }
        }
        return created;
    }

    private Branch seedBranch() {
        return branchRepository.findFirstByNameIgnoreCaseAndDeletedAtIsNull(branchName)
                .orElseGet(() -> branchRepository.save(Branch.builder().name(branchName).build()));
    }

    private Role seedSuperAdminRole() {
        Role role = roleRepository.findFirstByNameIgnoreCaseAndDeletedAtIsNull(roleName)
                .orElseGet(() -> roleRepository.save(Role.builder()
                        .name(roleName)
                        .description("Full access to every module")
                        .active(true)
                        .build()));

        Set<UUID> allPermissionIds = permissionRepository.findAllByDeletedAtIsNullOrderByNameAsc()
                .stream()
                .map(Permission::getId)
                .collect(Collectors.toSet());
        rolePermissionService.replacePermissions(role, allPermissionIds);
        return role;
    }

    private boolean seedAdminUser(Branch branch, Role role) {
        if (userRepository.existsByUsernameIgnoreCaseAndDeletedAtIsNull(adminUsername)) {
            return false;
        }

        User admin = User.builder()
                .branch(branch)
                .firstName(adminFirstName)
                .lastName(adminLastName)
                .phone(adminPhone)
                .username(adminUsername)
                .password(passwordEncoder.encode(adminPassword))
                .roles(new HashSet<>(Set.of(role)))
                .build();
        userRepository.save(admin);

        log.warn("Seeded admin user '{}' with the configured default password. Change it after first login.",
                adminUsername);
        return true;
    }

    private static List<String> permissionNames() {
        List<String> names = new ArrayList<>();
        for (String resource : RESOURCES) {
            for (String action : ACTIONS) {
                names.add(resource + "_" + action);
            }
        }
        return names;
    }
}
