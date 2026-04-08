package com.ocr.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ocr.backend.dto.DocumentDtos.ActionResponse;
import com.ocr.backend.dto.DocumentDtos.DocumentDetailResponse;
import com.ocr.backend.dto.DocumentDtos.DocumentListResponse;
import com.ocr.backend.service.MockPlatformService;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private final MockPlatformService platformService;

    public DocumentController(MockPlatformService platformService) {
        this.platformService = platformService;
    }

    @GetMapping
    public DocumentListResponse list() {
        return platformService.getDocuments();
    }

    @GetMapping("/{documentId}")
    public DocumentDetailResponse detail(@PathVariable String documentId) {
        return platformService.getDocument(documentId);
    }

    @PostMapping("/{documentId}/reprocess")
    public ActionResponse reprocess(@PathVariable String documentId) {
        return platformService.reprocessDocument(documentId);
    }

    @PostMapping("/{documentId}/reviews/approve")
    public ActionResponse approve(@PathVariable String documentId) {
        return platformService.approveDocument(documentId);
    }

    @PostMapping("/{documentId}/reviews/reject")
    public ActionResponse reject(@PathVariable String documentId) {
        return platformService.rejectDocument(documentId);
    }
}

