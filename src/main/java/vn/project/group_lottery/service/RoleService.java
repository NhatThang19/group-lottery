package vn.project.group_lottery.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.project.group_lottery.model.Role;
import vn.project.group_lottery.repository.RoleRepository;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Optional<Role> getRoleById(Long id) {
        return this.roleRepository.findById(id);
    }

    public Role getRoleByName(String roleName) {
        return this.roleRepository.findRoleByName(roleName);
    }
}
