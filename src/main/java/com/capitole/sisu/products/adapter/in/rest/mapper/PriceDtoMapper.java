package com.capitole.sisu.products.adapter.in.rest.mapper;

import com.capitole.sisu.products.adapter.in.rest.dto.PriceDTO;
import com.capitole.sisu.products.domain.price.Price;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PriceDtoMapper {

    Price fromPriceDTOToPrice(PriceDTO priceDTO);
}
