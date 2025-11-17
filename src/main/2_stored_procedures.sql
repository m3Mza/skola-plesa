-- ============================================================================
-- 4.2. SQL SKRIPT SA PROCEDURAMA
-- ============================================================================
-- Projekat: Škola plesa
-- Autor: Mirko Popović SI 21/21
-- Datum: 13.11.2025
-- ============================================================================

USE `skola_plesa`;

DELIMITER $$

-- ============================================================================
-- PROCEDURA: sp_autentifikuj_korisnika
-- Opis: Autentifikacija korisnika po email-u i lozinci
-- Parametri:
--   p_email (IN) - Email adresa korisnika
--   p_lozinka (IN) - Lozinka korisnika
-- Vraća: Sve podatke o korisniku ako je autentifikacija uspešna
-- ============================================================================
CREATE PROCEDURE `sp_autentifikuj_korisnika`(
    IN p_email VARCHAR(255), 
    IN p_lozinka VARCHAR(255)
)
BEGIN
    SELECT * FROM korisnici 
    WHERE email = p_email 
      AND lozinka = p_lozinka 
      AND aktivan = 1;
END$$

-- ============================================================================
-- PROCEDURA: sp_izmeni_cas
-- Opis: Ažuriranje podataka postojećeg časa u rasporedu
-- Parametri:
--   p_id (IN) - ID časa koji se menja
--   p_datum_vreme (IN) - Novo datum i vreme časa
--   p_lokacija (IN) - Nova lokacija
--   p_tip_casa (IN) - Novi tip časa
--   p_trajanje_min (IN) - Novo trajanje u minutima
--   p_maksimalno_polaznika (IN) - Novi maksimalan broj polaznika
--   p_opis (IN) - Novi opis časa
-- ============================================================================
CREATE PROCEDURE `sp_izmeni_cas`(
    IN p_id BIGINT,
    IN p_datum_vreme DATETIME,
    IN p_lokacija VARCHAR(255),
    IN p_tip_casa VARCHAR(20),
    IN p_trajanje_min INT,
    IN p_maksimalno_polaznika INT,
    IN p_opis TEXT
)
BEGIN
    UPDATE raspored
    SET datum_vreme = p_datum_vreme,
        lokacija = p_lokacija,
        tip_casa = p_tip_casa,
        trajanje_min = p_trajanje_min,
        maksimalno_polaznika = p_maksimalno_polaznika,
        opis = p_opis
    WHERE id = p_id;
END$$

-- ============================================================================
-- PROCEDURA: sp_kreiraj_raspored
-- Opis: Kreiranje novog časa u rasporedu
-- Parametri:
--   p_datum_vreme (IN) - Datum i vreme časa
--   p_lokacija (IN) - Lokacija održavanja
--   p_tip_casa (IN) - Tip časa (balet, hiphop, latino)
--   p_trajanje_min (IN) - Trajanje u minutima
--   p_maksimalno_polaznika (IN) - Maksimalan broj polaznika
--   p_opis (IN) - Opis časa
--   p_instruktor_id (IN) - ID instruktora
--   p_kreirao_id (IN) - ID korisnika koji je kreirao čas
-- ============================================================================
CREATE PROCEDURE `sp_kreiraj_raspored`(
    IN p_datum_vreme DATETIME,
    IN p_lokacija VARCHAR(255),
    IN p_tip_casa VARCHAR(20),
    IN p_trajanje_min INT,
    IN p_maksimalno_polaznika INT,
    IN p_opis TEXT,
    IN p_instruktor_id BIGINT,
    IN p_kreirao_id BIGINT
)
BEGIN
    INSERT INTO raspored (
        datum_vreme, 
        lokacija, 
        tip_casa, 
        trajanje_min, 
        maksimalno_polaznika, 
        opis, 
        instruktor_id, 
        kreirao_id, 
        datum_kreiranja
    )
    VALUES (
        p_datum_vreme, 
        p_lokacija, 
        p_tip_casa, 
        p_trajanje_min, 
        p_maksimalno_polaznika, 
        p_opis, 
        p_instruktor_id, 
        p_kreirao_id, 
        NOW()
    );
END$$

-- ============================================================================
-- PROCEDURA: sp_obrisi_raspored
-- Opis: Brisanje časa iz rasporeda po ID-u
-- Parametri:
--   p_id (IN) - ID časa koji se briše
-- ============================================================================
CREATE PROCEDURE `sp_obrisi_raspored`(IN p_id BIGINT)
BEGIN
    DELETE FROM raspored WHERE id = p_id;
END$$

-- ============================================================================
-- PROCEDURA: sp_odjavi_sa_casa
-- Opis: Odjava učenika sa određenog tipa časa
-- Parametri:
--   p_tip_casa (IN) - Tip časa (balet, hiphop, latino)
--   p_korisnik_id (IN) - ID učenika koji se odjavljuje
--   p_instruktor_id (IN) - ID instruktora (opciono, za filtriranje)
-- ============================================================================
CREATE PROCEDURE `sp_odjavi_sa_casa`(
    IN p_tip_casa VARCHAR(20),
    IN p_korisnik_id BIGINT,
    IN p_instruktor_id BIGINT
)
BEGIN
    IF p_tip_casa = 'balet' THEN
        DELETE FROM balet 
        WHERE korisnik_id = p_korisnik_id 
          AND (p_instruktor_id IS NULL OR instruktor_id = p_instruktor_id);
          
    ELSEIF p_tip_casa = 'hiphop' THEN
        DELETE FROM hiphop 
        WHERE korisnik_id = p_korisnik_id 
          AND (p_instruktor_id IS NULL OR instruktor_id = p_instruktor_id);
          
    ELSEIF p_tip_casa = 'latino' THEN
        DELETE FROM latino 
        WHERE korisnik_id = p_korisnik_id 
          AND (p_instruktor_id IS NULL OR instruktor_id = p_instruktor_id);
    END IF;
END$$

-- ============================================================================
-- PROCEDURA: sp_prijavi_na_cas
-- Opis: Prijava učenika na određeni tip časa
-- Parametri:
--   p_tip_casa (IN) - Tip časa (balet, hiphop, latino)
--   p_korisnik_id (IN) - ID učenika koji se prijavljuje
--   p_instruktor_id (IN) - ID instruktora
--   p_napomena (IN) - Opciona napomena
-- ============================================================================
CREATE PROCEDURE `sp_prijavi_na_cas`(
    IN p_tip_casa VARCHAR(20),
    IN p_korisnik_id BIGINT,
    IN p_instruktor_id BIGINT,
    IN p_napomena TEXT
)
BEGIN
    IF p_tip_casa = 'balet' THEN
        INSERT INTO balet (korisnik_id, instruktor_id, napomena, datum_prijave)
        VALUES (p_korisnik_id, p_instruktor_id, p_napomena, NOW());
        
    ELSEIF p_tip_casa = 'hiphop' THEN
        INSERT INTO hiphop (korisnik_id, instruktor_id, napomena, datum_prijave)
        VALUES (p_korisnik_id, p_instruktor_id, p_napomena, NOW());
        
    ELSEIF p_tip_casa = 'latino' THEN
        INSERT INTO latino (korisnik_id, instruktor_id, napomena, datum_prijave)
        VALUES (p_korisnik_id, p_instruktor_id, p_napomena, NOW());
    END IF;
END$$

-- ============================================================================
-- PROCEDURA: sp_statistika_korisnika
-- Opis: Vraća statistiku za određenog korisnika (broj prijavljenih časova)
-- Parametri:
--   p_korisnik_id (IN) - ID korisnika
-- Vraća: Broj časova po tipu (balet, hiphop, latino) i ukupan broj
-- ============================================================================
CREATE PROCEDURE `sp_statistika_korisnika`(IN p_korisnik_id BIGINT)
BEGIN
    SELECT 
        (SELECT COUNT(*) FROM balet WHERE korisnik_id = p_korisnik_id) AS broj_balet,
        (SELECT COUNT(*) FROM hiphop WHERE korisnik_id = p_korisnik_id) AS broj_hiphop,
        (SELECT COUNT(*) FROM latino WHERE korisnik_id = p_korisnik_id) AS broj_latino,
        (
            (SELECT COUNT(*) FROM balet WHERE korisnik_id = p_korisnik_id) +
            (SELECT COUNT(*) FROM hiphop WHERE korisnik_id = p_korisnik_id) +
            (SELECT COUNT(*) FROM latino WHERE korisnik_id = p_korisnik_id)
        ) AS ukupno_casova;
END$$

-- ============================================================================
-- PROCEDURA: sp_prijavi_se_na_cas
-- Opis: Potvrđuje dolazak učenika na čas uz proveru kapaciteta
-- Parametri:
--   p_raspored_id (IN) - ID termina iz rasporeda
--   p_korisnik_id (IN) - ID učenika
--   p_uspeh (OUT) - 1 ako je uspešno, 0 ako nije
--   p_poruka (OUT) - Poruka o rezultatu operacije
-- ============================================================================
DROP PROCEDURE IF EXISTS `sp_prijavi_se_na_cas`$$

CREATE PROCEDURE `sp_prijavi_se_na_cas`(
    IN p_raspored_id BIGINT,
    IN p_korisnik_id BIGINT,
    OUT p_uspeh INT,
    OUT p_poruka VARCHAR(500)
)
BEGIN
    DECLARE v_max_polaznika INT;
    DECLARE v_trenutno_prijavljenih INT;
    DECLARE v_vec_prijavljen INT;
    
    -- Provera da li učenik već postoji u tabeli prijave
    SELECT COUNT(*) INTO v_vec_prijavljen
    FROM prijave
    WHERE raspored_id = p_raspored_id 
      AND korisnik_id = p_korisnik_id;
    
    IF v_vec_prijavljen > 0 THEN
        SET p_uspeh = 0;
        SET p_poruka = 'Već ste prijavljeni na ovaj čas.';
    ELSE
        -- Dohvati maksimalan broj polaznika za ovaj termin
        SELECT maksimalno_polaznika INTO v_max_polaznika
        FROM raspored
        WHERE id = p_raspored_id;
        
        -- Prebroji trenutno prijavljene
        SELECT COUNT(*) INTO v_trenutno_prijavljenih
        FROM prijave
        WHERE raspored_id = p_raspored_id;
        
        -- Proveri da li ima mesta
        IF v_trenutno_prijavljenih >= v_max_polaznika THEN
            SET p_uspeh = 0;
            SET p_poruka = CONCAT('Čas je popunjen. Maksimalan broj polaznika: ', v_max_polaznika);
        ELSE
            -- Prijavi učenika na čas
            INSERT INTO prijave (raspored_id, korisnik_id)
            VALUES (p_raspored_id, p_korisnik_id);
            
            SET p_uspeh = 1;
            SET p_poruka = 'Uspešno ste potvrdili dolazak na čas.';
        END IF;
    END IF;
END$$

-- ============================================================================
-- PROCEDURA: sp_odjavi_se_sa_casa
-- Opis: Otkazuje dolazak učenika na čas
-- Parametri:
--   p_raspored_id (IN) - ID termina iz rasporeda
--   p_korisnik_id (IN) - ID učenika
--   p_uspeh (OUT) - 1 ako je uspešno, 0 ako nije
--   p_poruka (OUT) - Poruka o rezultatu operacije
-- ============================================================================
DROP PROCEDURE IF EXISTS `sp_odjavi_se_sa_casa`$$

CREATE PROCEDURE `sp_odjavi_se_sa_casa`(
    IN p_raspored_id BIGINT,
    IN p_korisnik_id BIGINT,
    OUT p_uspeh INT,
    OUT p_poruka VARCHAR(500)
)
BEGIN
    DECLARE v_broj_obrisanih INT;
    
    -- Obriši prijavu
    DELETE FROM prijave
    WHERE raspored_id = p_raspored_id 
      AND korisnik_id = p_korisnik_id;
    
    -- Proveri koliko redova je obrisano
    SET v_broj_obrisanih = ROW_COUNT();
    
    IF v_broj_obrisanih > 0 THEN
        SET p_uspeh = 1;
        SET p_poruka = 'Uspešno ste otkazali dolazak na čas.';
    ELSE
        SET p_uspeh = 0;
        SET p_poruka = 'Niste bili prijavljeni na ovaj čas.';
    END IF;
END$$

-- ============================================================================
-- PROCEDURA: sp_proveri_prijavu
-- Opis: Proverava da li je učenik prijavljen na određeni čas
-- Parametri:
--   p_raspored_id (IN) - ID termina iz rasporeda
--   p_korisnik_id (IN) - ID učenika
-- Vraća: Podatke o prijavi ako postoji
-- ============================================================================
DROP PROCEDURE IF EXISTS `sp_proveri_prijavu`$$

CREATE PROCEDURE `sp_proveri_prijavu`(
    IN p_raspored_id BIGINT,
    IN p_korisnik_id BIGINT
)
BEGIN
    SELECT 
        p.id,
        p.raspored_id,
        p.korisnik_id,
        p.datum_prijave,
        r.maksimalno_polaznika,
        (SELECT COUNT(*) FROM prijave WHERE raspored_id = p_raspored_id) AS trenutno_prijavljenih
    FROM prijave p
    JOIN raspored r ON p.raspored_id = r.id
    WHERE p.raspored_id = p_raspored_id 
      AND p.korisnik_id = p_korisnik_id;
END$$

DELIMITER ;

-- ============================================================================
-- KRAJ SKRIPTA
-- ============================================================================
