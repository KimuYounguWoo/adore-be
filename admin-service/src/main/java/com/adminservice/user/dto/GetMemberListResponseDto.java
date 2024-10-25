package com.adminservice.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class GetMemberListResponseDto {
    private List<MemberListInfo> memberList;
    private int totalPages;
    private boolean hasNext;

    @Getter
    @Setter
    public static class MemberListInfo {
        private Long id;
        private String name;
        private String email;
        private LocalDateTime createdAt;
    }
}
