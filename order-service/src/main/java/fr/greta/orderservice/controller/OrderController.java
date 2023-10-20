package fr.greta.orderservice.controller;

import fr.greta.orderservice.dto.OrderRequest;
import fr.greta.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String PlaceOrder(@RequestBody OrderRequest orderRequest) throws IllegalAccessException {
        orderService.placeOrder(orderRequest);
        return "Order Placed Successfully";
    }

}
