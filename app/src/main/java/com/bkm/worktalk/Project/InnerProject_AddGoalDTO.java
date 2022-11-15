package com.bkm.worktalk.Project;

public class InnerProject_AddGoalDTO {

    public String goalName; //프로젝트 목표명
    public String goalNameForChange; //프로젝트 목표명(수정용)
    public String goalExplain; //프로젝트 목표 내용(설명)
    public static boolean isGoalSuccessed; //프로젝트 목표 달성 여부

    public InnerProject_AddGoalDTO() {
    }

    //getter, setter 설정===========================================================================
    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public String getGoalNameForChange() {
        return goalNameForChange;
    }

    public void setGoalNameForChange(String goalNameForChange) {
        this.goalNameForChange = goalNameForChange;
    }

    public String getGoalExplain() {
        return goalExplain;
    }

    public void setGoalExplain(String goalExplain) {
        this.goalExplain = goalExplain;
    }

    public boolean getGoalSuccessed() {
        return isGoalSuccessed;
    }

    public static void setGoalSuccessed(boolean goalSuccessed) {
        isGoalSuccessed = goalSuccessed;
    }

    //값을 추가할때 쓰는 함수==========================================================================
    public InnerProject_AddGoalDTO(String goalName, String goalNameForChange, String goalExplain, boolean isGoalSuccessed) {
        this.goalName = goalName;
        this.goalNameForChange = goalNameForChange;
        this.goalExplain = goalExplain;
        this.isGoalSuccessed = isGoalSuccessed;
    }

}
