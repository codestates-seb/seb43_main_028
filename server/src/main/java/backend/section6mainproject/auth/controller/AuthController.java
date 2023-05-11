package backend.section6mainproject.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class AuthController {



    @GetMapping("/members/refresh")
    public ResponseEntity<?> getRefreshToken() {
        return ResponseEntity.ok().build();
    }


}
