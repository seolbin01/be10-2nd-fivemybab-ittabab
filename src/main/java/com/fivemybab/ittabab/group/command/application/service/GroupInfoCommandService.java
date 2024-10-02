package com.fivemybab.ittabab.group.command.application.service;

import com.fivemybab.ittabab.group.command.domain.aggregate.ChatRoomStatus;
import com.fivemybab.ittabab.group.command.domain.aggregate.GroupInfo;
import com.fivemybab.ittabab.group.command.domain.aggregate.GroupUser;
import com.fivemybab.ittabab.group.command.domain.repository.GroupCommentRepository;
import com.fivemybab.ittabab.group.command.domain.repository.GroupInfoRepository;
import com.fivemybab.ittabab.group.command.domain.repository.GroupUserRepository;
import com.fivemybab.ittabab.group.query.dto.ChatMessageDto;
import com.fivemybab.ittabab.group.query.dto.GroupInfoDto;
import com.fivemybab.ittabab.group.query.dto.GroupUserDto;
import com.fivemybab.ittabab.group.query.service.GroupInfoQueryService;
import com.fivemybab.ittabab.user.command.application.dto.CreateNotificationRequest;
import com.fivemybab.ittabab.user.command.application.service.NotificationCommandService;
import com.fivemybab.ittabab.user.query.service.UserQueryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupInfoCommandService {

    private final GroupInfoRepository groupInfoRepository;
    private final GroupUserRepository groupUserRepository;
    private final GroupCommentRepository groupCommentRepository;

    private final ModelMapper modelMapper;

    private final UserQueryService userQueryService;
    private final GroupInfoQueryService groupInfoQueryService;
    private final GroupInfoCommandService groupInfoCommandService;
    private final NotificationCommandService notificationCommandService;


    public GroupInfoDto findGroupByGroupId(Long groupId) {
        return groupInfoQueryService.findGroupByGroupId(groupId);
    }

    /* 모임 등록 */
    @Transactional
    public void registGroup(GroupInfoDto newGroupInfo) {
        groupInfoRepository.save(modelMapper.map(newGroupInfo, GroupInfo.class));
    }

    /* 모임 삭제 */
    @Transactional
    public void deleteGroupInfo(Long groupId) {

        // 1. 모임에 달린 댓글 모두 삭제
        groupCommentRepository.deleteByGroupId(groupId);

        // 2. 모임에 가입된 사용자 삭제
        groupUserRepository.deleteByGroupId(groupId);

        // 3. 모임 삭제
        groupInfoRepository.deleteById(groupId);
    }

    /* 모임에 가입된 사용자 아이디 가져오는 메소드 */
    public List<Long> findGroupUserByGroupId(Long groupId) {
        return groupInfoQueryService.findGroupUserByGroupId(groupId);
    }

    /* 모임에 신규 사용자 가입 메소드 */
    public void registGroupUser(Long userId, Long groupId) {
        GroupUserDto newGroupUser = new GroupUserDto(null, userId, groupId);

        groupUserRepository.save(modelMapper.map(newGroupUser, GroupUser.class));
    }

    /* 모임 채팅방 생성 */
    public void createChat(ChatMessageDto chatMessageDto, Authentication loginId) {

        chatMessageDto.setSenderId(userQueryService.findUserIdByLoginId(loginId).get().getUserId());

        // 1. 모임 회원들에게 알림을 보낸다.
        CreateNotificationRequest notificationRequest = new CreateNotificationRequest();
        String content = chatMessageDto.getChatRoomId() + "모임의 채팅방이 생성되었습니다. 채팅방에 참여하세요!";
        List<Long> userList = groupInfoQueryService.findGroupUserByGroupId(chatMessageDto.getChatRoomId());

        notificationRequest.setContent(content);
        notificationRequest.setTarget("GROUP");
        notificationRequest.setTargetId(chatMessageDto.getChatRoomId());
        notificationRequest.setUserIdList(userList);

        notificationCommandService.createNotification(notificationRequest);

        // 2. 해당 모임의 채팅방을 생성 상태로 변경한다.

        GroupInfo foundGroupInfo = groupInfoRepository.findById(chatMessageDto.getChatRoomId()).orElse(null);

        if(foundGroupInfo != null) {
            foundGroupInfo.modifyChatRoomStatus(ChatRoomStatus.Created);
        }
    }

    /* 모임 채팅방에 메시지 전송*/
    public void sendChat(ChatMessageDto chatMessageDto, Authentication loginId) {
        chatMessageDto.setSenderId(userQueryService.findUserIdByLoginId(loginId).get().getUserId());
        List<Long> userList = groupInfoQueryService.findGroupUserByGroupId(chatMessageDto.getChatRoomId());
    }
}
