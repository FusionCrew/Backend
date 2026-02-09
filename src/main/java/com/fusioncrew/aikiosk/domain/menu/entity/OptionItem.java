package com.fusioncrew.aikiosk.domain.menu.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Table(name = "option_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptionItem {

    @Id
    @Column(name = "option_item_id")
    private String optionItemId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int extraPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_group_id")
    private OptionGroup optionGroup;

    @Builder
    public OptionItem(String name, int extraPrice) {
        this.optionItemId = "optitem_" + UUID.randomUUID().toString();
        this.name = name;
        this.extraPrice = extraPrice;
    }

    public void setOptionGroup(OptionGroup optionGroup) {
        this.optionGroup = optionGroup;
    }
}
