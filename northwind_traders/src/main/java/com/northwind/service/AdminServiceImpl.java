package com.northwind.service;

import com.northwind.exceptions.AdminServiceException;
import com.northwind.model.entity.Admin;
import com.northwind.model.entity.User;
import com.northwind.repository.iAdminRepository;
import com.northwind.util.exception.ServiceExceptionUtil;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class AdminServiceImpl implements iAdminService {

    @Inject
    private iAdminRepository adminRepo;
    @Inject
    private iUserService userService;
    @Inject
    private Logger logger;

    @Override
    public List<Admin> getAllAdmins() {
        return adminRepo.findAll();
    }

    @Override
    public Optional<Admin> getAdminById(Integer id) {
        return Optional.ofNullable(adminRepo.findById(id));
    }

    @Override
    public void saveAdmin(Admin admin) {
        try {
            adminRepo.save(admin);
            logger.log(Level.INFO, "Admin saved with ID: {0}", admin.getId());
        } catch (Exception e) {
            handleException("saving admin: " + admin.getId(), e, Level.SEVERE);
        }
    }

    @Override
    public void deleteAdmin(Integer id) {
        try {
            Admin admin = getAdminById(id)
                    .orElseThrow(() -> new AdminServiceException("Admin not found. ID: " + id));
            User user = admin.getUser();

            adminRepo.delete(id);
            userService.deleteUser(user.getId());
            logger.info("Admin and associated user deleted — Admin ID: " + admin.getId() +
                    ", User ID: " + admin.getUser().getId());
        } catch (Exception e) {
            handleException("deleting admin with ID: " + id, e, Level.SEVERE);
        }
    }

    @Override
    public Optional<Admin> getAdminWithUserByUserId(Integer userId) {
        return Optional.ofNullable(adminRepo.findAdminWithUserByUserId(userId));
    }

    @Override
    public Optional<Admin> getByIdWithUser(Integer id) {
        return Optional.ofNullable(adminRepo.findByIdWithUser(id));
    }

    private void handleException(String action, Exception e, Level level) {
        ServiceExceptionUtil.handle(logger, action, e, level, AdminServiceException.class, null);
    }
}
