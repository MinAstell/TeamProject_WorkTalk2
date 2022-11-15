package com.bkm.worktalk.Project;

public class InnerProject_AddMemberDTO {

    public String email; //멤버 이메일
    public String hp; //멤버 전화번호
    public String name; //멤버 이름

    public InnerProject_AddMemberDTO() {
    }

    //getter, setter 설정===========================================================================
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //값을 추가할때 쓰는 함수==========================================================================
    public InnerProject_AddMemberDTO(String email, String hp, String name) {
        this.email = email;
        this.hp = hp;
        this.name = name;
    }

}
