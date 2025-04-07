package tn.esprit.Controller;

import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.Exceptions.CustomException;
import tn.esprit.Services.*;
import tn.esprit.entities.*;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(
        origins = {"*"}
)
@RequiredArgsConstructor
@RequestMapping("event")
@RestController
public class eventController {


    private final IEventService eventService;
    private final IFeedBackEventService feedBackEventService;
    private final ILocationEventService locationEventService;
    private final INotificationService notificationService;
    private final IPlanningService planningService;
    private final IRegistrationService registrationService;
    private final FileStorageService fileStorageService;


    @Value("${file.upload-dir}")
    private String uploadDir;

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
        String fileUrl = fileStorageService.storeFile(file);
        Map<String, String> response = new HashMap<>();
        response.put("url", fileUrl);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{filename}")
    public Resource getFile(@PathVariable String filename) {
        try {

            Path filePath = Paths.get(uploadDir).resolve(filename);
            return new UrlResource(filePath.toUri());
        } catch (Exception e) {
            throw new RuntimeException("Fichier introuvable : " + filename);
        }
    }

    @GetMapping("/event")
    public List<Event> getEvents() {
        List<Event> listEvents = eventService.getEvent();
        return listEvents;
    }
    @GetMapping("/retrieve-Event/{event-id}")
    public Event retrieveEvent(@PathVariable("event-id") Long chId) {
        Event event = eventService.retrieveEvent(chId);
        return event;
    }
    @PostMapping("/add-event")
    public Event addEvent(@RequestBody Event c) {
        Event event = eventService.addEvent(c);
        return event;
    }


    @DeleteMapping("/remove-event/{event-id}")
    public void removeEvent(@PathVariable("event-id") Long chId) {
        eventService.deleteEvent(chId);
    }
    @PutMapping("/modify-event")
    public Event modifyEvent(@RequestBody Event c) {
        Event event = eventService.updateEvent(c);
        return event;
    }



    @GetMapping("/count/{idEvent}")
    public long getParticipantCount(@PathVariable Long idEvent) {
        return registrationService.getParticipantCountByEventId(idEvent);
    }

    @PostMapping("/add/{idEvent}/{idUser}")
    public ResponseEntity<?> registerUser(@RequestBody Registration registration,
                                          @PathVariable Long idEvent,
                                          @PathVariable Long idUser) {
        try {
            Registration newRegistration = registrationService.addRegistration(registration, idEvent, idUser);

            return ResponseEntity.ok(newRegistration);
        } catch (CustomException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    @PostMapping("/addfeedback/{idEvent}/{idUser}")
    public ResponseEntity<?> addFeedback(@RequestBody FeedBackEvent feedBackEvent,
                                         @PathVariable Long idEvent,
                                         @PathVariable Long idUser) {
        try {
            FeedBackEvent savedFeedback = feedBackEventService.addFeedBackEvent(feedBackEvent, idEvent, idUser);
            return ResponseEntity.ok(savedFeedback);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erreur: " + e.getMessage());
        }
    }



    @GetMapping("/feedbackevent/{eventId}")
    public ResponseEntity<List<FeedBackEvent>> getFeedbacksByEventId(@PathVariable Long eventId) {
        List<FeedBackEvent> feedbacks = feedBackEventService.getFeedbacksByEventId(eventId);
        return ResponseEntity.ok(feedbacks);
    }


    @DeleteMapping("/deletefeedback/{id}")
    public ResponseEntity<Void> deleteFeedBackEvent(@PathVariable Long id) {
        feedBackEventService.deleteFeedBackEvent(id);
        return ResponseEntity.noContent().build();
    }



    @GetMapping("/registration/{eventId}")
    public ResponseEntity<List<Registration>> getRegistrationByEventId(@PathVariable Long eventId) {
        List<Registration> registrations = registrationService.getRegistrationByEventId(eventId);
        return ResponseEntity.ok(registrations);
    }


    @DeleteMapping("/deleteregistration/{id}")
    public ResponseEntity<Void> deleteRegistration(@PathVariable Long id) {
        registrationService.deleteRegistration(id);
        return ResponseEntity.noContent().build();
    }



    @GetMapping("/checkparticipant/{idEvent}/{idUser}")
    public ResponseEntity<Boolean> checkUserParticipation(@PathVariable Long idEvent, @PathVariable Long idUser) {
        try {
            boolean isAlreadyRegistered = registrationService.dejaparticiper(idEvent, idUser);
            return ResponseEntity.ok(isAlreadyRegistered);
        } catch (CustomException ex) {
            return ResponseEntity.status(404).body(false);  // Event or User not found
        }
    }



    @PostMapping("/addplanning/{idEvent}/{idLocationEvent}")
    public ResponseEntity<Planning> addPlanning(@RequestBody Planning planning,
                                                @PathVariable Long idEvent,
                                                @PathVariable Long idLocationEvent) {
        Planning newPlanning = planningService.addPlanning(planning, idEvent, idLocationEvent);
        return ResponseEntity.ok(newPlanning);
    }

    @GetMapping("/planningevent/{eventId}")
    public ResponseEntity<List<Planning>> getPlanningByEventId(@PathVariable Long eventId) {
        List<Planning> plannings = planningService.getPlanningByEventId(eventId);
        return ResponseEntity.ok(plannings);
    }

    @DeleteMapping("/deleteplanning/{id}")
    public ResponseEntity<Void> deletePlanning(@PathVariable Long id) {
        planningService.deletePlanning(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/getplanning/{id}")
    public ResponseEntity<Planning> getPlanning(@PathVariable Long id) {

            Planning planning = planningService.retrievePlanning(id);
            return ResponseEntity.ok(planning);


    }

    @PutMapping("/updateplanning/{idEvent}/{idLocationEvent}")
    public Planning updatePlanning(@RequestBody Planning planning,@PathVariable Long idEvent, @PathVariable Long idLocationEvent) {
        return planningService.updatePlanning(planning,idEvent,idLocationEvent);
    }


    @GetMapping(value = "/qrcode/{eventId}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getEventQRCode(@PathVariable Long eventId) throws WriterException, IOException {
        try {
            byte[] qrCode = eventService.generateEventQRCode(eventId);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(qrCode);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/searchevent/{s}")
    public List<Event> searchEvents(@PathVariable("s") String s) {
        return eventService.searchEvents(s);
    }



    // ✅ Ajouter une location
    @PostMapping("/addlocation")
    public ResponseEntity<LocationEvent> addLocation(@RequestBody LocationEvent locationEvent) {
        LocationEvent savedLocation = locationEventService.addLocationEvent(locationEvent);
        return ResponseEntity.ok(savedLocation);
    }

    // ✅ Mettre à jour une location
    @PutMapping("/updatelocation")
    public ResponseEntity<LocationEvent> updateLocation(@RequestBody LocationEvent locationEvent) {
        LocationEvent updatedLocation = locationEventService.updateLocationEvent(locationEvent);
        return ResponseEntity.ok(updatedLocation);
    }

    // ✅ Récupérer une location par ID
    @GetMapping("/getlocation/{id}")
    public ResponseEntity<LocationEvent> getLocationById(@PathVariable Long id) {
        LocationEvent locationEvent = locationEventService.retrieveLocationEvent(id);
        return ResponseEntity.ok(locationEvent);
    }

    // ✅ Supprimer une location
    @DeleteMapping("/deletelocation/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        locationEventService.deleteLocationEvent(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ Récupérer toutes les locations
    @GetMapping("/alllocation")
    public ResponseEntity<List<LocationEvent>> getAllLocations() {
        List<LocationEvent> locations = locationEventService.getLocationEvent();
        return ResponseEntity.ok(locations);
    }
    }


