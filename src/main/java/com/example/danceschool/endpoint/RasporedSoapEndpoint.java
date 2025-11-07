package com.example.danceschool.endpoint;

import com.example.danceschool.model.Raspored;
import com.example.danceschool.model.User;
import com.example.danceschool.repository.UserRepository;
import com.example.danceschool.service.RasporedService;
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

@Endpoint
public class RasporedSoapEndpoint {

    private static final String NAMESPACE_URI = "http://danceschool.example.com/raspored";

    @Autowired
    private RasporedService rasporedService;

    @Autowired
    private UserRepository userRepository;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetAllSchedulesRequest")
    @ResponsePayload
    public GetAllSchedulesResponse getAllSchedules(@RequestPayload GetAllSchedulesRequest request) {
        GetAllSchedulesResponse response = new GetAllSchedulesResponse();

        try {
            // Get user
            Optional<User> userOpt = userRepository.findById(request.getUserId());
            if (!userOpt.isPresent()) {
                return response; // Return empty list
            }

            User user = userOpt.get();
            List<Raspored> schedules = rasporedService.getScheduleForUser(user);

            // Convert to SOAP response types
            for (Raspored raspored : schedules) {
                RasporedType rasporedType = convertToRasporedType(raspored);
                response.getSchedule().add(rasporedType);
            }

        } catch (Exception e) {
            // Log error and return empty response
            System.err.println("Error in getAllSchedules: " + e.getMessage());
        }

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "GetSchedulesByClassTypeRequest")
    @ResponsePayload
    public GetSchedulesByClassTypeResponse getSchedulesByClassType(@RequestPayload GetSchedulesByClassTypeRequest request) {
        GetSchedulesByClassTypeResponse response = new GetSchedulesByClassTypeResponse();

        try {
            // Get user
            Optional<User> userOpt = userRepository.findById(request.getUserId());
            if (!userOpt.isPresent()) {
                return response; // Return empty list
            }

            User user = userOpt.get();
            List<Raspored> schedules = rasporedService.getScheduleForUser(user);

            // Filter by class type
            String classType = request.getClassType().value();
            for (Raspored raspored : schedules) {
                if (raspored.getTipCasa().equalsIgnoreCase(classType)) {
                    RasporedType rasporedType = convertToRasporedType(raspored);
                    response.getSchedule().add(rasporedType);
                }
            }

        } catch (Exception e) {
            System.err.println("Error in getSchedulesByClassType: " + e.getMessage());
        }

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "AddScheduleRequest")
    @ResponsePayload
    public AddScheduleResponse addSchedule(@RequestPayload AddScheduleRequest request) {
        AddScheduleResponse response = new AddScheduleResponse();

        try {
            // Get user
            Optional<User> userOpt = userRepository.findById(request.getUserId());
            if (!userOpt.isPresent()) {
                response.setSuccess(false);
                response.setMessage("User not found");
                return response;
            }

            User user = userOpt.get();

            // Convert XMLGregorianCalendar to LocalDateTime
            LocalDateTime datumVreme = convertToLocalDateTime(request.getDatumVreme());

            // Get instruktorId (use request instruktorId if provided, otherwise use current user)
            Long instruktorId = request.getInstruktorId() != null ? request.getInstruktorId() : user.getId();

            // Call service
            Raspored newRaspored = rasporedService.addSchedule(
                    user,
                    request.getTipCasa().value(),
                    datumVreme,
                    request.getTrajanjeMin(),
                    request.getLokacija(),
                    request.getOpis(),
                    instruktorId
            );

            if (newRaspored != null) {
                response.setSuccess(true);
                response.setMessage("Schedule added successfully");
                response.setSchedule(convertToRasporedType(newRaspored));
            } else {
                response.setSuccess(false);
                response.setMessage("Failed to add schedule");
            }

        } catch (IllegalArgumentException e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Internal error: " + e.getMessage());
            System.err.println("Error in addSchedule: " + e.getMessage());
            e.printStackTrace();
        }

        return response;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "DeleteScheduleRequest")
    @ResponsePayload
    public DeleteScheduleResponse deleteSchedule(@RequestPayload DeleteScheduleRequest request) {
        DeleteScheduleResponse response = new DeleteScheduleResponse();

        try {
            // Get user
            Optional<User> userOpt = userRepository.findById(request.getUserId());
            if (!userOpt.isPresent()) {
                response.setSuccess(false);
                response.setMessage("User not found");
                return response;
            }

            User user = userOpt.get();

            // Call service
            rasporedService.deleteSchedule(user, request.getScheduleId());

            response.setSuccess(true);
            response.setMessage("Schedule deleted successfully");

        } catch (IllegalArgumentException e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Internal error: " + e.getMessage());
            System.err.println("Error in deleteSchedule: " + e.getMessage());
        }

        return response;
    }

    // Helper methods
    private RasporedType convertToRasporedType(Raspored raspored) {
        RasporedType rasporedType = new RasporedType();
        rasporedType.setId(raspored.getId());
        rasporedType.setTipCasa(raspored.getTipCasa());
        rasporedType.setDatumVreme(convertToXMLGregorianCalendar(raspored.getDatumVreme()));
        rasporedType.setTrajanjeMin(raspored.getTrajanjeMin());
        rasporedType.setLokacija(raspored.getLokacija());
        rasporedType.setOpis(raspored.getOpis());
        rasporedType.setInstruktorId(raspored.getInstruktorId());

        // Optional: fetch instructor name
        if (raspored.getInstruktorId() != null) {
            userRepository.findById(raspored.getInstruktorId()).ifPresent(instructor -> {
                rasporedType.setInstruktorIme(instructor.getIme() + " " + instructor.getPrezime());
            });
        }

        return rasporedType;
    }

    private XMLGregorianCalendar convertToXMLGregorianCalendar(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return null;
        }
        try {
            GregorianCalendar gregorianCalendar = GregorianCalendar.from(
                    localDateTime.atZone(ZoneId.systemDefault())
            );
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
        } catch (DatatypeConfigurationException e) {
            System.err.println("Error converting to XMLGregorianCalendar: " + e.getMessage());
            return null;
        }
    }

    private LocalDateTime convertToLocalDateTime(XMLGregorianCalendar xmlGregorianCalendar) {
        if (xmlGregorianCalendar == null) {
            return null;
        }
        return xmlGregorianCalendar.toGregorianCalendar()
                .toZonedDateTime()
                .toLocalDateTime();
    }
}
