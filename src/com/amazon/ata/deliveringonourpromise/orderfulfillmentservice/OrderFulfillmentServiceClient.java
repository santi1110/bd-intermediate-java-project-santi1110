package com.amazon.ata.deliveringonourpromise.orderfulfillmentservice;

import com.amazon.ata.deliveringonourpromise.PromiseClients;
import com.amazon.ata.deliveringonourpromise.types.Promise;
import com.amazon.ata.orderfulfillmentservice.OrderPromise;
import com.amazon.ata.orderfulfillmentservice.OrderFulfillmentService;

import java.util.concurrent.CompletableFuture;

public class OrderFulfillmentServiceClient implements PromiseClients {
    private OrderFulfillmentService ofService;


    /**
     * Create a new client that interacts with the order fulfillment service.
     *
     * @param ofService The OrderFulfillmentService instance to use.
     */
    public OrderFulfillmentServiceClient(OrderFulfillmentService ofService) {
        this.ofService = ofService;
    }

    /**
     * Fetches fulfillment details for the given order item ID.
     *
     * @param customerOrderItemId The order item ID to retrieve fulfillment details for.
     * @return Fulfillment details for the specified order item.
     */
    public Promise getDeliveryPromiseByOrderItemId(String customerOrderItemId) {
        // Retrieve the order using the customerOrderItemId
       OrderPromise orderPromise = ofService.getOrderPromise(customerOrderItemId);

        if (null == orderPromise) {
            return null;
        }

        // Process the order and create an OrderFulfillment object
        return Promise.builder()
                .withCustomerOrderItemId(orderPromise.getCustomerOrderItemId())
                .withPromiseLatestArrivalDate(orderPromise.getPromiseLatestArrivalDate())
                .withPromiseEffectiveDate(orderPromise.getPromiseEffectiveDate())
                .withIsActive(orderPromise.isActive())
                .withPromiseLatestShipDate(orderPromise.getPromiseLatestShipDate())
                .withPromiseProvidedBy(orderPromise.getPromiseProvidedBy())
                .build();
    }


}
