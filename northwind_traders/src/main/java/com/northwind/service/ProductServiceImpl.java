package com.northwind.service;

import com.northwind.exceptions.ProductNotFoundException;
import com.northwind.exceptions.ProductServiceException;
import com.northwind.model.support.Audit;
import com.northwind.model.entity.Category;
import com.northwind.model.entity.Product;
import com.northwind.repository.iCrudRepository;
import com.northwind.repository.iProductRepository;
import com.northwind.util.exception.ServiceExceptionUtil;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class ProductServiceImpl implements iProductService {

    @Inject
    private iProductRepository productRepo;
    @Inject
    private iCrudRepository<Category, Byte> categoryRepo;
    @Inject
    private Logger logger;

    @Override
    public Optional<Product> getProductById(Integer id) {
        return Optional.ofNullable(productRepo.findById(id));
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    @Override
    public void updateProductWithAudit(Product product) {
        try {
            preserveAuditIfExists(product);
            productRepo.save(product);
            logger.log(Level.INFO, "Product updated with ID: {0}", product.getId());
        } catch (ProductNotFoundException e) {
            throw e;
        } catch (Exception e) {
            handleException("updating product with ID: " + product.getId(), e, Level.SEVERE);
        }
    }

    @Override
    public Product requireProductById(Integer id) {
        return getProductById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public void saveProduct(Product product) {
        try {
            preserveAuditIfExists(product);
            productRepo.save(product);
            logger.log(Level.INFO, "Product saved with ID: {0}", product.getId());
        } catch (Exception e) {
            handleException("saving product: " + (product.getName() != null ? product.getName() : "Unknown Product"), e, Level.SEVERE);
        }
    }

    @Override
    public void deleteProduct(Integer id) {
        try {
            requireProductById(id, "Cannot delete product. Product not found.");
            productRepo.delete(id);
            logger.log(Level.INFO, "Product deleted with ID: {0}", id);
        } catch (ProductNotFoundException e) {
            throw e;
        } catch (Exception e) {
            handleException("deleting product with ID: " + id, e, Level.SEVERE);
        }
    }

    @Override
    public List<Product> getProductByName(String name) {
        return productRepo.findProductByName(name);
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    @Override
    public Optional<Category> getCategoryById(Byte id) {
        return Optional.ofNullable(categoryRepo.findById(id));
    }

    private void preserveAuditIfExists(Product product) {
        if (product.getId() != null) {
            Product existingProduct = requireProductById(product.getId(), "Audit preservation failed.");
            product.setAudit(existingProduct.getAudit());
        } else {
            product.setAudit(Audit.createDefaultAudit());
        }
    }

    public Product requireProductById(Integer id, String message) {
        return getProductById(id).orElseThrow(() -> new ProductNotFoundException(message));
    }

    private void handleException(String action, Exception e, Level level) {
        ServiceExceptionUtil.handle(logger, action, e, level, ProductServiceException.class, ProductNotFoundException.class);
    }
}
