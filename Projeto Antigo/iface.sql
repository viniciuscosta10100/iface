/*
 Navicat Premium Data Transfer

 Source Server         : LocalHost
 Source Server Type    : MySQL
 Source Server Version : 50713
 Source Host           : localhost
 Source Database       : iface

 Target Server Type    : MySQL
 Target Server Version : 50713
 File Encoding         : utf-8

 Date: 08/09/2016 23:17:03 PM
*/

SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `comunidade`
-- ----------------------------
DROP TABLE IF EXISTS `comunidade`;
CREATE TABLE `comunidade` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) DEFAULT NULL,
  `responsavel_id` int(11) DEFAULT NULL,
  `descricao` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `responsavel_id` (`responsavel_id`),
  CONSTRAINT `fk_responsavel` FOREIGN KEY (`responsavel_id`) REFERENCES `usuario` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `comunidade_participante`
-- ----------------------------
DROP TABLE IF EXISTS `comunidade_participante`;
CREATE TABLE `comunidade_participante` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `comunidade_id` int(11) NOT NULL,
  `participante_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `comunidade_id` (`comunidade_id`),
  KEY `participante_id` (`participante_id`),
  CONSTRAINT `fk_comunidade` FOREIGN KEY (`comunidade_id`) REFERENCES `comunidade` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_participante` FOREIGN KEY (`participante_id`) REFERENCES `usuario` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `convite_amizade`
-- ----------------------------
DROP TABLE IF EXISTS `convite_amizade`;
CREATE TABLE `convite_amizade` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `remetente_id` int(11) NOT NULL,
  `destinatario_id` int(11) NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `remetente_id` (`remetente_id`),
  KEY `destinatario_id` (`destinatario_id`),
  CONSTRAINT `fk_amigo_destinatario` FOREIGN KEY (`destinatario_id`) REFERENCES `usuario` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_amigo_remetente` FOREIGN KEY (`remetente_id`) REFERENCES `usuario` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `estado_civil`
-- ----------------------------
DROP TABLE IF EXISTS `estado_civil`;
CREATE TABLE `estado_civil` (
  `id` int(11) NOT NULL,
  `nome` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `estado_civil`
-- ----------------------------
BEGIN;
INSERT INTO `estado_civil` VALUES ('1', 'Solteiro'), ('2', 'Em um relacionamento sério'), ('3', 'Noivo'), ('4', 'Casado'), ('5', 'Divorciado'), ('6', 'Viúvo');
COMMIT;

-- ----------------------------
--  Table structure for `mensagem`
-- ----------------------------
DROP TABLE IF EXISTS `mensagem`;
CREATE TABLE `mensagem` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `texto` text,
  `remetente_id` int(11) DEFAULT NULL,
  `destinatario_id` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `remetente_id` (`remetente_id`),
  KEY `destinatario_id` (`destinatario_id`),
  CONSTRAINT `fk_destinatario` FOREIGN KEY (`destinatario_id`) REFERENCES `usuario` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_remetente` FOREIGN KEY (`remetente_id`) REFERENCES `usuario` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `mensagem_comunidade`
-- ----------------------------
DROP TABLE IF EXISTS `mensagem_comunidade`;
CREATE TABLE `mensagem_comunidade` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `texto` text,
  `remetente_id` int(11) DEFAULT NULL,
  `comunidade_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `remetente_id` (`remetente_id`),
  KEY `comunidade_id` (`comunidade_id`),
  CONSTRAINT `fk_comunidade_mensagem` FOREIGN KEY (`comunidade_id`) REFERENCES `comunidade` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_remetente_mensagem_comunidade` FOREIGN KEY (`remetente_id`) REFERENCES `usuario` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
--  Table structure for `sexo`
-- ----------------------------
DROP TABLE IF EXISTS `sexo`;
CREATE TABLE `sexo` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nome` varchar(255) NOT NULL,
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
  `nome` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `senha` varchar(255) NOT NULL,
  `sexo_id` int(11) DEFAULT NULL,
  `data_nascimento` date DEFAULT NULL,
  `estado_civil_id` int(11) DEFAULT NULL,
  `mensagem_perfil` text,
  PRIMARY KEY (`id`),
  KEY `sexo_id` (`sexo_id`),
  KEY `estado_civil_id` (`estado_civil_id`),
  CONSTRAINT `fk_estado_civil` FOREIGN KEY (`estado_civil_id`) REFERENCES `estado_civil` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_sexo` FOREIGN KEY (`sexo_id`) REFERENCES `sexo` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
--  Records of `usuario`
-- ----------------------------
BEGIN;
INSERT INTO `usuario` VALUES ('1', 'Vinicius Costa', 'user1@iface.com', '123', '1', '1995-05-21', '1', 'Teste'), ('2', 'Baldoino Fonseca', 'user2@iface.com', '123', '1', '1985-01-01', '4', 'Teste2');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
