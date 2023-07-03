-- MySQL dump 10.13  Distrib 8.0.32, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: beatcode_mysql
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `ip_auth`
--

DROP TABLE IF EXISTS `ip_auth`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ip_auth` (
  `ip_addr` varbinary(16) NOT NULL COMMENT '客户机地址，多个地址可以登录同一个用户。支持IPv6，16byte',
  `last_login` bigint NOT NULL COMMENT '上次登录的时间，unix时间戳',
  `last_fresh` bigint NOT NULL COMMENT '上次发送请求的时间，如果logout则改成1970.1.1',
  `user_id` int NOT NULL COMMENT '在这个ip上登录的具体用户id',
  PRIMARY KEY (`ip_addr`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COMMENT='对ip授权，登录后就不需要再额外携带用户信息';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ip_auth`
--

LOCK TABLES `ip_auth` WRITE;
/*!40000 ALTER TABLE `ip_auth` DISABLE KEYS */;
INSERT INTO `ip_auth` (`ip_addr`, `last_login`, `last_fresh`, `user_id`) VALUES (_binary '\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0',1688375940326,0,2);
/*!40000 ALTER TABLE `ip_auth` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-07-03 17:23:12
