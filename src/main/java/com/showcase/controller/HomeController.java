package com.showcase.controller;

import com.showcase.dto.ProjectResponseDto;
import com.showcase.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    private final ProjectService service;

    public HomeController(ProjectService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(required = false) Integer year,
                        @RequestParam(required = false) String field,
                        @RequestParam(required = false, defaultValue = "1") int page) {

        List<Integer> years = service.getYears();

        if (years.isEmpty()) {
            model.addAttribute("years", years);
            model.addAttribute("projects", new ArrayList<ProjectResponseDto>());
            model.addAttribute("allProjects", new ArrayList<ProjectResponseDto>());
            model.addAttribute("activeYear", null);
            model.addAttribute("activeField", "전체");
            model.addAttribute("currentPage", 1);
            model.addAttribute("totalPages", 0);
            return "index";
        }

        int activeYear = (year != null) ? year : years.get(0);
        String activeField = (field != null && !field.isEmpty()) ? field : "전체";

        int currentPage = Math.max(1, page);
        int pageIndex = currentPage - 1; // 0-based

        List<ProjectResponseDto> projects = "전체".equals(activeField)
                ? service.getByYearPaged(activeYear, pageIndex)
                : service.getByYearAndFieldPaged(activeYear, activeField, pageIndex);

        long totalCount = "전체".equals(activeField)
                ? service.countByYear(activeYear)
                : service.countByYearAndField(activeYear, activeField);
        int totalPages = (int) Math.ceil((double) totalCount / ProjectService.PAGE_SIZE);

        List<ProjectResponseDto> allOfYear = service.getByYear(activeYear);

        // 슬라이더: 최신 연도 기준 썸네일 있는 프로젝트 최대 5개
        List<ProjectResponseDto> sliderProjects = new ArrayList<ProjectResponseDto>();
        for (ProjectResponseDto p : service.getByYear(years.get(0))) {
            if (p.getThumbnailUrl() != null && !p.getThumbnailUrl().isEmpty()) {
                sliderProjects.add(p);
                if (sliderProjects.size() >= 5) break;
            }
        }

        model.addAttribute("years", years);
        model.addAttribute("projects", projects);
        model.addAttribute("activeYear", activeYear);
        model.addAttribute("activeField", activeField);
        model.addAttribute("allProjects", allOfYear);
        model.addAttribute("sliderProjects", sliderProjects);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);

        return "index";
    }

    @GetMapping("/project/{id}")
    public String detail(@PathVariable Long id, Model model) {
        ProjectResponseDto project = service.getById(id);

        List<ProjectResponseDto> allOfYear = service.getByYear(project.getYear());
        List<ProjectResponseDto> related = new ArrayList<ProjectResponseDto>();
        for (ProjectResponseDto p : allOfYear) {
            if (!p.getId().equals(id)) {
                related.add(p);
            }
        }
        if (related.size() > 3) {
            related = related.subList(0, 3);
        }

        model.addAttribute("project", project);
        model.addAttribute("related", related);
        model.addAttribute("embedUrl", project.getEmbedUrl());
        return "detail";
    }
}
