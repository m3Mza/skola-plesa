-- SQL skripta za kreiranje tabela u MySQL bazi skola_plesa

USE skola_plesa;

-- =====================================================
-- TABELA: korisnici (sa ulogama: ucenik, instruktor)
-- =====================================================
CREATE TABLE IF NOT EXISTS korisnici (
    id INT AUTO_INCREMENT PRIMARY KEY,
    ime VARCHAR(100) NOT NULL,
    prezime VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    lozinka VARCHAR(255) NOT NULL,
    telefon VARCHAR(20),
    uloga ENUM('ucenik', 'instruktor') DEFAULT 'ucenik' NOT NULL,
    datum_registracije TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    aktivan BOOLEAN DEFAULT TRUE,
    INDEX idx_email (email),
    INDEX idx_uloga (uloga)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Test korisnici
INSERT INTO korisnici (ime, prezime, email, lozinka, telefon, uloga) 
VALUES 
    ('Mirko', 'Popović', 'mirko@test.com', 'test123', '+381601234567', 'ucenik'),
    ('Bane', 'Benić', 'bane.instruktor@skola.com', 'instruktor123', '+381601111111', 'instruktor');

-- =====================================================
-- TABELA: balet (prijave za časove baleta)
-- =====================================================
CREATE TABLE IF NOT EXISTS balet (
    id INT AUTO_INCREMENT PRIMARY KEY,
    korisnik_id INT NOT NULL,
    instruktor_id INT NOT NULL,
    datum_prijave TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    napomena TEXT,
    FOREIGN KEY (korisnik_id) REFERENCES korisnici(id) ON DELETE CASCADE,
    FOREIGN KEY (instruktor_id) REFERENCES korisnici(id) ON DELETE CASCADE,
    INDEX idx_korisnik (korisnik_id),
    INDEX idx_instruktor (instruktor_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABELA: hiphop (prijave za časove hip-hopa/modernog plesa)
-- =====================================================
CREATE TABLE IF NOT EXISTS hiphop (
    id INT AUTO_INCREMENT PRIMARY KEY,
    korisnik_id INT NOT NULL,
    instruktor_id INT NOT NULL,
    datum_prijave TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    napomena TEXT,
    FOREIGN KEY (korisnik_id) REFERENCES korisnici(id) ON DELETE CASCADE,
    FOREIGN KEY (instruktor_id) REFERENCES korisnici(id) ON DELETE CASCADE,
    INDEX idx_korisnik (korisnik_id),
    INDEX idx_instruktor (instruktor_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABELA: latino (prijave za časove latino plesova)
-- =====================================================
CREATE TABLE IF NOT EXISTS latino (
    id INT AUTO_INCREMENT PRIMARY KEY,
    korisnik_id INT NOT NULL,
    instruktor_id INT NOT NULL,
    datum_prijave TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    napomena TEXT,
    FOREIGN KEY (korisnik_id) REFERENCES korisnici(id) ON DELETE CASCADE,
    FOREIGN KEY (instruktor_id) REFERENCES korisnici(id) ON DELETE CASCADE,
    INDEX idx_korisnik (korisnik_id),
    INDEX idx_instruktor (instruktor_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- =====================================================
-- TABELA: raspored (kalendar časova)
-- =====================================================
CREATE TABLE IF NOT EXISTS raspored (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tip_casa ENUM('balet', 'hiphop', 'latino') NOT NULL,
    instruktor_id INT NOT NULL,
    datum_vreme DATETIME NOT NULL,
    trajanje_min INT DEFAULT 60,
    lokacija VARCHAR(255),
    maksimalno_polaznika INT DEFAULT 15,
    opis TEXT,
    kreirao_id INT NOT NULL,
    datum_kreiranja TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (instruktor_id) REFERENCES korisnici(id) ON DELETE CASCADE,
    FOREIGN KEY (kreirao_id) REFERENCES korisnici(id) ON DELETE CASCADE,
    INDEX idx_datum (datum_vreme),
    INDEX idx_tip (tip_casa),
    INDEX idx_instruktor (instruktor_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Primer unosa rasporeda
INSERT INTO raspored (tip_casa, instruktor_id, datum_vreme, trajanje_min, lokacija, opis, kreirao_id)
VALUES 
    ('balet', 2, '2025-11-10 10:00:00', 60, 'Sala 1', 'Balet za početnike - jutarnji termin', 2),
    ('hiphop', 2, '2025-11-10 14:00:00', 75, 'Sala 2', 'Moderni ples i hip-hop tehnike', 2),
    ('latino', 2, '2025-11-11 18:00:00', 60, 'Sala 1', 'Latino plesovi u paru', 2);

-- =====================================================
-- POGLEDI (VIEWS)
-- =====================================================

-- Pogled: Korisnici sa brojem prijavljenih časova
DROP VIEW IF EXISTS v_korisnici_sa_casovima;
CREATE VIEW v_korisnici_sa_casovima AS
SELECT 
    k.id,
    k.ime,
    k.prezime,
    k.email,
    k.uloga,
    k.aktivan,
    COALESCE(b_count.broj_balet, 0) AS broj_balet_casova,
    COALESCE(h_count.broj_hiphop, 0) AS broj_hiphop_casova,
    COALESCE(l_count.broj_latino, 0) AS broj_latino_casova,
    COALESCE(b_count.broj_balet, 0) + COALESCE(h_count.broj_hiphop, 0) + COALESCE(l_count.broj_latino, 0) AS ukupno_casova
FROM korisnici k
LEFT JOIN (SELECT korisnik_id, COUNT(*) AS broj_balet FROM balet GROUP BY korisnik_id) b_count 
    ON k.id = b_count.korisnik_id
LEFT JOIN (SELECT korisnik_id, COUNT(*) AS broj_hiphop FROM hiphop GROUP BY korisnik_id) h_count 
    ON k.id = h_count.korisnik_id
LEFT JOIN (SELECT korisnik_id, COUNT(*) AS broj_latino FROM latino GROUP BY korisnik_id) l_count 
    ON k.id = l_count.korisnik_id
WHERE k.uloga = 'ucenik';

-- Pogled: Raspored sa imenima instruktora
DROP VIEW IF EXISTS v_raspored_sa_instruktorima;
CREATE VIEW v_raspored_sa_instruktorima AS
SELECT 
    r.id,
    r.tip_casa,
    r.datum_vreme,
    r.trajanje_min,
    r.lokacija,
    r.maksimalno_polaznika,
    r.opis,
    CONCAT(i.ime, ' ', i.prezime) AS instruktor_ime,
    i.email AS instruktor_email,
    i.telefon AS instruktor_telefon
FROM raspored r
INNER JOIN korisnici i ON r.instruktor_id = i.id
WHERE r.datum_vreme >= NOW()
ORDER BY r.datum_vreme ASC;

-- Pogled: Sve prijave na časove (kombinovano)
DROP VIEW IF EXISTS v_sve_prijave;
CREATE VIEW v_sve_prijave AS
SELECT 
    b.id AS prijava_id,
    'balet' AS tip_casa,
    CONCAT(k.ime, ' ', k.prezime) AS ucenik_ime,
    k.email AS ucenik_email,
    CONCAT(i.ime, ' ', i.prezime) AS instruktor_ime,
    b.datum_prijave,
    b.napomena
FROM balet b
INNER JOIN korisnici k ON b.korisnik_id = k.id
INNER JOIN korisnici i ON b.instruktor_id = i.id
UNION ALL
SELECT 
    h.id AS prijava_id,
    'hiphop' AS tip_casa,
    CONCAT(k.ime, ' ', k.prezime) AS ucenik_ime,
    k.email AS ucenik_email,
    CONCAT(i.ime, ' ', i.prezime) AS instruktor_ime,
    h.datum_prijave,
    h.napomena
FROM hiphop h
INNER JOIN korisnici k ON h.korisnik_id = k.id
INNER JOIN korisnici i ON h.instruktor_id = i.id
UNION ALL
SELECT 
    l.id AS prijava_id,
    'latino' AS tip_casa,
    CONCAT(k.ime, ' ', k.prezime) AS ucenik_ime,
    k.email AS ucenik_email,
    CONCAT(i.ime, ' ', i.prezime) AS instruktor_ime,
    l.datum_prijave,
    l.napomena
FROM latino l
INNER JOIN korisnici k ON l.korisnik_id = k.id
INNER JOIN korisnici i ON l.instruktor_id = i.id;

-- Pogled: Statistika po tipovima časova
DROP VIEW IF EXISTS v_statistika_casova;
CREATE VIEW v_statistika_casova AS
SELECT 
    'balet' AS tip_casa,
    COUNT(*) AS broj_polaznika,
    COUNT(DISTINCT instruktor_id) AS broj_instruktora
FROM balet
UNION ALL
SELECT 
    'hiphop' AS tip_casa,
    COUNT(*) AS broj_polaznika,
    COUNT(DISTINCT instruktor_id) AS broj_instruktora
FROM hiphop
UNION ALL
SELECT 
    'latino' AS tip_casa,
    COUNT(*) AS broj_polaznika,
    COUNT(DISTINCT instruktor_id) AS broj_instruktora
FROM latino;

-- =====================================================
-- STORED PROCEDURES
-- =====================================================

-- Procedura: Prijava korisnika na čas
DROP PROCEDURE IF EXISTS sp_prijavi_na_cas;
DELIMITER //
CREATE PROCEDURE sp_prijavi_na_cas(
    IN p_korisnik_id INT,
    IN p_tip_casa VARCHAR(20),
    IN p_instruktor_id INT,
    OUT p_uspeh BOOLEAN,
    OUT p_poruka VARCHAR(255)
)
BEGIN
    DECLARE v_uloga VARCHAR(20);
    DECLARE v_vec_prijavljen INT;
    
    -- Proveri da li korisnik postoji i da li je učenik
    SELECT uloga INTO v_uloga FROM korisnici WHERE id = p_korisnik_id;
    
    IF v_uloga IS NULL THEN
        SET p_uspeh = FALSE;
        SET p_poruka = 'Korisnik ne postoji';
    ELSEIF v_uloga != 'ucenik' THEN
        SET p_uspeh = FALSE;
        SET p_poruka = 'Samo učenici mogu da se prijavljuju na časove';
    ELSE
        -- Proveri da li je već prijavljen
        CASE p_tip_casa
            WHEN 'balet' THEN
                SELECT COUNT(*) INTO v_vec_prijavljen FROM balet WHERE korisnik_id = p_korisnik_id;
                IF v_vec_prijavljen > 0 THEN
                    SET p_uspeh = FALSE;
                    SET p_poruka = 'Već ste prijavljeni na balet časove';
                ELSE
                    INSERT INTO balet (korisnik_id, instruktor_id) VALUES (p_korisnik_id, p_instruktor_id);
                    SET p_uspeh = TRUE;
                    SET p_poruka = 'Uspešno prijavljeni na balet';
                END IF;
                
            WHEN 'hiphop' THEN
                SELECT COUNT(*) INTO v_vec_prijavljen FROM hiphop WHERE korisnik_id = p_korisnik_id;
                IF v_vec_prijavljen > 0 THEN
                    SET p_uspeh = FALSE;
                    SET p_poruka = 'Već ste prijavljeni na hiphop časove';
                ELSE
                    INSERT INTO hiphop (korisnik_id, instruktor_id) VALUES (p_korisnik_id, p_instruktor_id);
                    SET p_uspeh = TRUE;
                    SET p_poruka = 'Uspešno prijavljeni na hiphop';
                END IF;
                
            WHEN 'latino' THEN
                SELECT COUNT(*) INTO v_vec_prijavljen FROM latino WHERE korisnik_id = p_korisnik_id;
                IF v_vec_prijavljen > 0 THEN
                    SET p_uspeh = FALSE;
                    SET p_poruka = 'Već ste prijavljeni na latino časove';
                ELSE
                    INSERT INTO latino (korisnik_id, instruktor_id) VALUES (p_korisnik_id, p_instruktor_id);
                    SET p_uspeh = TRUE;
                    SET p_poruka = 'Uspešno prijavljeni na latino';
                END IF;
                
            ELSE
                SET p_uspeh = FALSE;
                SET p_poruka = 'Nepoznat tip časa';
        END CASE;
    END IF;
END//
DELIMITER ;

-- Procedura: Odjava korisnika sa časa
DROP PROCEDURE IF EXISTS sp_odjavi_sa_casa;
DELIMITER //
CREATE PROCEDURE sp_odjavi_sa_casa(
    IN p_korisnik_id INT,
    IN p_tip_casa VARCHAR(20),
    OUT p_uspeh BOOLEAN,
    OUT p_poruka VARCHAR(255)
)
BEGIN
    DECLARE v_broj_obrisanih INT;
    
    CASE p_tip_casa
        WHEN 'balet' THEN
            DELETE FROM balet WHERE korisnik_id = p_korisnik_id;
            SET v_broj_obrisanih = ROW_COUNT();
            
        WHEN 'hiphop' THEN
            DELETE FROM hiphop WHERE korisnik_id = p_korisnik_id;
            SET v_broj_obrisanih = ROW_COUNT();
            
        WHEN 'latino' THEN
            DELETE FROM latino WHERE korisnik_id = p_korisnik_id;
            SET v_broj_obrisanih = ROW_COUNT();
            
        ELSE
            SET p_uspeh = FALSE;
            SET p_poruka = 'Nepoznat tip časa';
            SET v_broj_obrisanih = 0;
    END CASE;
    
    IF v_broj_obrisanih > 0 THEN
        SET p_uspeh = TRUE;
        SET p_poruka = CONCAT('Uspešno odjavljeni sa ', p_tip_casa, ' časova');
    ELSEIF p_uspeh IS NULL THEN
        SET p_uspeh = FALSE;
        SET p_poruka = 'Niste bili prijavljeni na ovaj čas';
    END IF;
END//
DELIMITER ;

-- Procedura: Kreiranje novog rasporeda
DROP PROCEDURE IF EXISTS sp_kreiraj_raspored;
DELIMITER //
CREATE PROCEDURE sp_kreiraj_raspored(
    IN p_tip_casa VARCHAR(20),
    IN p_instruktor_id INT,
    IN p_datum_vreme DATETIME,
    IN p_trajanje_min INT,
    IN p_lokacija VARCHAR(255),
    IN p_maksimalno_polaznika INT,
    IN p_opis TEXT,
    IN p_kreirao_id INT,
    OUT p_uspeh BOOLEAN,
    OUT p_poruka VARCHAR(255)
)
BEGIN
    DECLARE v_uloga VARCHAR(20);
    DECLARE v_konflikt INT;
    
    -- Proveri da li je kreator instruktor
    SELECT uloga INTO v_uloga FROM korisnici WHERE id = p_kreirao_id;
    
    IF v_uloga != 'instruktor' THEN
        SET p_uspeh = FALSE;
        SET p_poruka = 'Samo instruktori mogu kreirati raspored';
    ELSE
        -- Proveri da li postoji vremenski konflikt
        SELECT COUNT(*) INTO v_konflikt
        FROM raspored
        WHERE tip_casa = p_tip_casa
        AND (
            (p_datum_vreme BETWEEN datum_vreme AND DATE_ADD(datum_vreme, INTERVAL trajanje_min MINUTE))
            OR 
            (DATE_ADD(p_datum_vreme, INTERVAL p_trajanje_min MINUTE) BETWEEN datum_vreme AND DATE_ADD(datum_vreme, INTERVAL trajanje_min MINUTE))
        );
        
        IF v_konflikt > 0 THEN
            SET p_uspeh = FALSE;
            SET p_poruka = 'Postoji vremenski konflikt sa postojećim časom';
        ELSE
            INSERT INTO raspored (tip_casa, instruktor_id, datum_vreme, trajanje_min, lokacija, maksimalno_polaznika, opis, kreirao_id)
            VALUES (p_tip_casa, p_instruktor_id, p_datum_vreme, p_trajanje_min, p_lokacija, p_maksimalno_polaznika, p_opis, p_kreirao_id);
            
            SET p_uspeh = TRUE;
            SET p_poruka = CONCAT('Raspored uspešno kreiran ID:', LAST_INSERT_ID());
        END IF;
    END IF;
END//
DELIMITER ;

-- Procedura: Brisanje rasporeda
DROP PROCEDURE IF EXISTS sp_obrisi_raspored;
DELIMITER //
CREATE PROCEDURE sp_obrisi_raspored(
    IN p_raspored_id INT,
    IN p_korisnik_id INT,
    OUT p_uspeh BOOLEAN,
    OUT p_poruka VARCHAR(255)
)
BEGIN
    DECLARE v_uloga VARCHAR(20);
    DECLARE v_broj_obrisanih INT;
    
    -- Proveri da li je korisnik instruktor
    SELECT uloga INTO v_uloga FROM korisnici WHERE id = p_korisnik_id;
    
    IF v_uloga != 'instruktor' THEN
        SET p_uspeh = FALSE;
        SET p_poruka = 'Samo instruktori mogu brisati raspored';
    ELSE
        DELETE FROM raspored WHERE id = p_raspored_id;
        SET v_broj_obrisanih = ROW_COUNT();
        
        IF v_broj_obrisanih > 0 THEN
            SET p_uspeh = TRUE;
            SET p_poruka = 'Raspored uspešno obrisan';
        ELSE
            SET p_uspeh = FALSE;
            SET p_poruka = 'Raspored sa datim ID-jem ne postoji';
        END IF;
    END IF;
END//
DELIMITER ;

-- Procedura: Dobijanje statistike korisnika
DROP PROCEDURE IF EXISTS sp_statistika_korisnika;
DELIMITER //
CREATE PROCEDURE sp_statistika_korisnika(
    IN p_korisnik_id INT
)
BEGIN
    SELECT 
        k.id,
        k.ime,
        k.prezime,
        k.email,
        k.uloga,
        k.datum_registracije,
        COALESCE(b.broj_balet, 0) AS balet_casovi,
        COALESCE(h.broj_hiphop, 0) AS hiphop_casovi,
        COALESCE(l.broj_latino, 0) AS latino_casovi,
        COALESCE(b.broj_balet, 0) + COALESCE(h.broj_hiphop, 0) + COALESCE(l.broj_latino, 0) AS ukupno_casova
    FROM korisnici k
    LEFT JOIN (SELECT korisnik_id, COUNT(*) AS broj_balet FROM balet WHERE korisnik_id = p_korisnik_id GROUP BY korisnik_id) b ON k.id = b.korisnik_id
    LEFT JOIN (SELECT korisnik_id, COUNT(*) AS broj_hiphop FROM hiphop WHERE korisnik_id = p_korisnik_id GROUP BY korisnik_id) h ON k.id = h.korisnik_id
    LEFT JOIN (SELECT korisnik_id, COUNT(*) AS broj_latino FROM latino WHERE korisnik_id = p_korisnik_id GROUP BY korisnik_id) l ON k.id = l.korisnik_id
    WHERE k.id = p_korisnik_id;
END//
DELIMITER ;

-- Procedura: Autentifikacija korisnika
DROP PROCEDURE IF EXISTS sp_autentifikuj_korisnika;
DELIMITER //
CREATE PROCEDURE sp_autentifikuj_korisnika(
    IN p_email VARCHAR(255),
    IN p_lozinka VARCHAR(255),
    OUT p_uspeh BOOLEAN,
    OUT p_poruka VARCHAR(255),
    OUT p_korisnik_id INT
)
BEGIN
    DECLARE v_lozinka VARCHAR(255);
    DECLARE v_aktivan BOOLEAN;
    
    SELECT id, lozinka, aktivan INTO p_korisnik_id, v_lozinka, v_aktivan
    FROM korisnici
    WHERE email = p_email;
    
    IF p_korisnik_id IS NULL THEN
        SET p_uspeh = FALSE;
        SET p_poruka = 'Neispravni email ili lozinka';
        SET p_korisnik_id = NULL;
    ELSEIF v_aktivan = FALSE THEN
        SET p_uspeh = FALSE;
        SET p_poruka = 'Nalog je deaktiviran';
        SET p_korisnik_id = NULL;
    ELSEIF v_lozinka != p_lozinka THEN
        SET p_uspeh = FALSE;
        SET p_poruka = 'Neispravni email ili lozinka';
        SET p_korisnik_id = NULL;
    ELSE
        SET p_uspeh = TRUE;
        SET p_poruka = 'Uspešna prijava';
    END IF;
END//
DELIMITER ;
