package com.showcase.dto;

/**
 * 프로젝트 등록/수정 시 폼 데이터를 받는 DTO
 * Entity를 직접 폼에 바인딩하지 않기 위해 분리
 */
public class ProjectRequestDto {

    private Integer year;
    private String title;
    private String teamName;
    private String members;
    private String field;
    private String description;
    private String youtubeUrl;
    private String pdfUrl;
    private String attachmentUrl;
    private String thumbnailUrl;
    private String techStack;
    private String infrastructure;
    private String softwareTools;
    private String githubUrl;
    private String blogUrl;

    // 파일 삭제 플래그 (체크되면 기존 파일 URL 삭제)
    private boolean deletePdf;
    private boolean deleteAttach;

    public ProjectRequestDto() {}

    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getTeamName() { return teamName; }
    public void setTeamName(String teamName) { this.teamName = teamName; }

    public String getMembers() { return members; }
    public void setMembers(String members) { this.members = members; }

    public String getField() { return field; }
    public void setField(String field) { this.field = field; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getYoutubeUrl() { return youtubeUrl; }
    public void setYoutubeUrl(String youtubeUrl) { this.youtubeUrl = youtubeUrl; }

    public String getPdfUrl() { return pdfUrl; }
    public void setPdfUrl(String pdfUrl) { this.pdfUrl = pdfUrl; }

    public String getAttachmentUrl() { return attachmentUrl; }
    public void setAttachmentUrl(String attachmentUrl) { this.attachmentUrl = attachmentUrl; }

    public String getThumbnailUrl() { return thumbnailUrl; }
    public void setThumbnailUrl(String thumbnailUrl) { this.thumbnailUrl = thumbnailUrl; }

    public String getTechStack() { return techStack; }
    public void setTechStack(String techStack) { this.techStack = techStack; }

    public String getInfrastructure() { return infrastructure; }
    public void setInfrastructure(String infrastructure) { this.infrastructure = infrastructure; }

    public String getSoftwareTools() { return softwareTools; }
    public void setSoftwareTools(String softwareTools) { this.softwareTools = softwareTools; }

    public String getGithubUrl() { return githubUrl; }
    public void setGithubUrl(String githubUrl) { this.githubUrl = githubUrl; }

    public String getBlogUrl() { return blogUrl; }
    public void setBlogUrl(String blogUrl) { this.blogUrl = blogUrl; }

    public boolean isDeletePdf() { return deletePdf; }
    public void setDeletePdf(boolean deletePdf) { this.deletePdf = deletePdf; }

    public boolean isDeleteAttach() { return deleteAttach; }
    public void setDeleteAttach(boolean deleteAttach) { this.deleteAttach = deleteAttach; }
}
