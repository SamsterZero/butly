package in.vvm.butly.controller;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import in.vvm.butly.model.ShortenRequest;
import in.vvm.butly.model.ShortenResponse;
import in.vvm.butly.service.ButlyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
class ButlyController {

    private final ButlyService butlyService;

    @PostMapping("/api/urls")
    public ResponseEntity<ShortenResponse> butly(
            @Valid @RequestBody ShortenRequest req) {
        ShortenResponse res = butlyService.shortenUrl(req);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(res);
    }

    @GetMapping("/{shortUrl}")
    public ResponseEntity<Void> redirect(@PathVariable String shortUrl) {
        String longUrl = butlyService.resolveShortUrl(shortUrl);
        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(URI.create(longUrl))
                .build();
    }

}