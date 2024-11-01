package com.conseller.conseller.core.barter.implement;

import com.conseller.conseller.core.barter.api.dto.request.BarterModifyRequest;
import com.conseller.conseller.core.category.domain.SubCategory;
import com.conseller.conseller.core.gifticon.implement.GifticonReader;
import com.conseller.conseller.global.exception.CustomException;
import com.conseller.conseller.global.exception.CustomExceptionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BarterProcessor {

    private final GifticonReader gifticonReader;
    private final BarterModifier barterModifier;

    public Long getMaxSelectedCategory(List<Long> selectedCategoryIds) {
        return getMostSelectedCategory(getSelectedCategoryCount(selectedCategoryIds));
    }

    private Map<Long, Long> getSelectedCategoryCount(List<Long> selectedCategoryIds) {
        return selectedCategoryIds.stream()
                .map(id -> gifticonReader.read(id).getSubCategoryIdx())
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    private Long getMostSelectedCategory(Map<Long, Long> selectedCategoryCount) {
        return selectedCategoryCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new CustomException(CustomExceptionStatus.SUB_CATEGORY_INVALID));
    }
}
