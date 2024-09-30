package com.fivemybab.ittabab.board.query.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.fivemybab.ittabab.board.query.dto.PostQueryDto;
import com.fivemybab.ittabab.board.query.service.PostQueryService;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.javassist.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
@Tag(name = "Board", description = "게시판 관련 API")
public class PostQueryController {

    private final PostQueryService postQueryService;

    /* 게시물 목록 조회 (최신순) */
    @Operation(summary = "게시글 목록 조회(최신순)")
    @GetMapping("/posts/time")
    public ResponseEntity<List<PostQueryDto>> getPostsByTime() throws NotFoundException {
        List<PostQueryDto> posts = postQueryService.findPostsByTime();
        return ResponseEntity.ok(posts);
    }

    /* 게시물 목록 조회 (좋아요 내림차순) */
    @Operation(summary = "게시글 목록 조회(좋아요 내림차순)")
    @GetMapping("/posts/likes/desc")
    public ResponseEntity<List<PostQueryDto>> getPostsByLikesDesc() throws NotFoundException {
        List<PostQueryDto> posts = postQueryService.findPostsByLikesDesc();
        return ResponseEntity.ok(posts);
    }

    /* 게시물 목록 조회 (좋아요 오름차순) */
    @Operation(summary = "게시글 목록 조회(좋아요 오름차순)")
    @GetMapping("/posts/likes/asc")
    public ResponseEntity<List<PostQueryDto>> getPostsByLikesAsc() throws NotFoundException {
        List<PostQueryDto> posts = postQueryService.findPostsByLikesAsc();
        return ResponseEntity.ok(posts);
    }



/*    *//* 게시물 ID로 조회 *//*
    @GetMapping("/post/{postId}")
    public ResponseEntity<BoardQueryDto> getPostById(@PathVariable Long postId) throws NotFoundException {
        BoardQueryDto post = boardQueryService.findPostById(postId);
        return ResponseEntity.ok(post);
    }*/
}