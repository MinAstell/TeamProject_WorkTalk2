package com.bkm.worktalk;

public class NotificationModel {

    public String to;

    public Notification notification = new Notification();
    public Data data = new Data();

    public static class Notification {
        public String title;
        public String body;
    }

    public static class Data {
        public String title;
        public String body;
        public String sendingUser;  // 보낸사람 이름
        public String chatRoomPath;  // 채팅방 경로
        public String receiver;  // 받는사람 이름
        public String receiverUid;  // 받는사람 고유번호
    }
}
