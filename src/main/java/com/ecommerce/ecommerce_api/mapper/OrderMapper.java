package com.ecommerce.ecommerce_api.mapper;

import com.ecommerce.ecommerce_api.dto.OrderDTO;
import com.ecommerce.ecommerce_api.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {

    @Mapping(source = "orderItems", target = "items")
    OrderDTO toDTO(Order order);

    @Mapping(source = "items", target = "orderItems")
    Order toEntity(OrderDTO orderDTO);

    List<OrderDTO> toDTOList(List<Order> orders);
}