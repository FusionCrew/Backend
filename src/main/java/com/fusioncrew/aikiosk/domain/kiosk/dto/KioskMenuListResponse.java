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
        private String imageUrl; // Added for consistency
        private boolean isAvailable;
        private String categoryId;
        private String description; // Added description
        private List<KioskOptionGroupDto> optionGroups;
        private List<String> ingredients;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KioskOptionGroupDto {
        private String optionGroupId;
        private String name;
        private boolean isRequired;
        private boolean isMultipleSelectionAllowed;
        private List<KioskOptionItemDto> optionItems;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KioskOptionItemDto {
        private String optionItemId;
        private String name;
        private int extraPrice;
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
