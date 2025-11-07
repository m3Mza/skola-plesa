-- Update the stored procedure to explicitly set datum_prijave
USE skola_plesa;

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
                    INSERT INTO balet (korisnik_id, instruktor_id, datum_prijave) 
                    VALUES (p_korisnik_id, p_instruktor_id, NOW());
                    SET p_uspeh = TRUE;
                    SET p_poruka = 'Uspešno prijavljeni na balet';
                END IF;
                
            WHEN 'hiphop' THEN
                SELECT COUNT(*) INTO v_vec_prijavljen FROM hiphop WHERE korisnik_id = p_korisnik_id;
                IF v_vec_prijavljen > 0 THEN
                    SET p_uspeh = FALSE;
                    SET p_poruka = 'Već ste prijavljeni na hiphop časove';
                ELSE
                    INSERT INTO hiphop (korisnik_id, instruktor_id, datum_prijave) 
                    VALUES (p_korisnik_id, p_instruktor_id, NOW());
                    SET p_uspeh = TRUE;
                    SET p_poruka = 'Uspešno prijavljeni na hiphop';
                END IF;
                
            WHEN 'latino' THEN
                SELECT COUNT(*) INTO v_vec_prijavljen FROM latino WHERE korisnik_id = p_korisnik_id;
                IF v_vec_prijavljen > 0 THEN
                    SET p_uspeh = FALSE;
                    SET p_poruka = 'Već ste prijavljeni na latino časove';
                ELSE
                    INSERT INTO latino (korisnik_id, instruktor_id, datum_prijave) 
                    VALUES (p_korisnik_id, p_instruktor_id, NOW());
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
