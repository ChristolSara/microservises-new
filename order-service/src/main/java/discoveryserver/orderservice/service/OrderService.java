package discoveryserver.orderservice.service;

import discoveryserver.orderservice.dto.InventoryResponse;
import discoveryserver.orderservice.dto.OrderLineItemsDto;
import discoveryserver.orderservice.dto.OrderRequest;
import discoveryserver.orderservice.model.Order;
import discoveryserver.orderservice.model.OrderLineItems;
import discoveryserver.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {
    private final OrderRepository orderRepository;
    private  final WebClient.Builder webClientBuilder;

    public void placeOrder(OrderRequest orderRequest) throws IllegalAccessException {

        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList().stream()
                .map(this::mapToDto).toList();

        order.setOrderLineItemsList(orderLineItems);


        List<String> skuCodes = order.getOrderLineItemsList().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();


        //call inventery service and place order if product is in stock

      InventoryResponse[] inventoryResponseArray=  webClientBuilder.build().get().uri("http://inventory-service/api/inventory",
                      uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build())
              .retrieve()
              .bodyToMono(InventoryResponse[].class)
                        .block();
      //if the product is availble then make a order

        //verifier si tt les produit sont dispo en stock
        boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
                .allMatch(InventoryResponse::isInStock);

        //si un de ces produit n'est pas dispo en stock la reponse est false
        if(allProductsInStock){
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
