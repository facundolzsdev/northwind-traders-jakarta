-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         8.0.30 - MySQL Community Server - GPL
-- SO del servidor:              Win64
-- HeidiSQL Versión:             12.10.0.7000
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Volcando datos para la tabla northwind_db.admins: ~1 rows (aproximadamente)
INSERT IGNORE INTO `admins` (`AdminID`, `UserID`, `CreatedAt`, `UpdatedAt`) VALUES
	(9, 95, '2025-06-04 10:39:02', NULL);

-- Volcando datos para la tabla northwind_db.carts: ~0 rows (aproximadamente)

-- Volcando datos para la tabla northwind_db.cart_items: ~0 rows (aproximadamente)

-- Volcando datos para la tabla northwind_db.categories: ~8 rows (aproximadamente)
INSERT IGNORE INTO `categories` (`CategoryID`, `CategoryName`, `Description`) VALUES
	(1, 'Beverages', 'Soft drinks, coffees, teas, beers, and ales'),
	(2, 'Condiments', 'Sweet and savory sauces, relishes, spreads, and seasonings'),
	(3, 'Confections', 'Desserts, candies, and sweet breads'),
	(4, 'Dairy Products', 'Cheeses'),
	(5, 'Grains/Cereals', 'Breads, crackers, pasta, and cereal'),
	(6, 'Meat/Poultry', 'Prepared meats'),
	(7, 'Produce', 'Dried fruit and bean curd'),
	(8, 'Seafood', 'Seaweed and fish');

-- Volcando datos para la tabla northwind_db.customers: ~0 rows (aproximadamente)

-- Volcando datos para la tabla northwind_db.employees: ~0 rows (aproximadamente)

-- Volcando datos para la tabla northwind_db.historical_orders: ~0 rows (aproximadamente)

-- Volcando datos para la tabla northwind_db.historical_order_details: ~0 rows (aproximadamente)

-- Volcando datos para la tabla northwind_db.orders: ~0 rows (aproximadamente)

-- Volcando datos para la tabla northwind_db.order_details: ~0 rows (aproximadamente)

-- Volcando datos para la tabla northwind_db.products: ~80 rows (aproximadamente)
INSERT IGNORE INTO `products` (`ProductID`, `ProductName`, `CategoryID`, `Unit`, `Price`, `Stock`, `Active`, `CreatedAt`, `UpdatedAt`) VALUES
	(1, 'Chais', 1, '10 boxes x 20 bags', 18.00, 20, 1, '2024-12-05 09:14:15', NULL),
	(2, 'Chang', 1, '24 - 12 oz bottles', 19.00, 20, 1, '2024-12-05 09:14:21', NULL),
	(3, 'Aniseed Syrup', 2, '12 - 550 ml bottles', 10.00, 20, 1, '2024-12-05 09:14:22', NULL),
	(4, 'Chef Antons Cajun Seasoning', 2, '48 - 6 oz jars', 22.00, 20, 1, '2024-12-05 09:14:23', NULL),
	(5, 'Chef Anton\'s Gumbo Mix', 2, '36 boxes', 21.00, 20, 1, '2024-12-05 09:14:23', NULL),
	(6, 'Grandma\'s Boysenberry Spread', 2, '12 - 8 oz jars', 25.00, 20, 1, '2024-12-05 09:14:24', NULL),
	(7, 'Uncle Bob\'s Organic Dried Pears', 7, '12 - 1 lb pkgs.', 30.00, 20, 1, '2024-12-05 09:14:25', NULL),
	(8, 'Northwoods Cranberry Sauce', 2, '12 - 12 oz jars', 40.00, 20, 1, '2024-12-05 09:14:26', NULL),
	(9, 'Mishi Kobe Niku', 6, '18 - 500 g pkgs.', 98.00, 20, 1, '2024-12-05 09:14:27', NULL),
	(10, 'Ikura', 8, '12 - 200 ml jars', 31.00, 20, 1, '2024-12-05 09:14:28', NULL),
	(11, 'Queso Cabrales', 4, '1 kg pkg.', 21.00, 20, 1, '2024-12-05 09:14:29', NULL),
	(12, 'Queso Manchego La Pastora', 4, '10 - 500 g pkgs.', 38.00, 20, 1, '2024-12-05 09:14:29', NULL),
	(13, 'Konbu', 8, '2 kg box', 6.00, 20, 1, '2024-12-05 09:14:30', NULL),
	(14, 'Tofu', 7, '40 100 G Pkgs.', 25.00, 20, 1, '2024-12-05 09:14:31', NULL),
	(15, 'Genen Shouyu', 2, '24 - 250 ml bottles', 16.00, 20, 1, '2024-12-05 09:14:32', NULL),
	(16, 'Pavlova', 3, '32 - 500 g boxes', 17.00, 20, 1, '2024-12-05 09:14:32', NULL),
	(17, 'Alice Mutton', 6, '20 - 1 kg tins', 39.00, 20, 1, '2024-12-05 09:14:33', NULL),
	(18, 'Carnarvon Tigers', 8, '16 kg pkg.', 63.00, 20, 1, '2024-12-05 09:14:34', NULL),
	(19, 'Teatime Chocolate Biscuits', 3, '10 boxes x 12 pieces', 9.00, 20, 1, '2024-12-05 09:14:35', NULL),
	(20, 'Sir Rodney\'s Marmalade', 3, '30 gift boxes', 81.00, 20, 1, '2024-12-05 09:14:37', NULL),
	(21, 'Sir Rodney\'s Scones', 3, '24 pkgs. x 4 pieces', 10.00, 20, 1, '2024-12-05 09:14:38', NULL),
	(22, 'Gustaf\'s Knäckebröd', 5, '24 - 500 g pkgs.', 21.00, 20, 1, '2024-12-05 09:14:39', NULL),
	(23, 'Tunnbröd', 5, '12 - 250 g pkgs.', 9.00, 20, 1, '2024-12-05 09:14:39', NULL),
	(24, 'Guaraná Fantástica', 1, '12 - 355 ml cans', 5.00, 20, 1, '2024-12-05 09:14:40', NULL),
	(25, 'NuNuCa Nuß-Nougat-Creme', 3, '20 - 450 g glasses', 14.00, 20, 1, '2024-12-05 09:14:41', NULL),
	(26, 'Gumbär Gummibärchen', 3, '100 - 250 g bags', 31.00, 20, 1, '2024-12-05 09:14:41', NULL),
	(27, 'Schoggi Schokolade', 3, '100 - 100 g pieces', 44.00, 20, 1, '2024-12-05 09:14:42', NULL),
	(28, 'Rössle Sauerkraut', 7, '25 - 825 g cans', 46.00, 20, 1, '2024-12-05 09:14:43', NULL),
	(29, 'Thüringer Rostbratwurst', 6, '50 bags x 30 sausgs.', 124.00, 20, 1, '2024-12-05 09:14:43', NULL),
	(30, 'Nord-Ost Matjeshering', 8, '10 - 200 g glasses', 26.00, 20, 1, '2024-12-05 09:14:44', NULL),
	(31, 'Gorgonzola Telino', 4, '12 - 100 g pkgs', 13.00, 20, 1, '2024-12-05 09:14:45', NULL),
	(32, 'Mascarpone Fabioli', 4, '24 - 200 g pkgs.', 32.00, 20, 1, '2024-12-05 09:14:45', NULL),
	(33, 'Geitost', 4, '500 g', 3.00, 20, 1, '2024-12-05 09:14:46', NULL),
	(34, 'Sasquatch Ale', 1, '24 - 12 oz bottles', 14.00, 20, 1, '2024-12-05 09:14:47', NULL),
	(35, 'Steeleye Stout', 1, '24 - 12 oz bottles', 18.00, 20, 1, '2024-12-05 09:14:48', NULL),
	(36, 'Inlagd Sill', 8, '24 - 250 g jars', 19.00, 20, 1, '2024-12-05 09:14:48', NULL),
	(37, 'Gravad lax', 8, '12 - 500 g pkgs.', 26.00, 20, 1, '2024-12-05 09:14:49', NULL),
	(38, 'Côte de Blaye', 1, '12 - 75 cl bottles', 264.00, 20, 1, '2024-12-05 09:14:51', NULL),
	(39, 'Chartreuse verte', 1, '750 cc per bottle', 18.00, 20, 1, '2024-12-05 09:14:52', NULL),
	(40, 'Boston Crab Meat', 8, '24 - 4 oz tins', 18.00, 20, 1, '2024-12-05 09:14:53', NULL),
	(41, 'Jack\'s New England Clam Chowder', 8, '12 - 12 oz cans', 10.00, 20, 1, '2024-12-05 09:14:53', NULL),
	(42, 'Singaporean Hokkien Fried Mee', 5, '32 - 1 kg pkgs.', 14.00, 20, 1, '2024-12-05 09:14:54', NULL),
	(43, 'Ipoh Coffee', 1, '16 - 500 g tins', 46.00, 20, 1, '2024-12-05 09:14:54', NULL),
	(44, 'Gula Malacca', 2, '20 - 2 kg bags', 19.00, 20, 1, '2024-12-05 09:14:55', NULL),
	(45, 'Røgede sild', 8, '1k pkg.', 10.00, 20, 1, '2024-12-05 09:14:56', NULL),
	(46, 'Spegesild', 8, '4 - 450 g glasses', 12.00, 20, 1, '2024-12-05 09:14:57', NULL),
	(47, 'Zaanse koeken', 3, '10 - 4 oz boxes', 10.00, 20, 1, '2024-12-05 09:14:57', NULL),
	(48, 'Chocolade', 3, '10 pkgs.', 13.00, 20, 1, '2024-12-05 09:14:58', NULL),
	(49, 'Maxilaku', 3, '24 - 50 g pkgs.', 20.00, 20, 1, '2024-12-05 09:14:59', NULL),
	(50, 'Valkoinen suklaa', 3, '12 - 100 g bars', 16.00, 20, 1, '2024-12-05 09:14:59', NULL),
	(51, 'Manjimup Dried Apples', 7, '50 - 300 g pkgs.', 53.00, 20, 1, '2024-12-05 09:15:00', NULL),
	(52, 'Filo Mix', 5, '16 - 2 kg boxes', 7.00, 20, 1, '2024-12-05 09:15:01', NULL),
	(53, 'Perth Pasties', 6, '48 pieces', 33.00, 20, 1, '2024-12-05 09:15:02', NULL),
	(54, 'Tourtière', 6, '16 pies', 7.00, 20, 1, '2024-12-05 09:15:02', NULL),
	(55, 'Pâté chinois', 6, '24 boxes x 2 pies', 24.00, 20, 1, '2024-12-05 09:15:04', NULL),
	(56, 'Gnocchi di nonna Alice', 5, '24 - 250 g pkgs.', 38.00, 20, 1, '2024-12-05 09:15:05', NULL),
	(57, 'Ravioli Angelo', 5, '24 - 250 g pkgs.', 20.00, 20, 1, '2024-12-05 09:15:06', NULL),
	(58, 'Escargots de Bourgogne', 8, '24 pieces', 13.00, 20, 1, '2024-12-05 09:15:07', NULL),
	(59, 'Raclette Courdavault', 4, '5 kg pkg.', 55.00, 20, 1, '2024-12-05 09:15:07', NULL),
	(60, 'Camembert Pierrot', 4, '15 - 300 g rounds', 34.00, 20, 1, '2024-12-05 09:15:08', NULL),
	(61, 'Sirop d\'érable', 2, '24 - 500 ml bottles', 29.00, 20, 1, '2024-12-05 09:15:08', NULL),
	(62, 'Tarte au sucre', 3, '48 pies', 49.00, 20, 1, '2024-12-05 09:15:09', NULL),
	(63, 'Vegie-spread', 2, '15 - 625 g jars', 44.00, 20, 1, '2024-12-05 09:15:10', NULL),
	(64, 'Wimmers gute Semmelknödel', 5, '20 bags x 4 pieces', 33.00, 20, 1, '2024-12-05 09:15:11', NULL),
	(65, 'Louisiana Fiery Hot Pepper Sauce', 2, '32 - 8 oz bottles', 21.00, 20, 1, '2024-12-05 09:15:12', NULL),
	(66, 'Louisiana Hot Spiced Okra', 2, '24 - 8 oz jars', 17.00, 20, 1, '2024-12-05 09:15:12', NULL),
	(67, 'Laughing Lumberjack Lager', 1, '24 - 12 oz bottles', 14.00, 20, 1, '2024-12-05 09:15:13', NULL),
	(68, 'Scottish Longbreads', 3, '10 boxes x 8 pieces', 13.00, 20, 1, '2024-12-05 09:15:14', NULL),
	(69, 'Gudbrandsdalsost', 4, '10 kg pkg.', 36.00, 20, 1, '2024-12-05 09:15:14', NULL),
	(70, 'Outback Lager', 1, '24 - 355 ml bottles', 15.00, 20, 1, '2024-12-05 09:15:17', NULL),
	(71, 'Fløtemysost', 4, '10 - 500 g pkgs.', 22.00, 20, 1, '2024-12-05 09:15:17', NULL),
	(72, 'Mozzarella di Giovanni', 4, '24 - 200 g pkgs.', 35.00, 20, 1, '2024-12-05 09:15:18', NULL),
	(73, 'Röd Kaviar', 8, '24 - 150 g jars', 15.00, 20, 1, '2024-12-05 09:15:19', NULL),
	(74, 'Longlife Tofu', 7, '5 kg pkg.', 10.00, 20, 1, '2024-12-05 09:15:20', NULL),
	(75, 'Rhönbräu Klosterbier', 1, '24 - 0.5 l bottles', 8.00, 20, 1, '2024-12-05 09:15:20', NULL),
	(76, 'Lakkalikööri', 1, '500 ml', 18.00, 20, 1, '2024-12-05 09:15:21', NULL),
	(77, 'Original Frankfurter grüne Soße', 2, '12 boxes', 13.00, 20, 1, '2024-12-05 09:15:22', NULL),
	(78, 'Hot Pickled Okra', 2, '24 - 8 Oz Jars', 12.00, 20, 1, '2025-05-08 17:57:58', NULL),
	(79, 'Heinz Cider Vinegar', 2, '24 - 355 ml bottles', 17.00, 20, 1, '2025-05-08 18:06:29', NULL),
	(80, 'Xxxx Gold', 1, '24 - 255 ml bottles', 11.00, 20, 1, '2025-05-08 18:40:41', NULL);

-- Volcando datos para la tabla northwind_db.roles: ~3 rows (aproximadamente)
INSERT IGNORE INTO `roles` (`RoleID`, `RoleName`) VALUES
	(1, 'ADMIN'),
	(2, 'EMPLOYEE'),
	(3, 'CUSTOMER');

-- Volcando datos para la tabla northwind_db.users: ~1 rows (aproximadamente)
INSERT IGNORE INTO `users` (`UserID`, `UserName`, `Email`, `Password`, `CreatedAt`, `UpdatedAt`, `RoleID`) VALUES
	(95, 'off_admin', 'official_admin@outlook.com', '$2a$10$qRVsZfeufYkaicjTrJ.3Gu6HQKMF6KTQwTQCX3bSDwQWNeaKk0FwS', '2025-06-04 10:39:01', '2025-06-05 18:28:46', 1);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
