-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: forum
-- ------------------------------------------------------
-- Server version	8.0.39

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `forum_follow`
--

DROP TABLE IF EXISTS `forum_follow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `forum_follow` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) NOT NULL,
  `forum_id` int NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_follow` (`user_id`,`forum_id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `forum_follow`
--

LOCK TABLES `forum_follow` WRITE;
/*!40000 ALTER TABLE `forum_follow` DISABLE KEYS */;
INSERT INTO `forum_follow` VALUES (2,'14842714676',2);
/*!40000 ALTER TABLE `forum_follow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `forums`
--

DROP TABLE IF EXISTS `forums`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `forums` (
  `id` int NOT NULL AUTO_INCREMENT,
  `userID` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `introduction` text,
  `follow_count` int DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `forums`
--

LOCK TABLES `forums` WRITE;
/*!40000 ALTER TABLE `forums` DISABLE KEYS */;
INSERT INTO `forums` VALUES (1,'14842714676','香菇青菜','新鲜的芦笋，撒上一些五香粉，顶部覆上焦糖化的柚子，再挤上满满的奶油',0),(2,'14842714676','醋溜土豆丝','甲鱼在富含五香粉和芝麻菜的酱汁中文火慢炖，配以奶油般顺滑的芜菁。',1);
/*!40000 ALTER TABLE `forums` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `id` int NOT NULL AUTO_INCREMENT,
  `user_id` varchar(255) NOT NULL,
  `content` varchar(255) NOT NULL,
  `Read` tinyint(1) DEFAULT '0',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
INSERT INTO `notifications` VALUES (1,'14842714676','圆发业有下式。律过一那作进。标或铁特场部队人身。\n劳才造同马转派。个能做天。东素年东何。\n资热色值。性作内多联按较级油节。处海少对计结面。',0,'2025-05-30 14:47:05'),(2,'14842714676','物十支受需求。定程支定进发。些更必外一许保种音并。\n新正中局离元增由志指。性称许。还想太研。\n五红准。受系包。时加以它维。\n回当必状其。展以元支院。设级清样基叫老酸间流。\n队先压九小拉原而于保。门空世解话多不之。现快无备治。\n非持而林积没。收包想。关强因斯置期具内很解。',1,'2025-05-30 14:47:26'),(3,'14842714676','认到温二单近门却。式百们一。交京别教装向经。\n数品图没火正六出各。结加复断。切斗影示列统。\n员算千工变路风白。确积边支用。文比面基转必省数。\n不南中维所性目体由多。院车素光百近。调确报参业任下些。\n何应电这第图术事。生量感严便切高无小。以局向越明必门。',0,'2025-05-30 14:47:36'),(4,'14842714676','论则开基。委由命计务布。写花目能观其。\n却断布儿人出好。装动决于很米。写难两。\n内备经多持这公民将。候已算华。程清听律。\n存命年。最列特片往。型总众切候问省。\n于流为矿认使并子表。子常广放两之美又们单。之能本明厂。\n别地科生此。统般作切林。先石南八约将头把矿。',0,'2025-05-30 14:47:39'),(5,'14842714676','周但标地科论。快在改。八车成给土指认好转生。\n飞音看质来期自化。界一术强。议存难与太马应。\n片些解易温段。北少接证。先么老什京。',0,'2025-05-30 14:47:42');
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `UserId` varchar(255) NOT NULL,
  `Username` varchar(255) NOT NULL,
  `Password` varchar(255) NOT NULL,
  `Level` int DEFAULT '0',
  `Email` varchar(255) DEFAULT NULL,
  `Admin` int DEFAULT NULL,
  `avatarPath` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`UserId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES ('13692916472','negtue_ire','rIPNqUr88mQzMfm',0,NULL,0,NULL),('14842714676','k5qkfk_hkg','OK48HLGEytilxyu',3,'freg6n_sl075@163.com',1,'uploads\\avatars\\14842714676_1748588860140_微信图片_20250217204130.jpg'),('15875713043','o97kgs76','x5koDE4hOpkRyx7',0,NULL,0,NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'forum'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-30 23:16:53
