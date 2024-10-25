package com.adminservice.user.service;

import com.adminservice.global.CustomException;
import com.adminservice.global.CustomResponseCode;
import com.adminservice.global.ResponseCode;
import com.adminservice.user.dto.GetMemberListResponseDto;
import com.adminservice.user.dto.GetMemberResponseDto;
import com.adminservice.user.dto.MemberCreateRequestDto;
import com.adminservice.user.entity.Member;
import com.adminservice.user.entity.MemberState;
import com.adminservice.user.entity.SearchType;
import com.adminservice.user.repository.MemberRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberSearchServiceImpl implements MemberSearchService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> deleteMember(Long id) {
        Member member = memberRepository.findByIdAndState(id, MemberState.ACTIVE).orElseThrow(() -> new CustomException(ResponseCode.MEMBER_NOT_FOUND));
        member.setState(MemberState.INACTIVE);
        memberRepository.save(member);
        return ResponseEntity.ok(CustomResponseCode.MEMBER_DELETE_SUCCESS);
    }

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> createMember(MemberCreateRequestDto memberCreateRequestDto) {
        checkConflictMember(memberCreateRequestDto.getNickname(), memberCreateRequestDto.getEmail());
        memberCreateRequestDto.setPassword(encodePassword(memberCreateRequestDto.getPassword()));
        memberRepository.save(MemberCreateRequestDto.createMember(memberCreateRequestDto));
        return ResponseEntity.ok(CustomResponseCode.MEMBER_CREATE_SUCCESS);

    }

    @Override
    @Transactional
    public ResponseEntity<CustomResponseCode> updateMember(Long id, MemberCreateRequestDto memberCreateRequestDto) {
        Member oldMember = memberRepository.findByIdAndState(id, MemberState.ACTIVE).orElseThrow(() -> new CustomException(ResponseCode.MEMBER_NOT_FOUND));
        checkConflictMember(memberCreateRequestDto.getNickname(), memberCreateRequestDto.getEmail());
        memberRepository.save(MemberCreateRequestDto.updateMember(oldMember, memberCreateRequestDto));
        return ResponseEntity.ok(CustomResponseCode.MEMBER_UPDATE_SUCCESS);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<GetMemberResponseDto> getMember(Long id) {
        Member member = memberRepository.findByIdAndState(id, MemberState.ACTIVE).orElseThrow(() -> new CustomException(ResponseCode.MEMBER_NOT_FOUND));
        GetMemberResponseDto response = GetMemberResponseDto.getMemberInfo(member);
        return ResponseEntity.ok(response);
    }

    // 전체 리스트
    public GetMemberListResponseDto searchUsers(SearchType searchType, String keyword, int page) {
        Pageable pageable = PageRequest.of(page, 10);  // 한 페이지당 10개의 항목을 가져옵니다.
        Page<Member> resultPage;

        // 검색 타입에 따라 유저 검색
        if (searchType == SearchType.NICKNAME) {
            resultPage = memberRepository.findByNicknameContainingAndState(keyword, MemberState.ACTIVE, pageable);
        } else if (searchType == SearchType.EMAIL) {
            resultPage = memberRepository.findByEmailContainingAndState(keyword, MemberState.ACTIVE, pageable);
        } else {
            resultPage = Page.empty(pageable);
        }

        // 검색 결과를 MemberListInfo 리스트로 변환
        List<GetMemberListResponseDto.MemberListInfo> memberList = resultPage.getContent().stream()
                .map(member -> {
                    GetMemberListResponseDto.MemberListInfo memberInfo = new GetMemberListResponseDto.MemberListInfo();
                    memberInfo.setId(member.getId());
                    memberInfo.setName(member.getName());
                    memberInfo.setEmail(member.getEmail());
                    memberInfo.setCreatedAt(member.getCreatedAt());
                    return memberInfo;
                })
                .collect(Collectors.toList());

        GetMemberListResponseDto responseDto = new GetMemberListResponseDto();
        responseDto.setMemberList(memberList);  // 검색된 사용자 리스트
        responseDto.setTotalPages(resultPage.getTotalPages()); // 총 페이지 수
        responseDto.setHasNext(resultPage.hasNext());  // 다음 페이지 존재 여부

        return responseDto;
    }

    // 닉네임, 이메일 중복 체크
    public void checkConflictMember(String nickname, String email) {
        if (memberRepository.existsMemberByNickname(nickname)) {
            throw new CustomException(ResponseCode.NICKNAME_DUPLICATE);
        }
        if (memberRepository.existsMemberByEmail(email)) {
            throw new CustomException(ResponseCode.EMAIL_DUPLICATE);
        }
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
