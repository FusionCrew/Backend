package com.fusioncrew.aikiosk.domain.kiosk.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KioskMenuListResponse {

    private boolean success;
    private MenuListData data;
    private String timestamp;
    private String requestId;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MenuListData {
        private List<KioskMenuItemDto> items;
        private PageInfo page;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KioskMenuItemDto {
        private String menuItemId;
        private String name;
        private int price;
        private String thumbnailUrl;
        private boolean isAvailable;
        private String categoryId;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageInfo {
        private int size;
        private String nextCursor;
    }
}
