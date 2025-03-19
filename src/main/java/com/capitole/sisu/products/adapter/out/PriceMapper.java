package com.capitole.sisu.products.adapter.out;

import com.capitole.sisu.products.adapter.out.model.CurrentPriceEntity;
import com.capitole.sisu.products.adapter.out.model.PriceEntity;
import com.capitole.sisu.products.domain.price.CurrentPrice;
import com.capitole.sisu.products.domain.price.Price;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PriceMapper {

    Price fromPriceEntityToPrice(PriceEntity price);

    PriceEntity fromPriceToPriceEntity(Price price);

    CurrentPrice fromCurrentPriceEntityToCurrentPrice(CurrentPriceEntity currentPriceEntity);
}
