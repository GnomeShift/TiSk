package com.gnomeshift.tisk.stats;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS
)
public interface AssigneeStatisticsMapper {
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "totalAssigned", source = "totalCount")
    @Mapping(target = "openTickets", source = "openCount")
    @Mapping(target = "inProgressTickets", source = "inProgressCount")
    @Mapping(target = "closedTickets", source = "closedCount")
    @Mapping(target = "averageResolutionTimeHours", source = "averageResolutionTime", qualifiedByName = "secondsToHours")
    AssigneeStatisticsDTO toDto(AssigneeCount count);

    List<AssigneeStatisticsDTO> toDtoList(List<AssigneeCount> counts);

    @Named("secondsToHours")
    default Double convertSecondsToHours(Double seconds) {
        if (seconds == null) {
            return null;
        }
        return Math.round(seconds / 3600.0 * 100.0) / 100.0;
    }
}
