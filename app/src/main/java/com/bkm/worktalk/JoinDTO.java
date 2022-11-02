package com.bkm.worktalk;

public class JoinDTO {

    public String emailId;
    public String pw;
    public String name;
    public String empno;
    public String hp;
    public String deptno;
    public String job;
    public String token;

    public JoinDTO() {

    }

    public JoinDTO(String emailId, String pw, String name, String empno, String hp, String deptno, String job, String token) {
        this.emailId = emailId;
        this.pw = pw;
        this.name = name;
        this.empno = empno;
        this.hp = hp;
        this.deptno = deptno;
        this.job = job;
        this.token = token;
    }
}
