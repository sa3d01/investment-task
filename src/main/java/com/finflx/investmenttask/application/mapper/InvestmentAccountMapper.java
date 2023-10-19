package com.finflx.investmenttask.application.mapper;

import com.finflx.investmenttask.application.dto.InvestmentAccountRequest;
import com.finflx.investmenttask.application.dto.InvestmentAccountResponse;
import com.finflx.investmenttask.domain.entity.InvestmentAccount;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvestmentAccountMapper {

    InvestmentAccountMapper INSTANCE = Mappers.getMapper(InvestmentAccountMapper.class);

    InvestmentAccount map(InvestmentAccountRequest request);

    InvestmentAccountResponse map(InvestmentAccount entity);

    List<InvestmentAccountResponse> mapList(List<InvestmentAccount> entitiesList);

    void updateEntity(InvestmentAccountRequest request, @MappingTarget InvestmentAccount entity);
}
