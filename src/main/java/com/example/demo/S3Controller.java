package com.example.demo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/s3")
@RequiredArgsConstructor
@Tag(name = "S3", description = "Operações com arquivos no bucket S3 (via LocalStack)")
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping(value = "/upload", consumes = "multipart/form-data")
    @Operation(summary = "Faz upload de um arquivo para o S3")
    public ResponseEntity<String> upload(
            @Parameter(description = "Arquivo para upload", required = true)
            @RequestPart("file") MultipartFile file) {
        s3Service.uploadFile(file);
        return ResponseEntity.ok("Arquivo enviado com sucesso!");
    }

    @Operation(summary = "Lista todos os arquivos no bucket")
    @GetMapping("/files")
    public ResponseEntity<List<String>> listFiles() {
        List<String> files = s3Service.listFiles();
        return ResponseEntity.ok(files);
    }

    @Operation(summary = "Faz download de um arquivo do S3")
    @GetMapping("/download/{key}")
    public ResponseEntity<byte[]> download(
            @Parameter(description = "Nome do arquivo (chave S3)", required = true)
            @PathVariable String key) throws IOException {
        byte[] content = s3Service.downloadFile(key);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + key)
                .body(content);
    }
}
