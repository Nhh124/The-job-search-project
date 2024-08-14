package com.example.asm2.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;

@RequestMapping("/resources")
@RestController
public class LoadImageController {

        public final  String UPLOAD_DIRECTORY_USER = System.getProperty("user.dir") + "/src/main/resources/static/images/avatar/";
        public final  String UPLOAD_DIRECTORY_COMPANY = System.getProperty("user.dir") + "/src/main/resources/static/images/company-logo/";

        @GetMapping("/images")
        public ResponseEntity<InputStreamResource> downloadUserImage(@RequestParam("name") String fileName)
                throws Exception {
            final HttpHeaders httpHeaders = new HttpHeaders();

            File file = new File(UPLOAD_DIRECTORY_USER + fileName);
            if (!file.exists()) {
                throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
            }
            final InputStream inputStream = Files.newInputStream(file.toPath());
            final InputStreamResource resource = new InputStreamResource(inputStream);
            httpHeaders.set(HttpHeaders.LAST_MODIFIED, String.valueOf(file.lastModified()));
            httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"");
            httpHeaders.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()));
            return ResponseEntity.ok()
                    .headers(httpHeaders)
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        }

    @GetMapping("/company-logo")
    public ResponseEntity<InputStreamResource> downloadCompanyLogo(@RequestParam("name") String fileName) throws Exception {
        final HttpHeaders httpHeaders = new HttpHeaders();
        File file = new File(UPLOAD_DIRECTORY_COMPANY + fileName);

        if (!file.exists()) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST);
        }

        final InputStream inputStream = Files.newInputStream(file.toPath());
        final InputStreamResource resource = new InputStreamResource(inputStream);
        httpHeaders.set(HttpHeaders.LAST_MODIFIED, String.valueOf(file.lastModified()));
        httpHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getName() + "\"");
        httpHeaders.set(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()));

        return ResponseEntity.ok()
                .headers(httpHeaders)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
