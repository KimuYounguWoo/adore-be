package com.adminservice.user.controller;

import com.adminservice.global.CustomResponseCode;
import com.adminservice.user.dto.GetMemberListResponseDto;
import com.adminservice.user.dto.GetMemberResponseDto;
import com.adminservice.user.dto.MemberCreateRequestDto;
import com.adminservice.user.entity.SearchType;
import com.adminservice.user.service.MemberSearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/user")
public class UserController {

    private final MemberSearchService memberSearchService;


    @GetMapping("/lists/{page}")
    public ResponseEntity<GetMemberListResponseDto> searchUsers(
            @PathVariable("page") int page,
            @RequestParam("type") SearchType searchType,
            @RequestParam("keyword") String keyword) {
        GetMemberListResponseDto response = memberSearchService.searchUsers(searchType, keyword, page-1);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/create")
    public ResponseEntity<CustomResponseCode> createMember(
            @Valid @RequestBody MemberCreateRequestDto memberCreateRequestDto) {
        return memberSearchService.createMember(memberCreateRequestDto);
    }

    @PatchMapping("/update")
    public ResponseEntity<CustomResponseCode> updateMember(
            @Valid @RequestBody MemberCreateRequestDto memberCreateRequestDto, @RequestParam Long id) {
        return memberSearchService.updateMember(id, memberCreateRequestDto);
    }

    @GetMapping("/")
    public ResponseEntity<GetMemberResponseDto> viewMemberInfo(@RequestParam Long id) {
        return memberSearchService.getMember(id);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<CustomResponseCode> deleteMember(@RequestParam Long id) {
        return memberSearchService.deleteMember(id);
    }
}
