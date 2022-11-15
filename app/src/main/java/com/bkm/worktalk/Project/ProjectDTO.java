package com.bkm.worktalk.Project;

public class ProjectDTO {

    String projectName; //프로젝트 이름
    String ProjectExplain; //프로젝트 설명
    String ProjectNameForChange; //프로젝트 이름 변경용 변수

    public ProjectDTO(){} // 생성자 메서드

    //getter, setter 설정===========================================================================
    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectExplain() {
        return ProjectExplain;
    }

    public void setProjectExplain(String projectExplain) {
        ProjectExplain = projectExplain;
    }

    public String getProjectNameForChange() {
        return ProjectNameForChange;
    }

    public void setProjectNameForChange(String projectNameForChange) {
        ProjectNameForChange = projectNameForChange;
    }

    //값을 추가할때 쓰는 함수==========================================================================
    public ProjectDTO(String projectName, String ProjectExplain, String ProjectNameForChange){
        this.projectName = projectName;
        this.ProjectExplain = ProjectExplain;
        this.ProjectNameForChange = ProjectNameForChange;
    }

}
