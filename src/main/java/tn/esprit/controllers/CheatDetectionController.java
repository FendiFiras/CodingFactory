package tn.esprit.controllers;

import tn.esprit.services.CheatDetectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cheat")
public class CheatDetectionController {

    @Autowired
    CheatDetectionService cheatDetectionService;

    @PostMapping("/detect")
    public boolean detect(@RequestBody Map<String, Object> payload) {
        return cheatDetectionService.detectCheating(payload);
    }
}