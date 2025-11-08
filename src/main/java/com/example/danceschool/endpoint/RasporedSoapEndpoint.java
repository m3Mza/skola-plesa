package com.example.danceschool.endpoint;

import com.example.danceschool.model.Korisnik;
import com.example.danceschool.model.Raspored;
import com.example.danceschool.repository.KorisnikRepozitorijum;
import com.example.danceschool.service.RasporedServis;
import com.example.danceschool.raspored.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

/**
 * SOAP Endpoint za upravljanje rasporedom časova.
 */
@Endpoint
public class RasporedSoapEndpoint {

    private static final String NAMESPACE_URI = "http://danceschool.example.com/raspored";

    @Autowired
    private RasporedServis raspored_servis;

    @Autowired
    private KorisnikRepozitorijum korisnik_repozitorijum;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetAllSchedulesRequest")
    @ResponsePayload
    public GetAllSchedulesResponse getAllSchedules(@RequestPayload GetAllSchedulesRequest request) {
        GetAllSchedulesResponse response = new GetAllSchedulesResponse();

        try {
            Optional<Korisnik> korisnik_opt = korisnik_repozitorijum.pronadji_po_id(request.getUserId());
            if (!korisnik_opt.isPresent()) {
                return response;
            }

            Korisnik korisnik = korisnik_opt.get();
            List<Raspored> rasporedi = raspored_servis.dobavi_raspored_za_korisnika(korisnik);

            for (Raspored raspored : rasporedi) {
                RasporedType raspored_type = konvertuj_u_raspored_type(raspored);
                response.getSchedule().add(raspored_type);
            }

        } catch (Exception e) {
            System.err.println("GREŠKA u getAllSchedules: " + e.getMessage());
        }

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetSchedulesByClassTypeRequest")
    @ResponsePayload
    public GetSchedulesByClassTypeResponse getSchedulesByClassType(@RequestPayload GetSchedulesByClassTypeRequest request) {
        GetSchedulesByClassTypeResponse response = new GetSchedulesByClassTypeResponse();

        try {
            Optional<Korisnik> korisnik_opt = korisnik_repozitorijum.pronadji_po_id(request.getUserId());
            if (!korisnik_opt.isPresent()) {
                return response;
            }

            Korisnik korisnik = korisnik_opt.get();
            List<Raspored> rasporedi = raspored_servis.dobavi_raspored_za_korisnika(korisnik);

            String tip_casa = request.getClassType().value();
            for (Raspored raspored : rasporedi) {
                if (raspored.getTip_casa().equalsIgnoreCase(tip_casa)) {
                    RasporedType raspored_type = konvertuj_u_raspored_type(raspored);
                    response.getSchedule().add(raspored_type);
                }
            }

        } catch (Exception e) {
            System.err.println("GREŠKA u getSchedulesByClassType: " + e.getMessage());
        }

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "AddScheduleRequest")
    @ResponsePayload
    public AddScheduleResponse addSchedule(@RequestPayload AddScheduleRequest request) {
        AddScheduleResponse response = new AddScheduleResponse();

        try {
            Optional<Korisnik> korisnik_opt = korisnik_repozitorijum.pronadji_po_id(request.getUserId());
            if (!korisnik_opt.isPresent()) {
                response.setSuccess(false);
                response.setMessage("Korisnik nije pronađen");
                return response;
            }

            Korisnik korisnik = korisnik_opt.get();

            LocalDateTime datum_vreme = konvertuj_u_local_date_time(request.getDatumVreme());

            Raspored novi_raspored = raspored_servis.dodaj_raspored(
                    korisnik,
                    request.getTipCasa().value(),
                    datum_vreme,
                    request.getTrajanjeMin(),
                    request.getLokacija(),
                    15, // Default maksimalno_polaznika
                    request.getOpis()
            );

            if (novi_raspored != null) {
                response.setSuccess(true);
                response.setMessage("Raspored uspešno dodat");
                response.setSchedule(konvertuj_u_raspored_type(novi_raspored));
            } else {
                response.setSuccess(false);
                response.setMessage("Neuspelo dodavanje rasporeda");
            }

        } catch (RuntimeException e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Interna greška: " + e.getMessage());
            System.err.println("GREŠKA u addSchedule: " + e.getMessage());
            e.printStackTrace();
        }

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "DeleteScheduleRequest")
    @ResponsePayload
    public DeleteScheduleResponse deleteSchedule(@RequestPayload DeleteScheduleRequest request) {
        DeleteScheduleResponse response = new DeleteScheduleResponse();

        try {
            Optional<Korisnik> korisnik_opt = korisnik_repozitorijum.pronadji_po_id(request.getUserId());
            if (!korisnik_opt.isPresent()) {
                response.setSuccess(false);
                response.setMessage("Korisnik nije pronađen");
                return response;
            }

            Korisnik korisnik = korisnik_opt.get();

            String rezultat = raspored_servis.obrisi_raspored(korisnik, request.getScheduleId());

            if (rezultat.startsWith("USPEH")) {
                response.setSuccess(true);
                response.setMessage("Raspored uspešno obrisan");
            } else {
                response.setSuccess(false);
                response.setMessage(rezultat);
            }

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Interna greška: " + e.getMessage());
            System.err.println("GREŠKA u deleteSchedule: " + e.getMessage());
        }

        return response;
    }

    /**
     * Konvertuje Raspored model u SOAP RasporedType.
     */
    private RasporedType konvertuj_u_raspored_type(Raspored raspored) {
        RasporedType raspored_type = new RasporedType();
        raspored_type.setId(raspored.getId());
        raspored_type.setTipCasa(raspored.getTip_casa());
        raspored_type.setDatumVreme(konvertuj_u_xml_gregorian_calendar(raspored.getDatum_vreme()));
        raspored_type.setTrajanjeMin(raspored.getTrajanje_min());
        raspored_type.setLokacija(raspored.getLokacija());
        raspored_type.setOpis(raspored.getOpis());
        raspored_type.setInstruktorId(raspored.getInstruktor_id());

        if (raspored.getInstruktor_id() != null) {
            korisnik_repozitorijum.pronadji_po_id(raspored.getInstruktor_id()).ifPresent(instruktor -> {
                raspored_type.setInstruktorIme(instruktor.getIme() + " " + instruktor.getPrezime());
            });
        }

        return raspored_type;
    }

    /**
     * Konvertuje LocalDateTime u XMLGregorianCalendar.
     */
    private XMLGregorianCalendar konvertuj_u_xml_gregorian_calendar(LocalDateTime local_date_time) {
        if (local_date_time == null) {
            return null;
        }
        try {
            GregorianCalendar gregorian_calendar = GregorianCalendar.from(
                    local_date_time.atZone(ZoneId.systemDefault())
            );
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorian_calendar);
        } catch (DatatypeConfigurationException e) {
            System.err.println("GREŠKA pri konverziji u XMLGregorianCalendar: " + e.getMessage());
            return null;
        }
    }

    /**
     * Konvertuje XMLGregorianCalendar u LocalDateTime.
     */
    private LocalDateTime konvertuj_u_local_date_time(XMLGregorianCalendar xml_gregorian_calendar) {
        if (xml_gregorian_calendar == null) {
            return null;
        }
        return xml_gregorian_calendar.toGregorianCalendar()
                .toZonedDateTime()
                .toLocalDateTime();
    }
}
