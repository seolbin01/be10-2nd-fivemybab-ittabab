package com.fivemybab.ittabab.user.command.application.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FriendRequestDTO {

    private Long fromUserId;
    private Long toUserId;
}