package com.northwind.util.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class ViewPaths {

    public static class Common {
        public static final String BASE = "/views/common/";
        public static final String ERROR_ACCESS_DENIED = BASE + "error/access-denied.jsf";
        public static final String ERROR_CUSTOM = BASE + "error/unexpected-error.jsf";
        public static final String LOGIN = BASE + "login.jsf";
        public static final String CUSTOMER_SIGNUP = BASE + "customer-signup.jsf";
        public static final String USER_ACCOUNT_EDIT = BASE + "user-account-edit.jsf";
    }

    public static class Admin {
        public static final String BASE = "/views/admin/";
        public static final String SIGNUP = BASE + "admin-signup.jsf";
        public static final String DASHBOARD = BASE + "dashboard.jsf";
        public static final String REPORTS = BASE + "reports.jsf";
        public static final String EMPLOYEE_INFO_EDIT = BASE + "employee-info-edit.jsf";
        public static final String EMPLOYEE_SIGNUP = BASE + "employee-signup.jsf";
        public static final String MANAGE_EMPLOYEES = BASE + "manage-employees.jsf";
    }

    public static class Employee {
        public static final String BASE = "/views/employee/";
        public static final String DASHBOARD = BASE + "dashboard.jsf";
        public static final String REPORTS = BASE + "reports.jsf";
        public static final String ORDERS_REVIEW = BASE + "orders-review.jsf";
        public static final String PRODUCT_FORM = BASE + "product-form-modal.jsf";
        public static final String PRODUCTS_REVIEW = BASE + "products-review.jsf";
    }

    public static class Customer {
        public static final String BASE = "/views/customer/";
        public static final String PRODUCTS = BASE + "products.jsf";
        public static final String CART = BASE + "cart.jsf";
        public static final String ORDERS = BASE + "orders.jsf";
        public static final String PERSONAL_INFO_EDIT = BASE + "personal-info-edit.jsf";
    }

    public static class Redirect {
        public static final String FACES = "?faces-redirect=true";
        public static final String EXPIRED = "?expired=true";
    }
}
