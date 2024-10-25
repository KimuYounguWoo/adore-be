package com.adminservice.user.service;

import com.adminservice.global.CustomResponseCode;
import com.adminservice.user.dto.GetMemberListResponseDto;
import com.adminservice.user.dto.GetMemberResponseDto;
import com.adminservice.user.dto.MemberCreateRequestDto;
import com.adminservice.user.entity.SearchType;
import org.springframework.http.ResponseEntity;

public interface MemberSearchService {
    ResponseEntity<CustomResponseCode> createMember(MemberCreateRequestDto memberCreateRequestDto);
    ResponseEntity<CustomResponseCode> updateMember(Long id, MemberCreateRequestDto memberCreateRequestDto);
    ResponseEntity<GetMemberResponseDto> getMember(Long id);
    ResponseEntity<CustomResponseCode> deleteMember(Long id);
    GetMemberListResponseDto searchUsers(SearchType searchType, String keyword, int page);
}
