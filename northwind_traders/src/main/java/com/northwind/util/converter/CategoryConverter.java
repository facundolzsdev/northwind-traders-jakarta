package com.northwind.util.converter;

import com.northwind.model.entity.Category;
import com.northwind.service.iProductService;
import jakarta.enterprise.inject.Model;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.inject.Inject;

import java.util.Optional;

/**
 * JSF converter for {@link Category} entity.
 * Converts between Category objects and their String representations.
 */
@Model
public class CategoryConverter implements Converter<Category> {

    @Inject
    iProductService productService;

    /**
     * Converts a String ID into a {@link Category} object using the product service.
     *
     * @param id the category ID as a string
     * @return the matching Category, or null if not found
     */
    @Override
    public Category getAsObject(FacesContext facesContext, UIComponent uiComponent, String id) {
        if (id == null) {
            return null;
        }
        Optional<Category> categoryOpt = productService.getCategoryById(Byte.valueOf(id));
        return categoryOpt.orElse(null);
    }

    /**
     * Converts a {@link Category} object into its String ID.
     *
     * @param category the Category to convert
     * @return the ID as a string, or "0" if null
     */
    @Override
    public String getAsString(FacesContext facesContext, UIComponent uiComponent, Category category) {
        if (category == null) {
            return "0";
        }
        return category.getId().toString();
    }
}
