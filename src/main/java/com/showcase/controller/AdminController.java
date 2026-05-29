package com.showcase.controller;

import com.showcase.dto.ProjectRequestDto;
import com.showcase.service.FileStorageService;
import com.showcase.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/manage-sc2026")
public class AdminController {

    private final ProjectService service;
    private final FileStorageService fileStorage;

    public AdminController(ProjectService service, FileStorageService fileStorage) {
        this.service = service;
        this.fileStorage = fileStorage;
    }

    /** 전체 목록 */
    @GetMapping
    public String list(Model model) {
        model.addAttribute("projects", service.getAll());
        return "admin/list";
    }

    /** 신규 등록 폼 */
    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("project", new ProjectRequestDto());
        model.addAttribute("projectId", null);
        return "admin/form";
    }

    /** 신규 등록 처리 */
    @PostMapping("/new")
    public String create(
            @ModelAttribute ProjectRequestDto dto,
            @RequestParam(value = "pdfFile",    required = false) MultipartFile pdfFile,
            @RequestParam(value = "attachFile", required = false) MultipartFile attachFile
    ) throws IOException {
        applyFiles(dto, pdfFile, attachFile);
        service.create(dto);
        return "redirect:/manage-sc2026";
    }

    /** 수정 폼 */
    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("project", service.getRequestDtoById(id));
        model.addAttribute("projectId", id);
        return "admin/form";
    }

    /** 수정 처리 */
    @PostMapping("/{id}/edit")
    public String update(
            @PathVariable Long id,
            @ModelAttribute ProjectRequestDto dto,
            @RequestParam(value = "pdfFile",    required = false) MultipartFile pdfFile,
            @RequestParam(value = "attachFile", required = false) MultipartFile attachFile
    ) throws IOException {
        applyFiles(dto, pdfFile, attachFile);
        service.update(id, dto);
        return "redirect:/manage-sc2026";
    }

    /** 삭제 처리 */
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/manage-sc2026";
    }

    /**
     * 파일 처리 우선순위:
     * 1. 새 파일 업로드 → 저장 후 URL 덮어씀
     * 2. 삭제 플래그 → 기존 파일 삭제 + URL null 처리
     * 3. 아무것도 없으면 → 기존 URL 유지
     */
    private void applyFiles(ProjectRequestDto dto,
                            MultipartFile pdfFile,
                            MultipartFile attachFile) throws IOException {
        // PDF 처리
        String newPdfUrl = fileStorage.store(pdfFile);
        if (newPdfUrl != null) {
            // 새 파일로 교체 (기존 로컬 파일 삭제)
            fileStorage.delete(dto.getPdfUrl());
            dto.setPdfUrl(newPdfUrl);
        } else if (dto.isDeletePdf()) {
            // 삭제 플래그 — 파일 + URL 제거
            fileStorage.delete(dto.getPdfUrl());
            dto.setPdfUrl(null);
        }

        // 첨부파일 처리
        String newAttachUrl = fileStorage.store(attachFile);
        if (newAttachUrl != null) {
            fileStorage.delete(dto.getAttachmentUrl());
            dto.setAttachmentUrl(newAttachUrl);
        } else if (dto.isDeleteAttach()) {
            fileStorage.delete(dto.getAttachmentUrl());
            dto.setAttachmentUrl(null);
        }
    }
}
