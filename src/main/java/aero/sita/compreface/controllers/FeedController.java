package aero.sita.compreface.controllers;

import aero.sita.compreface.handlers.WebSocketHandler;
import aero.sita.compreface.models.dto.BaseResponse;
import aero.sita.compreface.models.dto.GalleryAction;
import aero.sita.compreface.models.dto.SubjectResponse;
import aero.sita.compreface.services.FeedService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = {"CompreFace Relay API"}, description = "CompreFace relay service")
public class FeedController extends Api_1_0 {

    @Autowired
    private FeedService feedService;

    @Autowired
    private WebSocketHandler galleryWebSocketClient;

    @ApiOperation(value = "Feed a Digital Travel Lane ADD request to CompreFace", authorizations = {
            @Authorization(value = "Bearer")}, response = SubjectResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "CompreFace receives an add subject request")})
    @RequestMapping(value = "feed/add", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> addSubject(@Validated @RequestBody GalleryAction request) {
        SubjectResponse response = feedService.addSubject(request);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Feed a Digital Travel Lane UPDATE request to CompreFace", authorizations = {
            @Authorization(value = "Bearer")}, response = SubjectResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "CompreFace receives an add subject request")})
    @RequestMapping(value = "feed/update", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> updateSubject(@Validated @RequestBody GalleryAction request) {
        SubjectResponse response = feedService.updateSubject(request);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Feed a Digital Travel Lane DELETE request to CompreFace", authorizations = {
            @Authorization(value = "Bearer")}, response = SubjectResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "CompreFace receives a delete request")})
    @RequestMapping(value = "feed/delete", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deleteSubject(@Validated @RequestBody GalleryAction request) {
        SubjectResponse response = feedService.deleteSubject(request);
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Clear the subject database in CompreFace", authorizations = {
            @Authorization(value = "Bearer")}, response = SubjectResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "CompreFace receives a delete all subject request")})
    @RequestMapping(value = "subject/delete", method = RequestMethod.GET, produces = {
            MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> deleteAllSubjects() {
        SubjectResponse response = feedService.deleteAllSubjects();
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Restart the Gallery WebSocket client", authorizations = {@Authorization(value = "Bearer")}, response = BaseResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "WebSocket client restarted")})
    @RequestMapping(value = "websocket/restart/gallery", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> restartClient() {
        BaseResponse response = galleryWebSocketClient.restart();
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "Check status of websocket connection (true = connected)", authorizations = {@Authorization(value = "Bearer")}, response = BaseResponse.class)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "WebSocket client is connected")})
    @RequestMapping(value = "websocket/connected", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<?> isConnected() {
        BaseResponse response = galleryWebSocketClient.isConnected();
        if (!response.isSuccess()) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }

}