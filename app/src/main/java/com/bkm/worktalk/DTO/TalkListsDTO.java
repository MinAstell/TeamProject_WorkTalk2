package com.bkm.worktalk.DTO;

public class TalkListsDTO {
    public String chatRoomPath;
    public String opponent;

    public TalkListsDTO() {

    }

    public TalkListsDTO(String chatRoomPath, String opponent) {
        this.chatRoomPath = chatRoomPath;
        this.opponent = opponent;
    }
}
