package bytech.co.tz.matundasi.modules.branch.service;

import bytech.co.tz.matundasi.modules.branch.entity.Branch;
import bytech.co.tz.matundasi.modules.branch.payload.BranchRequests;
import bytech.co.tz.matundasi.modules.branch.repository.BranchRepository;
import bytech.co.tz.matundasi.modules.branch.response.BranchResponse;
import bytech.co.tz.matundasi.modules.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BranchService {
    private final BranchRepository branchRepository;
    private final UserRepository userRepository;

    @Transactional
    public BranchResponse create(BranchRequests request) {
        Branch branch = Branch.builder()
                .name(request.name().trim())
                .build();

        return toResponse(branchRepository.save(branch));
    }

    @Transactional(readOnly = true)
    public List<BranchResponse> findAll() {
        return branchRepository.findAllByDeletedAtIsNullOrderByNameAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public BranchResponse findById(UUID id) {
        return toResponse(getActiveBranch(id));
    }

    @Transactional
    public BranchResponse update(UUID id, BranchRequests request) {
        Branch branch = getActiveBranch(id);
        branch.setName(request.name().trim());

        return toResponse(branchRepository.save(branch));
    }

    @Transactional
    public void delete(UUID id) {
        Branch branch = getActiveBranch(id);

        if (userRepository.existsByBranch_IdAndDeletedAtIsNull(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Branch still has one or more users");
        }

        branch.softDelete();
        branchRepository.save(branch);
    }

    private Branch getActiveBranch(UUID id) {
        return branchRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Branch not found"));
    }

    private BranchResponse toResponse(Branch branch) {
        return new BranchResponse(
                branch.getId(),
                branch.getName()
        );
    }
}
