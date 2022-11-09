package com.bkm.worktalk;

public class InnerProject_AddMemberDTO {

    public String email;
    public String hp;
    public String name;

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

    public InnerProject_AddMemberDTO(String email, String hp, String name) {
        this.email = email;
        this.hp = hp;
        this.name = name;
    }

}
