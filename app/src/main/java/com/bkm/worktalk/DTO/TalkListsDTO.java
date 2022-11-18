package com.bkm.worktalk.DTO;

public class TalkListsDTO {
    public String chatRoomPath = "";
    public String opponent = "";

    public String getChatRoomPath() {
        return chatRoomPath;
    }

    public void setChatRoomPath(String chatRoomPath) {
        this.chatRoomPath = chatRoomPath;
    }

    public String getOpponent() {
        return opponent;
    }

    public void setOpponent(String opponent) {
        this.opponent = opponent;
    }

    public TalkListsDTO() {

    }

    public TalkListsDTO(String chatRoomPath, String opponent) {
        this.chatRoomPath = chatRoomPath;
        this.opponent = opponent;
    }
}
