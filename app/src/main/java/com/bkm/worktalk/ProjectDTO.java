package com.bkm.worktalk;

public class ProjectDTO {

    String projectName; //프로젝트 이름
    String ProjectExplain; //프로젝트 설명

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

    //값을 추가할때 쓰는 함수==========================================================================
    public ProjectDTO(String projectName, String ProjectExplain){
        this.projectName = projectName;
        this.ProjectExplain = ProjectExplain;
    }

}
