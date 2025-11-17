-- ============================================================================
-- 4.3. SQL SKRIPT SA TESTNIM PODACIMA
-- ============================================================================
-- Projekat: Škola plesa
-- Autor: Mirko Popović SI 21/21
-- Datum: 13.11.2025
-- ============================================================================

USE `skola_plesa`;

-- ============================================================================
-- UNOS TESTNIH PODATAKA - KORISNICI
-- ============================================================================

INSERT INTO `korisnici` (`id`, `ime`, `prezime`, `email`, `lozinka`, `telefon`, `uloga`, `datum_registracije`, `aktivan`) VALUES
(1, 'Marko', 'Marković', 'marko@gmail.com', 'instruktor123', '0641234567', 'INSTRUKTOR', '2025-01-13 18:52:24', 1),
(2, 'Ana', 'Anić', 'ana@gmail.com', 'ana123', '0642345678', 'UCENIK', '2025-01-13 18:52:24', 1),
(3, 'Petar', 'Petrović', 'petar@gmail.com', 'petar123', '0643456789', 'UCENIK', '2025-01-13 18:52:24', 1);

-- ============================================================================
-- UNOS TESTNIH PODATAKA - RASPORED ČASOVA
-- ============================================================================

INSERT INTO `raspored` (`id`, `datum_kreiranja`, `datum_vreme`, `instruktor_id`, `kreirao_id`, `lokacija`, `maksimalno_polaznika`, `opis`, `tip_casa`, `trajanje_min`) VALUES
(1, '2025-01-13 18:54:30.000000', '2025-01-22 18:00:00.000000', 1, 1, 'Sala 1', 15, 'Početni nivo baleta za sve uzraste', 'balet', 60),
(2, '2025-01-13 19:37:26.000000', '2025-01-20 19:00:00.000000', 1, 1, 'Sala 2', 20, 'Energični hip hop časovi', 'hiphop', 90),
(3, '2025-01-13 20:34:48.000000', '2025-01-25 20:00:00.000000', 1, 1, 'Sala 3', 12, 'Latino ritmovi za sve nivoe', 'latino', 75);

-- ============================================================================
-- UNOS TESTNIH PODATAKA - UPISI NA BALET ČASOVE
-- ============================================================================

INSERT INTO `balet` (`id`, `datum_prijave`, `instruktor_id`, `korisnik_id`, `napomena`) VALUES
(1, '2025-01-13 18:56:00.000000', 1, 2, 'Početnik u baletu'),
(2, '2025-01-13 18:56:15.000000', 1, 3, 'Imam prethodno iskustvo');

-- ============================================================================
-- UNOS TESTNIH PODATAKA - UPISI NA HIPHOP ČASOVE
-- ============================================================================

INSERT INTO `hiphop` (`id`, `datum_prijave`, `instruktor_id`, `korisnik_id`, `napomena`) VALUES
(1, '2025-01-13 19:38:00.000000', 1, 2, 'Volim energične plesove'),
(2, '2025-01-13 19:38:12.000000', 1, 3, NULL);

-- ============================================================================
-- UNOS TESTNIH PODATAKA - UPISI NA LATINO ČASOVE
-- ============================================================================

INSERT INTO `latino` (`id`, `datum_prijave`, `instruktor_id`, `korisnik_id`, `napomena`) VALUES
(1, '2025-01-13 20:35:20.000000', 1, 2, 'Želim da naučim salsu'),
(2, '2025-01-13 20:35:30.000000', 1, 3, 'Plešem bachatu već godinu dana');

-- ============================================================================
-- PODEŠAVANJE AUTO_INCREMENT VREDNOSTI
-- ============================================================================

ALTER TABLE `balet` AUTO_INCREMENT = 3;
ALTER TABLE `hiphop` AUTO_INCREMENT = 3;
ALTER TABLE `korisnici` AUTO_INCREMENT = 4;
ALTER TABLE `latino` AUTO_INCREMENT = 3;
ALTER TABLE `raspored` AUTO_INCREMENT = 4;

COMMIT;

-- ============================================================================
-- KRAJ SKRIPTA
-- ============================================================================

-- ============================================================================
-- NAPOMENE ZA TESTIRANJE
-- ============================================================================

-- Instruktor Marko je kreirao 3 časa:
-- 1. Balet - 22.01.2025 u 18:00 (Sala 1)
-- 2. Hip Hop - 20.01.2025 u 19:00 (Sala 2)
-- 3. Latino - 25.01.2025 u 20:00 (Sala 3)
--
-- Učenici Ana i Petar su prijavljeni na sve tri vrste časova.
-- ============================================================================
