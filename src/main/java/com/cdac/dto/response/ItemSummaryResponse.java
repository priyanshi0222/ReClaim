package com.cdac.dto.response;

import java.time.LocalDate;

import com.cdac.enums.ItemStatus;
import com.cdac.enums.ItemType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemSummaryResponse {
	private Long id;

	private String title;

	private String category;

	private String color;

	private String locationDescription;

	private LocalDate itemDate;

	private ItemType itemType;

	private ItemStatus status;

	private String thumbnailImageUrl;
}
