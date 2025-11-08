-- Stored Procedure za izmenu rasporeda
USE skola_plesa;

DROP PROCEDURE IF EXISTS sp_izmeni_cas;
DELIMITER //
CREATE PROCEDURE sp_izmeni_cas(
    IN p_raspored_id INT,
    IN p_tip_casa VARCHAR(20),
    IN p_datum_vreme DATETIME,
    IN p_trajanje_min INT,
    IN p_lokacija VARCHAR(255),
    IN p_maksimalno_polaznika INT,
    IN p_opis TEXT,
    IN p_korisnik_id INT,
    OUT p_uspeh BOOLEAN,
    OUT p_poruka VARCHAR(255)
)
BEGIN
    DECLARE v_uloga VARCHAR(20);
    DECLARE v_postojeci_raspored INT;
    DECLARE v_konflikt INT;
    
    -- Proveri da li je korisnik instruktor
    SELECT uloga INTO v_uloga FROM korisnici WHERE id = p_korisnik_id;
    
    IF v_uloga != 'instruktor' THEN
        SET p_uspeh = FALSE;
        SET p_poruka = 'Samo instruktori mogu da menjaju raspored';
    ELSE
        -- Proveri da li raspored postoji
        SELECT COUNT(*) INTO v_postojeci_raspored FROM raspored WHERE id = p_raspored_id;
        
        IF v_postojeci_raspored = 0 THEN
            SET p_uspeh = FALSE;
            SET p_poruka = 'Raspored sa datim ID-jem ne postoji';
        ELSE
            -- Proveri da li postoji vremenski konflikt sa drugim časovima (ne računajući trenutni)
            SELECT COUNT(*) INTO v_konflikt
            FROM raspored
            WHERE id != p_raspored_id
            AND tip_casa = p_tip_casa
            AND (
                (p_datum_vreme BETWEEN datum_vreme AND DATE_ADD(datum_vreme, INTERVAL trajanje_min MINUTE))
                OR 
                (DATE_ADD(p_datum_vreme, INTERVAL p_trajanje_min MINUTE) BETWEEN datum_vreme AND DATE_ADD(datum_vreme, INTERVAL trajanje_min MINUTE))
            );
            
            IF v_konflikt > 0 THEN
                SET p_uspeh = FALSE;
                SET p_poruka = 'Postoji vremenski konflikt sa postojećim časom';
            ELSE
                -- Ažuriraj raspored
                UPDATE raspored 
                SET tip_casa = p_tip_casa,
                    datum_vreme = p_datum_vreme,
                    trajanje_min = p_trajanje_min,
                    lokacija = p_lokacija,
                    maksimalno_polaznika = p_maksimalno_polaznika,
                    opis = p_opis
                WHERE id = p_raspored_id;
                
                SET p_uspeh = TRUE;
                SET p_poruka = CONCAT('Raspored uspešno ažuriran ID:', p_raspored_id);
            END IF;
        END IF;
    END IF;
END//
DELIMITER ;
