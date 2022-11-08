package com.bkm.worktalk;

public class InnerProject_AddMemberDTO {

    public String memberListName;
    public String memberListEmail;
    public String memberListHP;

    public InnerProject_AddMemberDTO() {
    }

    //getter, setter 설정===========================================================================
    public String getMemberListName() {
        return memberListName;
    }

    public void setMemberListName(String memberListName) {
        this.memberListName = memberListName;
    }

    public String getMemberListEmail() {
        return memberListEmail;
    }

    public void setMemberListEmail(String memberListEmail) {
        this.memberListEmail = memberListEmail;
    }

    public String getMemberListHP() {
        return memberListHP;
    }

    public void setMemberListHP(String memberListHP) {
        this.memberListHP = memberListHP;
    }

    public InnerProject_AddMemberDTO(String memberListName, String memberListEmail, String memberListHP) {
        this.memberListName = memberListName;
        this.memberListEmail = memberListEmail;
        this.memberListHP = memberListHP;
    }

}
