package com.gnomeshift.tisk.stats;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(
        componentModel = "spring",
        uses = {DailyStatisticsMapper.class},
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface PeriodStatisticsMapper {
    @Mapping(target = "startDate", source = "startDate")
    @Mapping(target = "endDate", source = "endDate")
    @Mapping(target = "totalCreated", source = "totalCreated")
    @Mapping(target = "totalClosed", source = "totalClosed")
    @Mapping(target = "dailyStatistics", source = "dailyCounts")
    PeriodStatisticsDTO toDto(PeriodStatistics data);
}
