package com.project.wms.entity.message;

/**
 * 微信消息实体
 */
public class WeChatMessage {
    
    String message = ""; //消息体
    String roomName = ""; //群聊名
    String name = ""; //发送人
    Boolean isRoom = false;
    String log = "";

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getRoom() {
        return isRoom;
    }

    public void setRoom(Boolean room) {
        isRoom = room;
    }
    
    public String getLog() {
        return log;
    }
    
    public void setLog(String log) {
        this.log = log;
    }
}
