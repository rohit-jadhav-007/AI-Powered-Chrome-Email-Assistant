package com.email.writer.App;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/email")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class EmailGeneretorController {

    private final EmailGeneratorService emailGeneratorService;

        @PostMapping("/generate")
        public ResponseEntity<String> generateEmail(@RequestBody EmailRequest emailRequest) {
            String response = emailGeneratorService.GenerateEmailReply(emailRequest);
            return ResponseEntity.ok(response);
        }


}
