package com.showcase.exception;

/**
 * 프로젝트를 찾을 수 없을 때 발생하는 커스텀 예외
 */
public class ProjectNotFoundException extends RuntimeException {

    private final Long projectId;

    public ProjectNotFoundException(Long id) {
        super("프로젝트를 찾을 수 없습니다. ID: " + id);
        this.projectId = id;
    }

    public Long getProjectId() {
        return projectId;
    }
}
