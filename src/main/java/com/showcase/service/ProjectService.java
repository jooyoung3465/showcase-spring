package com.showcase.service;

import com.showcase.dto.ProjectRequestDto;
import com.showcase.dto.ProjectResponseDto;
import com.showcase.entity.Project;
import com.showcase.exception.ProjectNotFoundException;
import com.showcase.repository.ProjectRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository repository;

    public ProjectService(ProjectRepository repository) {
        this.repository = repository;
    }

    /** 등록된 연도 목록 (최신순) */
    public List<Integer> getYears() {
        return repository.findDistinctYears();
    }

    public static final int PAGE_SIZE = 9;

    /** 연도별 프로젝트 목록 (페이지) */
    public List<ProjectResponseDto> getByYearPaged(Integer year, int page) {
        Page<Project> projects = repository.findByYearOrderByCreatedAtDesc(
                year, PageRequest.of(page, PAGE_SIZE));
        List<ProjectResponseDto> result = new ArrayList<ProjectResponseDto>();
        for (Project p : projects.getContent()) {
            result.add(ProjectResponseDto.from(p));
        }
        return result;
    }

    /** 연도+분야별 프로젝트 목록 (페이지) */
    public List<ProjectResponseDto> getByYearAndFieldPaged(Integer year, String field, int page) {
        Page<Project> projects = repository.findByYearAndFieldOrderByCreatedAtDesc(
                year, field, PageRequest.of(page, PAGE_SIZE));
        List<ProjectResponseDto> result = new ArrayList<ProjectResponseDto>();
        for (Project p : projects.getContent()) {
            result.add(ProjectResponseDto.from(p));
        }
        return result;
    }

    /** 연도별 전체 개수 */
    public long countByYear(Integer year) {
        return repository.countByYear(year);
    }

    /** 연도+분야별 전체 개수 */
    public long countByYearAndField(Integer year, String field) {
        return repository.countByYearAndField(year, field);
    }

    /** 연도별 프로젝트 목록 */
    public List<ProjectResponseDto> getByYear(Integer year) {
        List<Project> projects = repository.findByYearOrderByCreatedAtDesc(year);
        List<ProjectResponseDto> result = new ArrayList<ProjectResponseDto>();
        for (Project p : projects) {
            result.add(ProjectResponseDto.from(p));
        }
        return result;
    }

    /** 연도 + 분야별 프로젝트 목록 */
    public List<ProjectResponseDto> getByYearAndField(Integer year, String field) {
        List<Project> projects = repository.findByYearAndFieldOrderByCreatedAtDesc(year, field);
        List<ProjectResponseDto> result = new ArrayList<ProjectResponseDto>();
        for (Project p : projects) {
            result.add(ProjectResponseDto.from(p));
        }
        return result;
    }

    /** 단건 조회 — 없으면 ProjectNotFoundException */
    public ProjectResponseDto getById(Long id) {
        Project project = repository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));
        return ProjectResponseDto.from(project);
    }

    /** 어드민용 단건 Entity 조회 (수정 폼에 기존값 세팅용) */
    public ProjectRequestDto getRequestDtoById(Long id) {
        Project project = repository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));
        ProjectRequestDto dto = new ProjectRequestDto();
        dto.setYear(project.getYear());
        dto.setTitle(project.getTitle());
        dto.setTeamName(project.getTeamName());
        dto.setMembers(project.getMembers());
        dto.setField(project.getField());
        dto.setDescription(project.getDescription());
        dto.setYoutubeUrl(project.getYoutubeUrl());
        dto.setPdfUrl(project.getPdfUrl());
        dto.setAttachmentUrl(project.getAttachmentUrl());
        dto.setThumbnailUrl(project.getThumbnailUrl());
        dto.setTechStack(project.getTechStack());
        dto.setInfrastructure(project.getInfrastructure());
        dto.setSoftwareTools(project.getSoftwareTools());
        dto.setGithubUrl(project.getGithubUrl());
        dto.setBlogUrl(project.getBlogUrl());
        return dto;
    }

    /** 전체 목록 (어드민) */
    public List<ProjectResponseDto> getAll() {
        List<Project> projects = repository.findAll(
                Sort.by(Sort.Direction.DESC, "year", "createdAt")
        );
        List<ProjectResponseDto> result = new ArrayList<ProjectResponseDto>();
        for (Project p : projects) {
            result.add(ProjectResponseDto.from(p));
        }
        return result;
    }

    /** 프로젝트 저장 (신규) */
    @Transactional
    public ProjectResponseDto create(ProjectRequestDto dto) {
        applyYoutubeThumbnail(dto);
        Project project = toEntity(dto);
        return ProjectResponseDto.from(repository.save(project));
    }

    /** 프로젝트 수정 */
    @Transactional
    public ProjectResponseDto update(Long id, ProjectRequestDto dto) {
        applyYoutubeThumbnail(dto);
        Project project = repository.findById(id)
                .orElseThrow(() -> new ProjectNotFoundException(id));
        applyDtoToEntity(dto, project);
        return ProjectResponseDto.from(repository.save(project));
    }

    /** DTO → 신규 Entity 생성 */
    private Project toEntity(ProjectRequestDto dto) {
        Project p = new Project();
        applyDtoToEntity(dto, p);
        return p;
    }

    /** DTO 값을 기존 Entity에 반영 */
    private void applyDtoToEntity(ProjectRequestDto dto, Project p) {
        p.setYear(dto.getYear());
        p.setTitle(dto.getTitle());
        p.setTeamName(dto.getTeamName());
        p.setMembers(dto.getMembers());
        p.setField(dto.getField());
        p.setDescription(dto.getDescription());
        p.setYoutubeUrl(dto.getYoutubeUrl());
        p.setPdfUrl(dto.getPdfUrl());
        p.setAttachmentUrl(dto.getAttachmentUrl());
        p.setThumbnailUrl(dto.getThumbnailUrl());
        p.setTechStack(dto.getTechStack());
        p.setInfrastructure(dto.getInfrastructure());
        p.setSoftwareTools(dto.getSoftwareTools());
        p.setGithubUrl(dto.getGithubUrl());
        p.setBlogUrl(dto.getBlogUrl());
    }

    /**
     * YouTube URL에서 영상 ID를 추출해 썸네일 URL 자동 설정
     * - https://www.youtube.com/watch?v=VIDEO_ID
     * - https://youtu.be/VIDEO_ID
     */
    private void applyYoutubeThumbnail(ProjectRequestDto dto) {
        String youtube = dto.getYoutubeUrl();
        if (youtube == null || youtube.trim().isEmpty()) {
            return;
        }
        String videoId = null;

        // watch?v= 형식
        int vIdx = youtube.indexOf("v=");
        if (vIdx != -1) {
            String after = youtube.substring(vIdx + 2);
            int end = after.indexOf('&');
            videoId = (end == -1) ? after : after.substring(0, end);
        }

        // youtu.be/ 형식
        if (videoId == null && youtube.contains("youtu.be/")) {
            int slashIdx = youtube.indexOf("youtu.be/") + 9;
            String after = youtube.substring(slashIdx);
            int end = after.indexOf('?');
            videoId = (end == -1) ? after : after.substring(0, end);
        }

        if (videoId != null && !videoId.trim().isEmpty()) {
            dto.setThumbnailUrl(
                "https://img.youtube.com/vi/" + videoId.trim() + "/hqdefault.jpg"
            );
        }
    }

    /** 프로젝트 삭제 */
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ProjectNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
