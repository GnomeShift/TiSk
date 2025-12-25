package com.gnomeshift.tisk.stats;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface DailyStatisticsMapper {
    @Mapping(target = "date", source = "date")
    @Mapping(target = "created", source = "created")
    @Mapping(target = "closed", source = "closed")
    DailyStatisticsDTO toDto(DailyCount count);

    List<DailyStatisticsDTO> toDtoList(List<DailyCount> counts);
}
