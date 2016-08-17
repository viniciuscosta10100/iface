/*
 Navicat Premium Data Transfer

 Source Server         : LocalHost
 Source Server Type    : MySQL
 Source Server Version : 50713
 Source Host           : localhost
 Source Database       : db_iface

 Target Server Type    : MySQL
 Target Server Version : 50713
 File Encoding         : utf-8

 Date: 08/16/2016 23:25:07 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `amizade`
-- ----------------------------
DROP TABLE IF EXISTS `amizade`;
CREATE TABLE `amizade` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status` int(11) NOT NULL,
  `usuario1_id` int(11) DEFAULT NULL,
  `usuario2_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKCB3D412564914C18` (`usuario1_id`),
  KEY `FKCB3D41256491C077` (`usuario2_id`),
  CONSTRAINT `FKCB3D412564914C18` FOREIGN KEY (`usuario1_id`) REFERENCES `usuario` (`id`),
  CONSTRAINT `FKCB3D41256491C077` FOREIGN KEY (`usuario2_id`) REFERENCES `usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `comunidade`
-- ----------------------------
DROP TABLE IF EXISTS `comunidade`;
CREATE TABLE `comunidade` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `descricao` varchar(255) DEFAULT NULL,
  `nome` varchar(255) DEFAULT NULL,
  `responsavel_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKEBD7B60DA09FC37B` (`responsavel_id`),
  CONSTRAINT `FKEBD7B60DA09FC37B` FOREIGN KEY (`responsavel_id`) REFERENCES `usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `estado_civil`
-- ----------------------------
DROP TABLE IF EXISTS `estado_civil`;
CREATE TABLE `estado_civil` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `estado_civil`
-- ----------------------------
BEGIN;
INSERT INTO `estado_civil` VALUES ('1', 'Solteiro'), ('2', 'Casado'), ('3', 'Divorciado'), ('4', 'Viúvo');
COMMIT;

-- ----------------------------
--  Table structure for `mensagem`
-- ----------------------------
DROP TABLE IF EXISTS `mensagem`;
CREATE TABLE `mensagem` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status` int(11) NOT NULL,
  `texto` varchar(255) DEFAULT NULL,
  `destinatario_id` int(11) DEFAULT NULL,
  `remetente_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKDBECC62B944DD3B8` (`remetente_id`),
  KEY `FKDBECC62B2831964A` (`destinatario_id`),
  CONSTRAINT `FKDBECC62B2831964A` FOREIGN KEY (`destinatario_id`) REFERENCES `usuario` (`id`),
  CONSTRAINT `FKDBECC62B944DD3B8` FOREIGN KEY (`remetente_id`) REFERENCES `usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `mensagem_comunidade`
-- ----------------------------
DROP TABLE IF EXISTS `mensagem_comunidade`;
CREATE TABLE `mensagem_comunidade` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `texto` varchar(255) DEFAULT NULL,
  `comunidade_id` int(11) DEFAULT NULL,
  `remetente_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK96084CA1103610E7` (`comunidade_id`),
  KEY `FK96084CA1944DD3B8` (`remetente_id`),
  CONSTRAINT `FK96084CA1103610E7` FOREIGN KEY (`comunidade_id`) REFERENCES `comunidade` (`id`),
  CONSTRAINT `FK96084CA1944DD3B8` FOREIGN KEY (`remetente_id`) REFERENCES `usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `sexo`
-- ----------------------------
DROP TABLE IF EXISTS `sexo`;
CREATE TABLE `sexo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `sexo`
-- ----------------------------
BEGIN;
INSERT INTO `sexo` VALUES ('1', 'Masculino'), ('2', 'Feminino');
COMMIT;

-- ----------------------------
--  Table structure for `usuario`
-- ----------------------------
DROP TABLE IF EXISTS `usuario`;
CREATE TABLE `usuario` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `data_nascimento` date DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `mensagem_perfil` varchar(255) DEFAULT NULL,
  `nome` varchar(255) DEFAULT NULL,
  `senha` varchar(255) DEFAULT NULL,
  `estado_civil_id` int(11) DEFAULT NULL,
  `sexo_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKF814F32EAB134527` (`sexo_id`),
  KEY `FKF814F32E5B908D20` (`estado_civil_id`),
  CONSTRAINT `FKF814F32E5B908D20` FOREIGN KEY (`estado_civil_id`) REFERENCES `estado_civil` (`id`),
  CONSTRAINT `FKF814F32EAB134527` FOREIGN KEY (`sexo_id`) REFERENCES `sexo` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `usuario`
-- ----------------------------
BEGIN;
INSERT INTO `usuario` VALUES ('1', '1995-05-21', 'mvinicius.infor@gmail.com', 'Teste 123', 'Vinicius Costa', '123', '1', '1');
COMMIT;

-- ----------------------------
--  Table structure for `usuario_comunidade`
-- ----------------------------
DROP TABLE IF EXISTS `usuario_comunidade`;
CREATE TABLE `usuario_comunidade` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `comunidade_id` int(11) DEFAULT NULL,
  `usuario_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK2206EFBEC1866FCD` (`usuario_id`),
  KEY `FK2206EFBE103610E7` (`comunidade_id`),
  CONSTRAINT `FK2206EFBE103610E7` FOREIGN KEY (`comunidade_id`) REFERENCES `comunidade` (`id`),
  CONSTRAINT `FK2206EFBEC1866FCD` FOREIGN KEY (`usuario_id`) REFERENCES `usuario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

SET FOREIGN_KEY_CHECKS = 1;
