package com.bkm.worktalk.DTO;

import java.util.HashMap;
import java.util.Map;

public class ChatRoom_DTO {

    public static class Comment {
        public String userName = "";
        public String userContents = "";
        public Map<String, Object> readUsers = new HashMap<>();
        public String createdTime = "";
        public String timestamp = "";
    }

    public static class Comment2 {
        public String userName = "";
        public String userContents = "";
        public Map<String, Object> readUsers = new HashMap<>();
        public String createdTime = "";
        public String timestamp = "";
        public String userEmail = "";
    }

    public ChatRoom_DTO() {

    }
}