package com.bkm.worktalk.DTO;

import java.util.HashMap;

public class JoinDTO {

    public String emailId;
    public String pw;
    public String name;
    public String empno;
    public String hp;
    public String deptno;
    public String job;
    public String token;
    public String profileImageUrl;

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmpno() {
        return empno;
    }

    public void setEmpno(String empno) {
        this.empno = empno;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getDeptno() {
        return deptno;
    }

    public void setDeptno(String deptno) {
        this.deptno = deptno;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public JoinDTO() {

    }

    public JoinDTO(String emailId, String pw, String name, String empno, String hp, String deptno, String job, String token, String profileImageUrl) {
        this.emailId = emailId;
        this.pw = pw;
        this.name = name;
        this.empno = empno;
        this.hp = hp;
        this.deptno = deptno;
        this.job = job;
        this.token = token;
        this.profileImageUrl = profileImageUrl;
    }
}
