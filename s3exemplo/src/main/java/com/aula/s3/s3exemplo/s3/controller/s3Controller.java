package com.aula.s3.s3exemplo.s3.controller;

import com.aula.s3.s3exemplo.s3.service.S3Service;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.HeadObjectResponse;

import java.io.IOException;

@RestController
@RequestMapping("/s3")
public class s3Controller {
    private final S3Service service;

    private s3Controller(S3Service service){
        this.service = service;
    }

    @GetMapping("/list")
    public ResponseEntity<S3Service.ListResult> list(
            @RequestParam(value = "prefix", required = false) String prefix
    ){
        return ResponseEntity.ok(service.list(prefix));
    }
    @PostMapping("/upload")
    public ResponseEntity<String> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "prefix", required = false) String prefix
    ) throws IOException {
        service.upload(file, prefix);
        return ResponseEntity.ok("Upload realizado com sucesso");
    }

    @GetMapping("/file")
    public ResponseEntity<InputStreamResource> getFile(
            @RequestParam("key") String key
    ) {
        ResponseInputStream<GetObjectResponse> stream = service.getFile(key);
        GetObjectResponse meta = stream.response();

        String filename = key.contains("/")
                ? key.substring(key.lastIndexOf('/') + 1)
                : key;

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(
                        meta.contentType() != null
                                ? meta.contentType()
                                : MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + filename + "\"")
                .body(new InputStreamResource(stream));
    }
    @GetMapping("/metadata")
    public ResponseEntity<?> getMetadata(@RequestParam("key") String key) {
        HeadObjectResponse meta = service.getMetadata(key);
        return ResponseEntity.ok(new MetadataResponse(
                key,
                meta.contentLength(),
                meta.contentType(),
                meta.lastModified() != null ? meta.lastModified().toString() : null,
                meta.eTag()
        ));
    }

    public record MetadataResponse(
            String key,
            Long sizeBytes,
            String contentType,
            String lastModified,
            String eTag
    ) {}
}
