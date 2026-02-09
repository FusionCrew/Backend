package com.fusioncrew.aikiosk.domain.menu.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "option_groups")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptionGroup {

    @Id
    @Column(name = "option_group_id")
    private String optionGroupId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean isRequired;

    @Column(nullable = false)
    private boolean isMultipleSelectionAllowed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_item_id")
    private MenuItem menuItem;

    @OneToMany(mappedBy = "optionGroup", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OptionItem> optionItems = new ArrayList<>();

    @Builder
    public OptionGroup(String name, boolean isRequired, boolean isMultipleSelectionAllowed) {
        this.optionGroupId = "optgrp_" + UUID.randomUUID().toString();
        this.name = name;
        this.isRequired = isRequired;
        this.isMultipleSelectionAllowed = isMultipleSelectionAllowed;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public void addOptionItem(OptionItem item) {
        this.optionItems.add(item);
        item.setOptionGroup(this);
    }
}
