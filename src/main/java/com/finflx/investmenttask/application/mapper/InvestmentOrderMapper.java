package com.finflx.investmenttask.application.mapper;

import com.finflx.investmenttask.application.dto.InvestmentOrderRequest;
import com.finflx.investmenttask.application.dto.InvestmentOrderResponse;
import com.finflx.investmenttask.domain.entity.InvestmentOrder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvestmentOrderMapper {
    InvestmentOrderMapper INSTANCE = Mappers.getMapper(InvestmentOrderMapper.class);

    InvestmentOrder map(InvestmentOrderRequest request);

    @Mapping(target = "account", source = "account")
    InvestmentOrderResponse map(InvestmentOrder entity);

    @Mapping(target = "account", source = "account")
    List<InvestmentOrderResponse> mapList(List<InvestmentOrder> entitiesList);

    void updateEntity(InvestmentOrderRequest request, @MappingTarget InvestmentOrder entity);
}
