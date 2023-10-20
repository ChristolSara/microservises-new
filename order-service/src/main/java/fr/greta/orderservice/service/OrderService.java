package fr.greta.orderservice.service;

import fr.greta.orderservice.dto.OrderLineItemsDto;
import fr.greta.orderservice.dto.OrderRequest;
import fr.greta.orderservice.model.Order;
import fr.greta.orderservice.model.OrderLineItems;
import fr.greta.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private  final WebClient webClient;

    public void placeOrder(OrderRequest orderRequest) throws IllegalAccessException {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList().stream()
                .map(this::mapToDto).toList();

        order.setOrderLineItemsList(orderLineItems);


        //call inventery service and place order if product is in stock

      Boolean result=  webClient.get().uri("http://localhost:8082/api/inventory").retrieve().bodyToMono(Boolean.class)
                        .block();
      //if the product is availble then make a order

        if(result){
            orderRepository.save(order);
        }else{
            throw new IllegalAccessException("Product is not in the stocke ");
        }


    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }


}
