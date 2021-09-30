package com.skillbox.socialnetwork.api.response.friendsDTO;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

public enum FriendshipDTO {;
    private interface Error {String getError();}
    private interface Timestamp {LocalDateTime getTimestamp();}
    private interface Message {String getMessage();}

    private interface Total {Integer getTotal();}
    private interface Offset {Integer getOffset();}
    private interface PerPage {Integer getPerPage();}
    private interface Friends {Set<FriendsDTO> getFriends();}

    public enum Response{;
        @Data
        public static class FriendsList implements Error, Timestamp, Total, Offset, PerPage, Friends {

            private String error;

            private LocalDateTime timestamp;

            private Integer total;

            private Integer offset;

            private Integer perPage;

            private Set<FriendsDTO> friends;

            @Override
            public String getError() {
                return error;
            }

            public void setError(String error) {
                this.error = error;
            }

            @Override
            public LocalDateTime getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(LocalDateTime timestamp) {
                this.timestamp = timestamp;
            }

            @Override
            public Integer getTotal() {
                return total;
            }

            public void setTotal(Integer total) {
                this.total = total;
            }

            @Override
            public Integer getOffset() {
                return offset;
            }

            public void setOffset(Integer offset) {
                this.offset = offset;
            }

            @Override
            public Integer getPerPage() {
                return perPage;
            }

            public void setPerPage(Integer perPage) {
                this.perPage = perPage;
            }

            @Override
            public Set<FriendsDTO> getFriends() {
                return friends;
            }

            public void setFriends(Set<FriendsDTO> friends) {
                this.friends = friends;
            }
        }

        @Data
        public static class FriendsResponse200 implements Error, Timestamp, Message {

            private String error;

            private LocalDateTime timestamp;

            private String message;


            @Override
            public String getError() {
                return error;
            }

            public void setError(String error) {
                this.error = error;
            }

            @Override
            public LocalDateTime getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(LocalDateTime timestamp) {
                this.timestamp = timestamp;
            }

            @Override
            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }
        }
    }
}
