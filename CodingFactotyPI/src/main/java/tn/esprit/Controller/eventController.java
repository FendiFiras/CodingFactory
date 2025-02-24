package tn.esprit.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.Services.*;
import tn.esprit.entities.Event;

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

}
