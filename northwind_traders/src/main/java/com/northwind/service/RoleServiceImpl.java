package com.northwind.service;

import com.northwind.model.entity.Role;
import com.northwind.model.enums.RoleType;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.Optional;

@Stateless
public class RoleServiceImpl implements iRoleService {

    @Inject
    private EntityManager em;

    @Override
    public Optional<Role> getRoleByType(RoleType roleType) {
        try {
            Role role = em.createQuery(
                            "SELECT r FROM Role r WHERE r.name = :roleType", Role.class)
                    .setParameter("roleType", roleType)
                    .getSingleResult();
            return Optional.of(role);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
