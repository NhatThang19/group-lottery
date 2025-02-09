package vn.project.group_lottery.service;

import org.springframework.stereotype.Service;

import vn.project.group_lottery.model.Role;
import vn.project.group_lottery.repository.RoleRepository;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getRoleByName(String roleName) {
        return this.roleRepository.findRoleByName(roleName);
    }
}
