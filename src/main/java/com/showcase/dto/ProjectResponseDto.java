package com.showcase.dto;

import com.showcase.entity.Project;

/**
 * 뷰에 전달할 프로젝트 응답 DTO
 * Entity를 직접 노출하지 않고 필요한 필드만 전달
 */
public class ProjectResponseDto {

    private Long id;
    private Integer year;
    private String title;
    private String teamName;
    private String members;
    private String field;
    private String description;
    private String youtubeUrl;
    private String embedUrl;   // youtube watch URL을 embed URL로 변환
    private String pdfUrl;
    private String attachmentUrl;
    private String thumbnailUrl;
    private String techStack;
    private String infrastructure;
    private String softwareTools;
    private String githubUrl;
    private String blogUrl;

    public ProjectResponseDto() {}

    /** Entity -> DTO 변환 */
    public static ProjectResponseDto from(Project project) {
        ProjectResponseDto dto = new ProjectResponseDto();
        dto.id            = project.getId();
        dto.year          = project.getYear();
        dto.title         = project.getTitle();
        dto.teamName      = project.getTeamName();
        dto.members       = project.getMembers();
        dto.field         = project.getField();
        dto.description   = project.getDescription();
        dto.youtubeUrl     = project.getYoutubeUrl();
        dto.pdfUrl         = project.getPdfUrl();
        dto.attachmentUrl  = project.getAttachmentUrl();
        dto.thumbnailUrl   = project.getThumbnailUrl();
        dto.techStack      = project.getTechStack();
        dto.infrastructure = project.getInfrastructure();
        dto.softwareTools  = project.getSoftwareTools();
        dto.githubUrl      = project.getGithubUrl();
        dto.blogUrl        = project.getBlogUrl();

        // YouTube watch?v= -> embed/ 변환
        if (project.getYoutubeUrl() != null && !project.getYoutubeUrl().isEmpty()) {
            dto.embedUrl = project.getYoutubeUrl().replace("watch?v=", "embed/");
        }
        return dto;
    }

    public Long getId() { return id; }
    public Integer getYear() { return year; }
    public String getTitle() { return title; }
    public String getTeamName() { return teamName; }
    public String getMembers() { return members; }
    public String getField() { return field; }
    public String getDescription() { return description; }
    public String getYoutubeUrl() { return youtubeUrl; }
    public String getEmbedUrl() { return embedUrl; }
    public String getPdfUrl() { return pdfUrl; }
    public String getAttachmentUrl() { return attachmentUrl; }
    public String getThumbnailUrl() { return thumbnailUrl; }
    public String getTechStack() { return techStack; }
    public String getInfrastructure() { return infrastructure; }
    public String getSoftwareTools() { return softwareTools; }
    public String getGithubUrl() { return githubUrl; }
    public String getBlogUrl() { return blogUrl; }
}
