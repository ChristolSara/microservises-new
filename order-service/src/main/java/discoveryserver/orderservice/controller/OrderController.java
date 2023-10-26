package discoveryserver.orderservice.controller;

import discoveryserver.orderservice.service.OrderService;
import discoveryserver.orderservice.dto.OrderRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
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
    @CircuitBreaker(name="inventory",fallbackMethod = "fallbackMethod")
    public String PlaceOrder(@RequestBody OrderRequest orderRequest) throws IllegalAccessException {
        orderService.placeOrder(orderRequest);
        return "Order Placed Successfully";
    }



    public String fallbackMethod(OrderRequest orderRequest,RuntimeException runtimeException ){

        return "Oops! Something went wrong,please order after some time !";
    }

}
