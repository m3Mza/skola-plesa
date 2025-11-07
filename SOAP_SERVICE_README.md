# SOAP Web Service Documentation

## Overview
A SOAP web service has been successfully added to the Dance School application. The service exposes schedule management operations via SOAP/XML protocol.

## WSDL Location
When the application is running, the WSDL is available at:
```
http://localhost:8080/ws/raspored.wsdl
```

## Endpoint URL
```
http://localhost:8080/ws
```

## Target Namespace
```
http://danceschool.example.com/raspored
```

## Available Operations

### 1. GetAllSchedules
Retrieves all schedules for a specific user based on their role.

**Request:**
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:ras="http://danceschool.example.com/raspored">
   <soapenv:Header/>
   <soapenv:Body>
      <ras:GetAllSchedulesRequest>
         <ras:userId>1</ras:userId>
      </ras:GetAllSchedulesRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

**Response:**
```xml
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
   <SOAP-ENV:Body>
      <ns2:GetAllSchedulesResponse xmlns:ns2="http://danceschool.example.com/raspored">
         <ns2:schedule>
            <ns2:id>1</ns2:id>
            <ns2:tipCasa>balet</ns2:tipCasa>
            <ns2:datumVreme>2025-11-10T18:00:00</ns2:datumVreme>
            <ns2:trajanjeMin>60</ns2:trajanjeMin>
            <ns2:instruktorId>2</ns2:instruktorId>
            <ns2:instruktorIme>John Doe</ns2:instruktorIme>
         </ns2:schedule>
      </ns2:GetAllSchedulesResponse>
   </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```

### 2. GetSchedulesByClassType
Retrieves schedules filtered by class type.

**Request:**
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:ras="http://danceschool.example.com/raspored">
   <soapenv:Header/>
   <soapenv:Body>
      <ras:GetSchedulesByClassTypeRequest>
         <ras:userId>1</ras:userId>
         <ras:classType>balet</ras:classType>
      </ras:GetSchedulesByClassTypeRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

Valid classType values: `balet`, `hiphop`, `latino`

### 3. AddSchedule
Adds a new schedule entry (instructor only).

**Request:**
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:ras="http://danceschool.example.com/raspored">
   <soapenv:Header/>
   <soapenv:Body>
      <ras:AddScheduleRequest>
         <ras:userId>2</ras:userId>
         <ras:tipCasa>balet</ras:tipCasa>
         <ras:datumVreme>2025-11-15T18:00:00</ras:datumVreme>
         <ras:trajanjeMin>90</ras:trajanjeMin>
         <ras:opis>Advanced ballet class</ras:opis>
      </ras:AddScheduleRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

**Response:**
```xml
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
   <SOAP-ENV:Body>
      <ns2:AddScheduleResponse xmlns:ns2="http://danceschool.example.com/raspored">
         <ns2:success>true</ns2:success>
         <ns2:message>Schedule added successfully</ns2:message>
         <ns2:schedule>
            <ns2:id>5</ns2:id>
            <ns2:tipCasa>balet</ns2:tipCasa>
            <ns2:datumVreme>2025-11-15T18:00:00</ns2:datumVreme>
            <ns2:trajanjeMin>90</ns2:trajanjeMin>
            <ns2:opis>Advanced ballet class</ns2:opis>
            <ns2:instruktorId>2</ns2:instruktorId>
         </ns2:schedule>
      </ns2:AddScheduleResponse>
   </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```

### 4. DeleteSchedule
Deletes a schedule entry (instructor only).

**Request:**
```xml
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:ras="http://danceschool.example.com/raspored">
   <soapenv:Header/>
   <soapenv:Body>
      <ras:DeleteScheduleRequest>
         <ras:userId>2</ras:userId>
         <ras:scheduleId>5</ras:scheduleId>
      </ras:DeleteScheduleRequest>
   </soapenv:Body>
</soapenv:Envelope>
```

**Response:**
```xml
<SOAP-ENV:Envelope xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
   <SOAP-ENV:Body>
      <ns2:DeleteScheduleResponse xmlns:ns2="http://danceschool.example.com/raspored">
         <ns2:success>true</ns2:success>
         <ns2:message>Schedule deleted successfully</ns2:message>
      </ns2:DeleteScheduleResponse>
   </SOAP-ENV:Body>
</SOAP-ENV:Envelope>
```

## Testing the SOAP Service

### Using cURL
```bash
curl -X POST http://localhost:8080/ws \
  -H "Content-Type: text/xml" \
  -d '<?xml version="1.0"?>
<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" 
                  xmlns:ras="http://danceschool.example.com/raspored">
   <soapenv:Header/>
   <soapenv:Body>
      <ras:GetAllSchedulesRequest>
         <ras:userId>1</ras:userId>
      </ras:GetAllSchedulesRequest>
   </soapenv:Body>
</soapenv:Envelope>'
```

### Using SoapUI
1. Create a new SOAP project
2. Enter WSDL URL: `http://localhost:8080/ws/raspored.wsdl`
3. SoapUI will automatically generate sample requests for all operations
4. Modify the sample requests with actual data and send

### Using Postman
1. Create a new request
2. Set method to POST
3. Set URL to `http://localhost:8080/ws`
4. Set Headers: `Content-Type: text/xml`
5. In Body tab, select raw and paste the SOAP envelope
6. Send the request

## Implementation Details

### Files Created/Modified

1. **pom.xml** - Added dependencies:
   - spring-boot-starter-web-services
   - spring-boot-starter-security
   - wsdl4j
   - jaxb2-maven-plugin

2. **src/main/resources/xsd/raspored.xsd** - XSD schema defining SOAP types

3. **src/main/java/com/example/danceschool/config/WebServiceConfig.java** - SOAP configuration

4. **src/main/java/com/example/danceschool/endpoint/RasporedSoapEndpoint.java** - SOAP endpoint implementation

5. **src/main/resources/application.properties** - Added SOAP servlet configuration

6. **Generated classes** in `com.example.danceschool.raspored` package from XSD schema

## Business Logic
The SOAP service reuses the existing business logic from:
- `RasporedService` - Schedule management
- `UserRepository` - User authentication
- Existing stored procedures for database operations

## Security Notes
- Currently using Spring Security with all requests permitted
- In production, implement WS-Security or token-based authentication
- Consider using HTTPS for encrypted communication
- Validate user permissions before executing operations

## Starting the Application
```bash
mvn spring-boot:run
```

The SOAP service will be available at `http://localhost:8080/ws` and WSDL at `http://localhost:8080/ws/raspored.wsdl`
