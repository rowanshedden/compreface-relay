package aero.sita.compreface.services;

import aero.sita.compreface.handlers.RestInterfaceHandler;
import aero.sita.compreface.models.dto.Error;
import aero.sita.compreface.models.dto.*;
import aero.sita.compreface.utils.MiscUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Feed Service
 * <p>
 * Use the CompreFace API to add or delete a subject
 * <p>
 * Messages sourced from a WebSocket connection
 */
@Service
public class FeedService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RestInterfaceHandler restInterface;

    @Value("${app.feeder.compreface.url}")
    private String url;
    @Value("${app.feeder.compreface.detection.threshold}")
    private String detectionThreshold;

    @Value("${app.feeder.country}")
    private String country;
    @Value("${app.feeder.port}")
    private String port;
    @Value("${app.feeder.alt.country}")
    private String altCountry;
    @Value("${app.feeder.alt.port}")
    private String altPort;
    @Value("${app.feeder.flight}")
    private String flight;

    /**
     * Received a WebSocket ADD message.
     * <p>
     * The Feed Service will receive a GalleryAction message of ADD from the
     * connected Gallery Service over the WebSocket connection. The received
     * GalleryAction will be transformed into an add subject request and the
     * Compreface API invoked to perform the operation
     *
     * @param galleryAction GalleryAction
     */
    public SubjectResponse addSubject(GalleryAction galleryAction) {
        /*
         * extract the gallery record
         */
        GalleryRecord galleryRecord = galleryAction.getData();
        /*
         * build an itinerary
         */
        if (galleryRecord.getItinerary() != null) {
            galleryRecord.getItinerary().setCarrierCode(flight.substring(0, 2).toUpperCase());
            galleryRecord.getItinerary().setServiceNumber(flight.toUpperCase());
            PortDetails departureAirport = new PortDetails();
            departureAirport.setCountryCode(country.toUpperCase());
            departureAirport.setPortCode(port.toUpperCase());
            departureAirport.setDateTime(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            galleryRecord.getItinerary().getRouteDetails().get(0).setDeparture(departureAirport);
            if (galleryRecord.getItinerary().getRouteDetails().get(0).getArrival().getPortCode().equalsIgnoreCase(port)) {
                PortDetails arrivalAirport = new PortDetails();
                arrivalAirport.setCountryCode(altCountry.toUpperCase());
                arrivalAirport.setPortCode(altPort.toUpperCase());
                arrivalAirport.setDateTime(LocalDateTime.now().plusHours(8).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                galleryRecord.getItinerary().getRouteDetails().get(1).setDeparture(arrivalAirport);
            }
        } else {
            // build a fake itinerary
            TravelItinerary itinerary = new TravelItinerary();
            itinerary.setServiceNumber(flight.toUpperCase());
            itinerary.setCarrierCode(flight.substring(0, 2).toUpperCase());
            itinerary.setCarrierType("A");
            itinerary.setPnrNumber("QSC1234");
            itinerary.setSeatNumber("23F");
            List<RouteDetails> routeDetailsList = new ArrayList<RouteDetails>();
            itinerary.setRouteDetails(routeDetailsList);
            RouteDetails routeDetails = new RouteDetails();
            PortDetails departureAirport = new PortDetails();
            departureAirport.setCountryCode(country.toUpperCase());
            departureAirport.setPortCode(port.toUpperCase());
            departureAirport.setDateTime(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            routeDetails.setDeparture(departureAirport);
            PortDetails arrivalAirport = new PortDetails();
            arrivalAirport.setCountryCode(altCountry.toUpperCase());
            arrivalAirport.setPortCode(altPort.toUpperCase());
            arrivalAirport.setDateTime(LocalDateTime.now().plusHours(8).format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            routeDetails.setArrival(arrivalAirport);
            routeDetailsList.add(routeDetails);
            galleryRecord.setItinerary(itinerary);
        }
        /*
         * add subject to CompreFace using the CompreFace API
         *
         * POST api/v1/recognition/faces?subject={{subject}}&det_prob_threshold={detection_threshold}
         * e.g.
         * POST api/v1/recognition/faces?subject=Damien Mascord
         */
        try {
            SubjectRequest subjectRequest = new SubjectRequest();
            subjectRequest.setFile(galleryRecord.getDtc().getLiveImage());
            String subjectName = galleryRecord.getDtc().getGivenNames() + " " + galleryRecord.getDtc().getFamilyName();
            RawHttpResult result = restInterface.call(url, "api/v1/recognition/faces?subject=" + subjectName.trim() + "&det_prob_threshold=" + detectionThreshold, HttpMethod.POST, subjectRequest.toJson());
            String body = result.getBody();
            SubjectResponse subjectResponse = (SubjectResponse) MiscUtil.fromJson(body, SubjectResponse.class);
            assert subjectResponse != null;
            subjectResponse.setSuccess(result.isSuccess());
            return subjectResponse;
        } catch (Exception e) {
            String errorMessage = "Unable to add subject: " + e.getLocalizedMessage();
            log.error(errorMessage);
            SubjectResponse errorResponse = new SubjectResponse();
            errorResponse.setError(new Error(System.currentTimeMillis(), 9999, "CompreFace relay add subject response", errorMessage, null));
            return errorResponse;
        }
    }

    /**
     * Received a WebSocket UPDATE message.
     * <p>
     * The Feed Service will receive a GalleryAction message of UPDATE from the
     * connected Gallery Service over the WebSocket connection. The received
     * GalleryAction will be transformed into a update subject request and the
     * Compreface API invoked to perform the operation
     *
     * @param galleryAction GalleryAction
     */
    public SubjectResponse updateSubject(GalleryAction galleryAction) {
        /*
         * extract the gallery record
         */
        GalleryRecord galleryRecord = galleryAction.getData();

        /*
         * update the subject using the CompreFace API
         *
         * PUT /api/v1/recognition/subjects/{{subject_name}}
         * e.g.
         * PUT /api/v1/recognition/subjects/Damien Mascord
         */
        try {
            SubjectRequest subjectRequest = new SubjectRequest();
            subjectRequest.setFile(galleryRecord.getDtc().getLiveImage());
            String subjectName = galleryRecord.getDtc().getGivenNames() + " " + galleryRecord.getDtc().getFamilyName();
            RawHttpResult result = restInterface.call(url, "/api/v1/recognition/subjects/" + subjectName.trim(), HttpMethod.PUT, subjectRequest.toJson());
            String body = result.getBody();
            SubjectResponse subjectResponse = (SubjectResponse) MiscUtil.fromJson(body, SubjectResponse.class);
            assert subjectResponse != null;
            subjectResponse.setSuccess(result.isSuccess());
            return subjectResponse;
        } catch (Exception e) {
            String errorMessage = "Unable to update subject: " + e.getLocalizedMessage();
            log.error(errorMessage);
            SubjectResponse errorResponse = new SubjectResponse();
            errorResponse.setError(new Error(System.currentTimeMillis(), 9999, "CompreFace relay update subject response", errorMessage, null));
            return errorResponse;
        }
    }

    /**
     * Received a WebSocket DELETE message.
     * <p>
     * The Feed Service will receive a GalleryAction message of DELETE from the
     * connected Gallery Service over the WebSocket connection. The received
     * GalleryAction will be transformed into a delete subject request and the
     * Compreface API invoked to perform the operation
     *
     * @param galleryAction GalleryAction
     */
    public SubjectResponse deleteSubject(GalleryAction galleryAction) {
        /*
         * extract the gallery record
         */
        GalleryRecord galleryRecord = galleryAction.getData();

        /*
         * delete the subject using the CompreFace API
         *
         * DELETE /api/v1/recognition/faces?subject={{subject_name}}
         * e.g.
         * DELETE /api/v1/recognition/faces?subject=gnm:Rowan George McDonald,fnm:Shedden,dob:1965-02-24,sex:M,nat:MYS,doc:BWUSM9BF,iss:MYS,exp:2026-09-03
         */
        try {
            SubjectRequest subjectRequest = new SubjectRequest();
            subjectRequest.setFile(galleryRecord.getDtc().getLiveImage());
            String subjectName = galleryRecord.getDtc().getGivenNames() + " " + galleryRecord.getDtc().getFamilyName();
            RawHttpResult result = restInterface.call(url, "/api/v1/recognition/faces?subject=" + subjectName.trim(), HttpMethod.DELETE, subjectRequest.toJson());
            String body = result.getBody();
            SubjectResponse subjectResponse = (SubjectResponse) MiscUtil.fromJson(body, SubjectResponse.class);
            assert subjectResponse != null;
            subjectResponse.setSuccess(result.isSuccess());
            return subjectResponse;
        } catch (Exception e) {
            String errorMessage = "Unable to delete subject: " + e.getLocalizedMessage();
            log.error(errorMessage);
            SubjectResponse errorResponse = new SubjectResponse();
            errorResponse.setError(new Error(System.currentTimeMillis(), 9999, "CompreFace relay delete subject response", errorMessage, null));
            return errorResponse;
        }
    }

    /**
     * Received an API delete all subjects message.
     * <p>
     * The Feed Service will receive a GET /subject/delete message via
     * the API and the CompreFace /api/v1/recognition/subjects API is
     * invoked to perform the operation
     */
    public SubjectResponse deleteAllSubjects() {
        /*
         * clear all the subjects using the CompreFace API
         *
         * e.g.
         * DELETE /api/v1/recognition/subjects
         */
        try {
            RawHttpResult result = restInterface.call(url, "/api/v1/recognition/subjects", HttpMethod.DELETE, null);
            String body = result.getBody();
            SubjectResponse subjectResponse = (SubjectResponse) MiscUtil.fromJson(body, SubjectResponse.class);
            assert subjectResponse != null;
            subjectResponse.setSuccess(result.isSuccess());
            return subjectResponse;
        } catch (Exception e) {
            String errorMessage = "Unable to delete all subjects: " + e.getLocalizedMessage();
            log.error(errorMessage);
            SubjectResponse errorResponse = new SubjectResponse();
            errorResponse.setError(new Error(System.currentTimeMillis(), 9999, "CompreFace relay delete all subjects response", errorMessage, null));
            return errorResponse;
        }
    }
}