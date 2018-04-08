package jy.sopt.chattingsample;

import java.util.List;

/**
 * Created by jyoung on 2017. 12. 18..
 */

public class Room {

    private String roomName;
    private String leaderName;
    private List<String> numberOfPeople;
    private ChatDetail content;
    private int badge;
    private String recentMessage;
    private String recentMessageTime;

    public Room() {
    }

    public Room(String roomName, String leaderName, List<String> numberOfPeople, ChatDetail content, int badge, String recentMessage, String recentMessageTime) {
        this.roomName = roomName;
        this.leaderName = leaderName;
        this.numberOfPeople = numberOfPeople;
        this.content = content;
        this.badge = badge;
        this.recentMessage = recentMessage;
        this.recentMessageTime = recentMessageTime;
    }

    public String getRecentMessage() {
        return recentMessage;
    }

    public void setRecentMessage(String recentMessage) {
        this.recentMessage = recentMessage;
    }

    public String getRecentMessageTime() {
        return recentMessageTime;
    }

    public void setRecentMessageTime(String recentMessageTime) {
        this.recentMessageTime = recentMessageTime;
    }

    public int getBadge() {
        return badge;
    }

    public void setBadge(int badge) {
        this.badge = badge;
    }

    public ChatDetail getContent() {
        return content;
    }

    public void setContent(ChatDetail content) {
        this.content = content;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public List<String> getNumberOfPeople() {
        return numberOfPeople;
    }

    public void setNumberOfPeople(List<String> numberOfPeople) {
        this.numberOfPeople = numberOfPeople;
    }
}
