# 🚀 Northwind Traders — Jakarta EE (Java EE) Web Application

This repository contains the source code and technical documentation of the final portfolio project: an ecommerce web
platform developed with Jakarta EE, simulating the Northwind Traders store.

# 📌 Project Description

This project is an ecommerce web application that simulates the purchase and order management processes of a store,
conceptually based on the **Northwind** database.

The system allows the management of customers, employees, products, and orders, incorporating different user roles
(customer, employee, and administrator), each with specific functionalities based on their role. Employees and
administrators also have access to custom reports to view and analyze relevant store data.

---

# 🧑‍💼 Roles and functionalities

| Role     | Main Features                                                                                                                               |
|----------|------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Customer | Sign up, log in, add products to the cart, place orders, view and clear order history.                                           |
| Employee | View pending orders, process them (accept or reject), restock products, create new products and edit existing ones. Access to order and stock reports. |
| Admin    | Register new administrators and employees. Access system-wide reports and administration panels.                                       |

---

# 🔒 Account Management by Role

All users can manage their information depending on their account type:

**Customers** can edit their personal information and credentials, and delete their accounts.

**Administrators** can edit their own access credentials, as well as the personal information and credentials of
employees. They can also enable or disable employee accounts in the system.

**Employees** cannot edit their own profiles. Only administrators have permission to modify their accounts.

⚠️ Disabling an employee prevents them from accessing the system, but it does not delete their account or historical
records.

---

## ⚙️ Technologies Used

- Java EE (Jakarta EE 9.1)
- JSF 3.0 + PrimeFaces 10
- WildFly Application Server
- MySQL
- JPA (Hibernate as implementation)
- Maven
- Lombok
- Java Util Logging (native Java SE logging system)
- CDI and EJB for dependency and transaction management
- BCrypt (for secure password hashing)

---

## 🗂️ Project Structure

- `src/main/webapp`: Contains the JSF views and static resources. Views are organized into the `views` package,
  structured by user type:
    * `admin/`: Views exclusive to users with administrator role.
    * `customer/`: Views for authenticated customers.
    * `employee/`: Views for employees.
    * `common/`: Public or shared views, accessible without authentication or by multiple roles (such as login, customer
      registration, etc.).
- `src/main/resources`: Configuration files and persistent resources of the application. Includes folders for static
  resources like CSS and images.
    - `src/main/java/com.northwind`: Contains the core application logic organized in different packages:
        - `context`: Contains components that provide centralized and typed access to the authenticated user during the
          session. The main class is `AuthenticatedUserContext`, which handles user validation and retrieval of the
          logged-in `User`, centralizing session logic to avoid scattered manual access. It also includes specific
          contexts per user type (`EmployeeContext`, `CustomerContext` and `AdminContext`).
        - `controller`: Groups the Managed Beans responsible for coordinating interaction between the view (
          JSF/PrimeFaces)
          and business logic. It is organized in thematic subpackages such as `account`, `admin`, `auth`,  `customer`
          , `employee`, `signup`, among others. Each bean manages forms, UI events, validations, navigation, and user
          feedback, delegating business logic to corresponding services. Includes reusable beans in the `base`
          subpackage, as well as helper components for redirection and navigation (`nav`). Some beans extend base
          classes to avoid code duplication and improve maintainability.
        - `exceptions`: Custom exceptions that enable controlled error handling and specific user feedback.
        - `filters`: Contains filters that manage key aspects of navigation and security. Currently includes:
            * `SessionExpiredFilter`: Detects expired sessions and redirects the user accordingly.
            * `NoCacheFilter`: Prevents the browser from caching protected pages, avoiding them being viewed via the "
              Back" button after logout.
            * `RoleBasedAccessFilter`:Restricts access to pages based on the authenticated user's role. Redirects to
              an "access denied" page and logs unauthorized attempts.
            * `StaticResourcesFilter`: Sets caching headers for static resources like images, stylesheets, and scripts,
              improving site load performance.
        - `model`: Includes JPA entities representing database tables, as well as Data Transfer Objects (DTOs),
          enumerations (enums) for structured constants, and supporting classes located in the `support` subpackage.
        - `repository`: Interfaces and classes that encapsulate data access. They interact directly with the database
          using `EntityManager`, which is injected (not manually instantiated). These classes perform CRUD operations,
          custom queries, and other entity-related interactions.
        - `service`: Interfaces and implementation classes that encapsulate business logic and act as intermediaries
          between data access and presentation beans. Includes a `factory` subpackage for classes responsible for
          creating entities like `Order`, `CartItem`, etc., initialized with default values relevant to each business
          use case.
        - `util`: Contains utility classes that centralize shared logic to keep code clean and reusable. Includes the
          following subpackages:
            * `constants`: Includes the `ViewPaths` class defining constant view paths used throughout the system. Also
              contains the `messages` subpackage with classes grouping UI messages.
            * `converter`: Includes custom JSF converters that transform domain objects or values into string
              representations for use in JSF forms and vice versa. Currently includes:
                * `CategoryConverter`:  Converts between `Category` objects and their IDs (String) using the product
                  service. Enables selection of categories in components like <h:selectOneMenu>.
                * `DateConverter`: Converts `LocalDate` values into strings with "dd/MM/yyyy" format and vice versa.
                  Facilitates date input and display without time in forms.
                * `DateTimeConverter`: Similar to `DateConverter`, but for `LocalDateTime`, formatting as "dd/MM/yyyy
                  HH:mm"
                  . Useful for displaying dates and times clearly in the UI.
            * `exception`: Provides utilities for consistent exception handling in both the presentation and service
              layers. Includes:
                * `BeanExceptionUtil`: Helps manage exceptions in JSF beans. Logs the error and shows a message to the
                  user using `GrowlUtil`.
                * `ServiceExceptionUtil`: Centralizes service-layer exception handling. Logs errors, rethrows known
                  exceptions, or wraps them into service-specific exceptions in a uniform way.
            * `general`: Contains general-purpose utilities not tied to a specific layer but useful across multiple
              contexts. Includes:
                * `InputSanitizer`: Cleans user input for consistency and removes unwanted characters. Provides methods
                  for general text cleanup or alphanumeric field sanitization, capitalizing words and removing extra
                  spaces or symbols.
                * `OrderStatusUtil`: Maps order statuses to visual severity levels (e.g., "warning" for "PENDING", "
                  success" for "COMPLETED"), useful for UI display.
            * `jsf`: Includes reusable classes for displaying messages in the UI, managing user sessions, and handling
              safe redirections. Includes:
                * `FacesMessageUtil`: Centralizes the creation of `FacesMessage` messages for the JSF interface. Offers
                  static methods to display info, warning, or error messages at different severity levels.
                * `GrowlUtil`: Helper class for easily showing growl-style messages in JSF. Supports success, warning,
                  or error messages and can persist them across redirects using the flash scope.
                * `NavigationUtil`: Encapsulates JSF navigation logic, including secure redirections, error handling,
                  and logout management. Provides methods to redirect to specific pages, invalidate sessions, and manage
                  the `JSESSIONID` cookie.
                * `SessionUtil`: Utility class for accessing and manipulating session attributes related to the
                  authenticated user. Allows easy storage and retrieval of user session data like `ID`, `username`, or
                  the complete `User` object.
            * `producer`: Contains CDI (Contexts and Dependency Injection) producers to facilitate dependency injection
              throughout the application. Includes:
                * `LoggerProducer`: Automatically injects `Logger` instances associated with the class performing the
                  injection.
            * `security`: Utilities related to application security. Currently includes:
                * `PasswordEncoder`: Utility class for hashing and verifying passwords using the **BCrypt** algorithm.
            * `validation`: Utilities responsible for centralizing validation rules used across different areas of the
              system, both in the backend and the presentation layer. Its goal is to keep validation logic decoupled
              from beans and services, promoting reusability, clarity, and maintainability. Each class follows a clear
              structure and, in some cases, integrates with UI utilities (such as `GrowlUtil`) to display warnings in
              the interface.

---

## 💾 Database

The project uses an adapted version of **Northwind** (originally from Microsoft) as its database.

**Modifications made:**

- Adjustments to some field names and types.
- Additional tables were added to meet specific application needs.

> ⚠️️ Important: The database retains Northwind’s main structure but is not an exact replica. It has been customized and extended to fit the application's workflow.

---

## 📐 Conventions and Style

- **Repository** interfaces use the `find` prefix for query methods.
- **Service** interfaces use the `get` prefix for methods that return entities or lists.
- In service interfaces, a comment is used to highlight the methods that correspond to business logic specific to the
  service layer and not present in the repository layer.
- Dependency injection is handled via CDI.

---

## 🌐 Navigation and URL Security Management

The application implements mechanisms to control and secure navigation through URLs:

✅ Custom error page for non-existent routes (404) or critical error situations, acting as a landing page in case of
failure or incorrect access.

✅ Access filters that restrict navigation when no valid session is active, preventing unauthenticated users from
accessing internal resources by manipulating the URL.

✅ Cache control filter to prevent access to protected views using the browser's "Back" button after logging out, unless
the user logs in again.

✅ Filter that blocks authenticated users with a specific role from accessing pages intended for other roles (for
example, a customer accessing admin views by URL manipulation). This filter relies on patterns and validations from the
`util.validation` subpackage.

These controls ensure a clear navigation experience and a basic security layer, preventing unauthorized or misleading
access.

---

## 🧠 A Personal Consideration

While this project utilizes an adapted version of the Northwind database (originally from Microsoft), it's important to
note that I'm not entirely satisfied with its structure or the final outcome. In several aspects, I believe the data
model could be clearer, more efficient, and more consistent. For instance, some tables could be better decomposed:
entities like employee and customer share common attributes and could be derived from a base table. Likewise, normalized
geographical tables could be incorporated to represent cities, postal codes, and countries, instead of storing them as
simple text strings. There are also data types that could be better defined or adjusted to the application's actual
needs.

Nevertheless, I chose not to completely redesign the database, as the objective was to adapt to a pre-existing database,
which is common in real-world scenarios where developers must work with already defined structures. This limitation
presented a challenge and allowed me to focus on interpreting inherited models, integrating them properly with the
business logic, and maintaining system consistency within those constraints.

Undoubtedly, had I been free to design from scratch, I would have made different decisions regarding normalization,
entity relationships, and naming conventions, relying much more on normal forms to achieve a more optimized structure.
---

## ✨ Current Status and Potential Enhancements

While the application meets the essential requirements of an e-commerce platform—including registration, login,
purchases, order management, differentiated roles, and reporting—I'm aware there are many areas for improvement and
expansion. For instance, each user type (customer, employee, administrator) could benefit from additional
functionalities to enrich their experience or automate certain processes.

There are also visual and user experience aspects that could be optimized, especially regarding responsive design.
Currently, the application is not adapted for mobile devices. This is partly due to the time available and because
handling responsiveness in JSF + PrimeFaces presents an additional challenge. Frontend development isn't my primary
focus, but I still aimed to maintain a clear, functional, and consistent interface for the desktop environment.

It's important to note that all development was done individually, from scratch, with a full-stack approach. This meant
simultaneously tackling database adaptation, business logic, security, session management, validations, the user
interface, and other key aspects. While extending functionality is entirely feasible, I need to balance the time
invested in this project with the need to continue learning and developing in new technologies and contexts.

As the saying goes: "done is better than perfect." This project allowed me to consolidate many key concepts and gain a
comprehensive understanding of developing a complete web application, which was the main goal.

---

> To view the Spanish version, visit `README-ES.md`.
> Para ver la versión en español, visit `README-ES.md`.