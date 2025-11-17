-- ============================================================================
-- 4.1. SQL SKRIPT ZA KREIRANJE BAZE PODATAKA, TABELA I RELACIJA
-- ============================================================================
-- Projekat: Škola plesa
-- Autor: Mirko Popović SI 21/21
-- Datum: 13.11.2025
-- ============================================================================

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

-- ============================================================================
-- KREIRANJE BAZE PODATAKA
-- ============================================================================

CREATE DATABASE IF NOT EXISTS `skola_plesa` 
DEFAULT CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE `skola_plesa`;

-- ============================================================================
-- KREIRANJE TABELA
-- ============================================================================

-- ----------------------------------------------------------------------------
-- Tabela: korisnici
-- Opis: Čuva podatke o svim korisnicima sistema (učenici i instruktori)
-- ----------------------------------------------------------------------------
CREATE TABLE `korisnici` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `ime` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `prezime` varchar(100) COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `lozinka` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL,
  `telefon` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `uloga` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'ucenik ili instruktor',
  `datum_registracije` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `aktivan` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  KEY `idx_email` (`email`),
  KEY `idx_uloga` (`uloga`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------------------------------------------------------
-- Tabela: raspored
-- Opis: Čuva raspored časova koje kreiraju instruktori
-- ----------------------------------------------------------------------------
CREATE TABLE `raspored` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `datum_kreiranja` datetime(6) DEFAULT NULL,
  `datum_vreme` datetime(6) DEFAULT NULL,
  `instruktor_id` bigint(20) DEFAULT NULL,
  `kreirao_id` bigint(20) DEFAULT NULL,
  `lokacija` varchar(255) DEFAULT NULL,
  `maksimalno_polaznika` int(11) DEFAULT NULL,
  `opis` text,
  `tip_casa` varchar(20) DEFAULT NULL COMMENT 'balet, hiphop, latino',
  `trajanje_min` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------------------------------------------------------
-- Tabela: balet
-- Opis: Čuva podatke o učenicima prijavljenim na balet časove
-- ----------------------------------------------------------------------------
CREATE TABLE `balet` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `datum_prijave` datetime(6) DEFAULT NULL,
  `instruktor_id` bigint(20) DEFAULT NULL,
  `korisnik_id` bigint(20) DEFAULT NULL,
  `napomena` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------------------------------------------------------
-- Tabela: hiphop
-- Opis: Čuva podatke o učenicima prijavljenim na hiphop časove
-- ----------------------------------------------------------------------------
CREATE TABLE `hiphop` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `datum_prijave` datetime(6) DEFAULT NULL,
  `instruktor_id` bigint(20) DEFAULT NULL,
  `korisnik_id` bigint(20) DEFAULT NULL,
  `napomena` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------------------------------------------------------
-- Tabela: latino
-- Opis: Čuva podatke o učenicima prijavljenim na latino časove
-- ----------------------------------------------------------------------------
CREATE TABLE `latino` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `datum_prijave` datetime(6) DEFAULT NULL,
  `instruktor_id` bigint(20) DEFAULT NULL,
  `korisnik_id` bigint(20) DEFAULT NULL,
  `napomena` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------------------------------------------------------
-- Tabela: prijave
-- Opis: Evidencija potvrđenih dolazaka učenika na časove sa provjerom kapaciteta
-- ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS `prijave` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `raspored_id` bigint(20) NOT NULL COMMENT 'Referenca na termin iz rasporeda',
  `korisnik_id` bigint(20) NOT NULL COMMENT 'Referenca na učenika koji se prijavio',
  `datum_prijave` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT 'Kada je učenik potvrdio dolazak',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_prijava_jednom` (`raspored_id`, `korisnik_id`) COMMENT 'Učenik se može prijaviti samo jednom na isti termin',
  KEY `idx_raspored` (`raspored_id`),
  KEY `idx_korisnik` (`korisnik_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Evidencija potvrđenih dolazaka učenika na časove';

-- ============================================================================
-- KREIRANJE VIEW-a (POGLEDA)
-- ============================================================================

-- ----------------------------------------------------------------------------
-- View: v_korisnici_sa_casovima
-- Opis: Prikazuje učenike sa brojem časova na koje su prijavljeni
-- ----------------------------------------------------------------------------
CREATE OR REPLACE VIEW `v_korisnici_sa_casovima` AS 
SELECT 
    `k`.`id` AS `id`, 
    `k`.`ime` AS `ime`, 
    `k`.`prezime` AS `prezime`, 
    `k`.`email` AS `email`, 
    `k`.`uloga` AS `uloga`, 
    `k`.`aktivan` AS `aktivan`, 
    COALESCE(`b_count`.`broj_balet`, 0) AS `broj_balet_casova`, 
    COALESCE(`h_count`.`broj_hiphop`, 0) AS `broj_hiphop_casova`, 
    COALESCE(`l_count`.`broj_latino`, 0) AS `broj_latino_casova`, 
    (
        COALESCE(`b_count`.`broj_balet`, 0) + 
        COALESCE(`h_count`.`broj_hiphop`, 0) + 
        COALESCE(`l_count`.`broj_latino`, 0)
    ) AS `ukupno_casova` 
FROM `korisnici` `k` 
LEFT JOIN (
    SELECT `korisnik_id`, COUNT(*) AS `broj_balet` 
    FROM `balet` 
    GROUP BY `korisnik_id`
) `b_count` ON `k`.`id` = `b_count`.`korisnik_id`
LEFT JOIN (
    SELECT `korisnik_id`, COUNT(*) AS `broj_hiphop` 
    FROM `hiphop` 
    GROUP BY `korisnik_id`
) `h_count` ON `k`.`id` = `h_count`.`korisnik_id`
LEFT JOIN (
    SELECT `korisnik_id`, COUNT(*) AS `broj_latino` 
    FROM `latino` 
    GROUP BY `korisnik_id`
) `l_count` ON `k`.`id` = `l_count`.`korisnik_id`
WHERE `k`.`uloga` = 'ucenik';

-- ----------------------------------------------------------------------------
-- View: v_raspored_sa_instruktorima
-- Opis: Prikazuje raspored sa podacima o instruktorima
-- ----------------------------------------------------------------------------
CREATE OR REPLACE VIEW `v_raspored_sa_instruktorima` AS 
SELECT 
    `r`.`id` AS `id`, 
    `r`.`tip_casa` AS `tip_casa`, 
    `r`.`datum_vreme` AS `datum_vreme`, 
    `r`.`trajanje_min` AS `trajanje_min`, 
    `r`.`lokacija` AS `lokacija`, 
    `r`.`maksimalno_polaznika` AS `maksimalno_polaznika`, 
    `r`.`opis` AS `opis`, 
    CONCAT(`i`.`ime`, ' ', `i`.`prezime`) AS `instruktor_ime`, 
    `i`.`email` AS `instruktor_email`, 
    `i`.`telefon` AS `instruktor_telefon` 
FROM `raspored` `r` 
JOIN `korisnici` `i` ON `r`.`instruktor_id` = `i`.`id` 
WHERE `r`.`datum_vreme` >= NOW() 
ORDER BY `r`.`datum_vreme` ASC;

-- ----------------------------------------------------------------------------
-- View: v_statistika_casova
-- Opis: Statistika broja polaznika i instruktora po tipu časa
-- ----------------------------------------------------------------------------
CREATE OR REPLACE VIEW `v_statistika_casova` AS 
SELECT 'balet' AS `tip_casa`, COUNT(*) AS `broj_polaznika`, COUNT(DISTINCT `instruktor_id`) AS `broj_instruktora` FROM `balet`
UNION ALL
SELECT 'hiphop' AS `tip_casa`, COUNT(*) AS `broj_polaznika`, COUNT(DISTINCT `instruktor_id`) AS `broj_instruktora` FROM `hiphop`
UNION ALL
SELECT 'latino' AS `tip_casa`, COUNT(*) AS `broj_polaznika`, COUNT(DISTINCT `instruktor_id`) AS `broj_instruktora` FROM `latino`;

-- ----------------------------------------------------------------------------
-- View: v_sve_prijave
-- Opis: Konsolidovani prikaz svih prijava na časove
-- ----------------------------------------------------------------------------
CREATE OR REPLACE VIEW `v_sve_prijave` AS 
SELECT 
    `b`.`id` AS `prijava_id`, 
    'balet' AS `tip_casa`, 
    CONCAT(`k`.`ime`, ' ', `k`.`prezime`) AS `ucenik_ime`, 
    `k`.`email` AS `ucenik_email`, 
    CONCAT(`i`.`ime`, ' ', `i`.`prezime`) AS `instruktor_ime`, 
    `b`.`datum_prijave` AS `datum_prijave`, 
    `b`.`napomena` AS `napomena` 
FROM `balet` `b` 
JOIN `korisnici` `k` ON `b`.`korisnik_id` = `k`.`id` 
JOIN `korisnici` `i` ON `b`.`instruktor_id` = `i`.`id`
UNION ALL
SELECT 
    `h`.`id` AS `prijava_id`, 
    'hiphop' AS `tip_casa`, 
    CONCAT(`k`.`ime`, ' ', `k`.`prezime`) AS `ucenik_ime`, 
    `k`.`email` AS `ucenik_email`, 
    CONCAT(`i`.`ime`, ' ', `i`.`prezime`) AS `instruktor_ime`, 
    `h`.`datum_prijave` AS `datum_prijave`, 
    `h`.`napomena` AS `napomena` 
FROM `hiphop` `h` 
JOIN `korisnici` `k` ON `h`.`korisnik_id` = `k`.`id` 
JOIN `korisnici` `i` ON `h`.`instruktor_id` = `i`.`id`
UNION ALL
SELECT 
    `l`.`id` AS `prijava_id`, 
    'latino' AS `tip_casa`, 
    CONCAT(`k`.`ime`, ' ', `k`.`prezime`) AS `ucenik_ime`, 
    `k`.`email` AS `ucenik_email`, 
    CONCAT(`i`.`ime`, ' ', `i`.`prezime`) AS `instruktor_ime`, 
    `l`.`datum_prijave` AS `datum_prijave`, 
    `l`.`napomena` AS `napomena` 
FROM `latino` `l` 
JOIN `korisnici` `k` ON `l`.`korisnik_id` = `k`.`id` 
JOIN `korisnici` `i` ON `l`.`instruktor_id` = `i`.`id`;

-- ============================================================================
-- FOREIGN KEY CONSTRAINTS (RELACIONE VEZE)
-- ============================================================================

-- FK prema tabeli raspored (CASCADE briše prijave kad se obriše termin)
ALTER TABLE `prijave`
ADD CONSTRAINT `fk_prijave_raspored`
FOREIGN KEY (`raspored_id`) 
REFERENCES `raspored`(`id`)
ON DELETE CASCADE
ON UPDATE CASCADE;

-- FK prema tabeli korisnici (CASCADE briše prijave kad se obriše korisnik)
ALTER TABLE `prijave`
ADD CONSTRAINT `fk_prijave_korisnik`
FOREIGN KEY (`korisnik_id`) 
REFERENCES `korisnici`(`id`)
ON DELETE CASCADE
ON UPDATE CASCADE;

COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

-- ============================================================================
-- KRAJ SKRIPTA
-- ============================================================================
