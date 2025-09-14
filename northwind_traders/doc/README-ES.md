# 🚀 Northwind Traders — Jakarta EE (Java EE) Web Application

Este repositorio contiene el código fuente y la documentación técnica del proyecto final del portafolio: una plataforma
web de ecommerce desarrollada con Jakarta EE, simulando la tienda Northwind Traders.

## 📌 Descripción del Proyecto

Este proyecto es una aplicación web de comercio electrónico que simula el proceso de compra y administración de pedidos
de una tienda, basada conceptualmente en la base de datos **Northwind**.

El sistema permite gestionar clientes, empleados, productos y órdenes, incorporando distintos perfiles de usuario
(cliente, empleado y administrador), cada uno con funcionalidades específicas según su rol. Los empleados y
administradores también cuentan con acceso a reportes propios para visualizar y analizar datos relevantes de la tienda.

---

## 🧑‍💼 Roles y funcionalidades

| Rol        | Funcionalidades principales                                                                                                                                             |
|------------|-------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Cliente    | Registrarse, iniciar sesión, agregar productos al carrito, realizar pedidos, ver historial de órdenes y limpiarlo.                                                      |
| Empleado   | Ver pedidos pendientes, procesarlos (aceptar o rechazar), reponer stock de productos, crear nuevos productos y editar existentes. Acceso a reportes de órdenes y stock. |
| Administrador | Registrar nuevos Administradores y Empleados. Acceder a reportes generales del sistema y a paneles de administración.                                                   |

---

## 🔒 Gestión de cuentas por rol:

Todos los usuarios pueden ser gestionados en su información, según su tipo de cuenta:

**Clientes** pueden modificar sus datos personales y credenciales, y eliminar sus cuentas.

**Administradores** pueden editar sus propias credenciales de acceso, así como la información personal y credenciales de
los empleados. Además, tienen la posibilidad de habilitar o deshabilitar cuentas de empleados dentro del sistema.

**Empleados** no pueden editar su propio perfil. Solo los administradores tienen permisos para modificar sus cuentas.

⚠️ La inhabilitación de un empleado impide su acceso al sistema, pero no elimina su cuenta ni sus registros.

---

## ⚙️ Tecnologías Utilizadas

- Java EE (Jakarta EE 9.1)
- JSF 3.0 + PrimeFaces 10
- WildFly Application Server
- MySQL
- JPA (Hibernate como implementación)
- Maven
- Lombok
- Java Util Logging (sistema de logs nativo de Java SE)
- CDI y EJB para gestión de dependencias y transacciones
- BCrypt (para hashing seguro de contraseñas)

---

## 🗂️ Estructura del Proyecto

- `src/main/webapp`: Contiene las vistas JSF y los recursos estáticos. Las vistas están organizadas en el
  paquete `views`, estructurado por tipo de usuario:
    * `admin/`: Vistas exclusivas para usuarios con rol de administrador.
    * `customer/`: Vistas específicas para clientes autenticados.
    * `employee/`: Vistas destinadas al personal empleado.
    * `common/`: Vistas públicas o compartidas, accesibles sin autenticación o por múltiples roles (como login, registro
      de clientes, etc.).
- `src/main/resources`: Configuraciones y archivos persistentes de la aplicación. Incluye carpetas para recursos
  estáticos como CSS e imágenes.
- `src/main/java/com.northwind`: Contiene la lógica principal de la aplicación organizada en diferentes paquetes:
    - `context`: Contiene componentes que permiten acceder de forma centralizada y tipada al usuario autenticado durante
      la sesión. La clase principal es `AuthenticatedUserContext`, que facilita la validación y obtención del `User`
      logueado, y centraliza la lógica relacionada a la sesión para evitar accesos manuales dispersos. También cuenta
      con contextos específicos por tipo de usuario (`EmployeeContext`, `CustomerContext` y `AdminContext`).
    - `controller`: Agrupa los Managed Beans responsables de coordinar la interacción entre la vista
      (JSF/PrimeFaces) y la lógica de negocio. Está organizado en subpaquetes temáticos como `account`, `admin`, `auth`,
      `customer`, `employee`, `signup`, entre otros. Cada bean se encarga de manejar formularios, eventos de interfaz,
      validaciones, navegación y feedback al usuario, delegando la lógica a los servicios correspondientes. Incluye
      beans reutilizables en el subpaquete `base`, así como componentes auxiliares para redirección y navegación (`nav`)
      . Algunos beans extienden clases base para evitar duplicación de código y facilitar la mantenibilidad.
    - `exceptions`: Excepciones personalizadas que permiten manejar errores de forma controlada y mostrar mensajes
      específicos al usuario.
    - `filters`: Contiene los filtros que gestionan aspectos clave de la navegación y seguridad de la aplicación.
      Actualmente incluye:
        * `SessionExpiredFilter`: Detecta sesiones expiradas y redirige al usuario de forma adecuada.
        * `NoCacheFilter`: Impide que el navegador almacene páginas protegidas en caché, evitando que se puedan
          visualizar con el botón "Atrás" después de cerrar sesión.
        * `RoleBasedAccessFilter`: Restringe el acceso a páginas según el rol del usuario autenticado. Redirige a una
          página de acceso denegado en caso de intentos no autorizados y registra dichos intentos en el log.
        * `StaticResourcesFilter`: Establece encabezados de caché para recursos estáticos como imágenes, hojas de estilo
          y scripts, mejorando el rendimiento de la carga del sitio.
    - `model`: Incluye las entidades JPA que representan las tablas de la base de datos, además de Data Transfer
      Objects (DTOs), enumeraciones (enums) que aportan claridad y estructura a ciertos valores constantes, y clases de
      apoyo ubicadas en el subpaquete `support`.
    - `repository`: Interfaces y clases que encapsulan el acceso a datos. Interactúan directamente con la base de datos
      utilizando `EntityManager`, el cual es inyectado (no instanciado manualmente). Estas clases se encargan de
      realizar operaciones CRUD, consultas personalizadas y otras interacciones específicas con las entidades
      persistentes.
    - `service`: Interfaces y clases de implementación que encapsulan la lógica de negocio y actúan como intermediarios
      entre el acceso a datos y los beans de presentación. Incluye un subpaquete `factory`, que agrupa clases
      responsables de la creación de entidades como `Order`, `CartItem`, etc., inicializándolas con valores por defecto
      relevantes para cada caso de uso del negocio.
    - `util`: Contiene clases utilitarias que centralizan lógica compartida para mantener el código limpio y
      reutilizable. Actualmente incluye los siguientes subpaquetes:
        * `constants`: Contiene la clase `ViewPaths`, que define rutas constantes para las vistas del sistema. Incluye
          también el subpaquete `messages`, con clases que agrupan los distintos mensajes mostrados en la interfaz de
          usuario.
        * `converter`: Contiene convertidores JSF personalizados que permiten transformar objetos de dominio o valores
          en representaciones string para su uso en formularios JSF, y viceversa. Actualmente incluye:
            * `CategoryConverter`: Convierte entre objetos `Category` y sus IDs (String) usando el servicio de
              productos. Permite seleccionar categorías en componentes como <h:selectOneMenu>.
            * `DateConverter`: Convierte valores `LocalDate` en strings con formato "dd/MM/yyyy" y viceversa. Facilita
              la entrada y visualización de fechas sin tiempo en formularios.
            * `DateTimeConverter`: Similar a `DateConverter`, pero para valores `LocalDateTime`, formateando como
              "dd/MM/yyyy HH:mm". Útil para mostrar fechas y horas de forma legible en la UI.
        * `exception`: Proporciona utilidades para el manejo consistente de excepciones tanto en la capa de presentación
          como en la capa de servicios. Incluye:
            * `BeanExceptionUtil`: Facilita el manejo de excepciones en beans JSF. Registra el error y muestra un
              mensaje al usuario mediante `GrowlUtil`.
            * `ServiceExceptionUtil`: Centraliza el manejo de excepciones en los servicios. Permite registrar errores,
              re-lanzar excepciones conocidas o envolverlas en excepciones específicas de la capa de servicio de forma
              uniforme.
        * `general`: Contiene utilidades generales no acopladas a una capa específica, pero útiles en múltiples
          contextos. Incluye:
            * `InputSanitizer`: Permite sanear entradas de texto del usuario para garantizar uniformidad y evitar
              caracteres no deseados. Ofrece métodos para limpiar textos generales o campos alfanuméricos, capitalizando
              palabras y eliminando espacios y símbolos innecesarios.
            * `OrderStatusUtil`: Facilita la asignación de niveles de severidad visual según el estado de una orden (por
              ejemplo, "warning" para "PENDIENTE", "success" para "COMPLETADO", etc.), útil para la presentación en la
              UI.
        * `jsf`: Contiene clases reutilizables para mostrar mensajes en la interfaz, gestionar la sesión del usuario y
          manejar redirecciones de forma segura. Incluye:
            * `FacesMessageUtil`: Centraliza la creación de mensajes `FacesMessage` para la interfaz JSF. Ofrece métodos
              estáticos para mostrar mensajes informativos, de advertencia o de error con diferentes niveles de
              severidad.
            * `GrowlUtil`: Clase auxiliar para mostrar mensajes tipo growl en JSF de forma sencilla. Permite mostrar
              mensajes de éxito, advertencia o error, y también mantenerlos en el flash scope para que persistan tras
              una redirección.
            * `NavigationUtil`: Encapsula lógica de navegación en JSF, incluyendo redirecciones seguras, manejo de
              errores y cierre de sesión. Ofrece métodos para redirigir a páginas específicas, invalidar sesiones y
              gestionar la cookie `JSESSIONID`.
            * `SessionUtil`: Clase utilitaria para acceder y manipular atributos de sesión relacionados con el usuario
              autenticado. Permite almacenar datos del usuario en sesión y recuperarlos fácilmente, como el `ID`
              , `username` o el objeto completo `User`.
        * `producer`: Contiene productor CDI (Contexts and Dependency Injection) para facilitar la inyección de
          dependencias en toda la aplicación. Incluye:
            * `LoggerProducer`: Permite inyectar automáticamente instancias de `Logger` asociadas a la clase que realiza
              la inyección.
        * `security`: Utilitarias relacionadas con la seguridad de la aplicación. Actualmente incluye:
            * `PasswordEncoder`: Clase utilitaria para hashear y verificar contraseñas utilizando el algoritmo
              **BCrypt**.
        * `validation`: Utilitarias responsables de centralizar las reglas de validación utilizadas en distintas áreas
          del sistema, tanto en el backend como en la capa de presentación. Su objetivo es mantener el código validante
          desacoplado de los beans y servicios, favoreciendo la reutilización, la claridad y el mantenimiento. Cada
          clase sigue una estructura clara, y en algunos casos se integran con utilidades de la interfaz (como
          `GrowlUtil`) para mostrar advertencias en la UI.

---

## 💾 Base de Datos

El proyecto utiliza como base de datos una versión adaptada de **Northwind** (original de Microsoft).

**Modificaciones realizadas:**

- Ajustes en algunos nombres y tipos de campos.
- Se agregaron tablas adicionales específicas para cubrir necesidades de la aplicación.

> ⚠️ Importante: La base de datos mantiene la estructura principal de Northwind, pero no es una réplica exacta. Se encuentra personalizada y extendida para adaptarse al flujo de la aplicación.

---

## 📐 Convenciones y Estilo

- En las interfaces de **repositorio** se utiliza el prefijo `find` para métodos de consulta.
- En las interfaces de **servicio** se utiliza el prefijo `get` para métodos que devuelven entidades o listas.
- En las interfaces de servicio, se destacan con un comentario los métodos que corresponden a lógica de negocio
  específica de servicio que no existe a nivel repositorio.
- Se utiliza inyección de dependencias mediante CDI.

---

## 🌐 Gestión de Navegación y Seguridad en URLs

La aplicación implementa mecanismos para controlar y proteger la navegación mediante URLs:

✅ Página personalizada de errores para rutas inexistentes (404) o situaciones de error graves, que actúa como página de
aterrizaje ante fallos o accesos incorrectos.

✅ Filtros de acceso que restringen la navegación si no existe una sesión iniciada y válida, evitando que usuarios no
autenticados accedan a recursos internos de la aplicación manipulando la URL.

✅ Filtro de control de caché para evitar que, tras cerrar sesión, sea posible acceder a vistas protegidas mediante el
botón "Atrás" del navegador sin volver a iniciar sesión.

✅ Filtro que impide que usuarios autenticados con un rol específico accedan a páginas destinadas a otros roles (por
ejemplo, que un cliente acceda a vistas administrativas manipulando la URL). Este filtro se apoya en patrones y
validaciones del subpaquete `util.validation`.

Estos controles garantizan tanto una experiencia de navegación clara como una capa básica de seguridad, previniendo
accesos no autorizados o confusos.

---

## 🎞️ Playlist en YouTube

Serie de videos explicando detalladamente el funcionamiento del sistema.

🔗 [Ver la playlist en YouTube](https://youtube.com/playlist?list=PLo6gJIiicJy_CTeun0VCVttNv1B8mRzYC&si=tOlpt0eCm3cFriel)

---

## 🧠 Una Consideración Personal

Si bien el proyecto utiliza una versión adaptada de la base de datos Northwind (original de Microsoft), es importante
destacar que no estoy completamente conforme con su estructura ni con el resultado final. En varios aspectos, considero
que el modelo de datos podría ser más claro, eficiente y coherente. Por ejemplo, algunas tablas podrían estar mejor
descompuestas: entidades como empleado y cliente comparten atributos comunes y podrían derivarse de una tabla base.
Asimismo, se podrían incorporar tablas geográficas normalizadas para representar ciudades, códigos postales y países, en
lugar de almacenarlos como simples cadenas de texto. También hay tipos de datos que podrían estar mejor definidos o
ajustados a las necesidades reales de la aplicación.

Aun así, opté por no rediseñar la base por completo, ya que el objetivo era adaptarme a una base de datos preexistente,
algo común en entornos reales donde los desarrolladores deben trabajar sobre estructuras ya definidas. Esta limitación
representó un desafío y me permitió enfocarme en interpretar modelos heredados, integrarlos adecuadamente con la lógica
de negocio y mantener la coherencia del sistema dentro de esas restricciones.

Sin dudas, si hubiera tenido libertad para diseñar desde cero, habría tomado decisiones distintas en cuanto a
normalización, relaciones entre entidades y criterios de nomenclatura, apoyándome mucho más en las formas normales para
lograr una estructura más optimizada.

---

## ✨ Estado Actual y Posibles Mejoras

Si bien la aplicación cumple con los requerimientos esenciales de un e-commerce —incluyendo registro, inicio de sesión,
compras, gestión de pedidos, roles diferenciados y reportes—, soy consciente de que existen múltiples áreas de mejora y
expansión. Por ejemplo, cada tipo de usuario (cliente, empleado, administrador) podría contar con funcionalidades
adicionales que enriquezcan su experiencia o automaticen ciertos procesos.

También hay aspectos visuales y de experiencia de usuario que podrían optimizarse, especialmente en lo referente al
diseño responsive. Actualmente, la aplicación no está adaptada para dispositivos móviles. Esto se debe, en parte, al
tiempo disponible y a que lidiar con la responsividad en JSF + PrimeFaces representa un desafío adicional. El frontend
no es mi área principal de enfoque, pero aun así procuré mantener una interfaz clara, funcional y coherente para el
entorno de escritorio.

Es importante señalar que todo el desarrollo fue realizado de forma individual, desde cero, y con un enfoque full-stack.
Esto implicó abordar simultáneamente la adaptación de la base de datos, la lógica de negocio, la seguridad, el manejo de
sesiones, las validaciones, la interfaz de usuario y otros aspectos clave. Si bien extender la funcionalidad es
totalmente viable, debo equilibrar el tiempo invertido en este proyecto con la necesidad de seguir aprendiendo y
desarrollando en nuevas tecnologías y contextos.

Como suele decirse: “mejor terminado que perfecto”. Este proyecto me permitió consolidar muchos conceptos clave y
obtener una visión integral del desarrollo de una aplicación web completa, lo cual era el objetivo principal.

---

> Para ver la versión en inglés, visita `README-EN.md`.
> To view the English version, visit `README-EN.md`.
