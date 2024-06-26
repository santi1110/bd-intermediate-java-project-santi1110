package com.amazon.ata.deliveringonourpromise.dao;

import com.amazon.ata.deliveringonourpromise.PromiseClients;
import com.amazon.ata.deliveringonourpromise.ordermanipulationauthority.OrderManipulationAuthorityClient;
import com.amazon.ata.deliveringonourpromise.types.Promise;
import com.amazon.ata.ordermanipulationauthority.OrderResult;
import com.amazon.ata.ordermanipulationauthority.OrderResultItem;
import com.amazon.ata.ordermanipulationauthority.OrderShipment;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * DAO implementation for Promises.
 */
public class PromiseDao implements ReadOnlyDao<String, List<Promise>> {
    private OrderManipulationAuthorityClient omaClient;
    private List<PromiseClients> clients;

    /**
     *
     * @param dpsClient deliveryconstructor
     * @param omaClient omaconstructor
     */





    /**
     * PromiseDao constructor, accepting service clients for DPS and OMA.
     *
     * @param dpsClient DeliveryPromiseServiceClient for DAO to access DPS
     * @param omaClient OrderManipulationAuthorityClient for DAO to access OMA
     * @param ofsClient OrderFulfillmentServiceClient for DAO to access OFS
     */


    /**
     *
     * @param clients List of PromiseDAO
     * @param omaClient OrderManipulationAuthorityClient for DAO to access OMA.
     */
    public PromiseDao(List<PromiseClients> clients, OrderManipulationAuthorityClient omaClient) {
        this.clients = clients;
        this.omaClient = omaClient;
    }

    /**
     * Returns a list of all Promises associated with the given order item ID.
     *
     * @param customerOrderItemId the order item ID to fetch promise for
     * @return a List of promises for the given order item ID
     */
    @Override
    public List<Promise> get(String customerOrderItemId) {
        ZonedDateTime itemDeliveryDate = getDeliveryDateForOrderItem(customerOrderItemId);
        List<Promise> promises = new ArrayList<>();

        if (clients != null) {
            for (PromiseClients client : clients) {
                Promise dpsPromise = client.getDeliveryPromiseByOrderItemId(customerOrderItemId);
                if (dpsPromise != null) {
                    dpsPromise.setDeliveryDate(itemDeliveryDate);
                    promises.add(dpsPromise);
                } else {
                    System.out.println("No delivery promise found for order item ID: " + customerOrderItemId);
                }
            }
        }
        System.out.println("Number of promises fetched from PromiseDao: " + promises.size());
        return promises;
    }



    /*
     * Fetches the delivery date of the shipment containing the order item specified by the given order item ID,
     * if there is one.
     *
     * If the order item ID doesn't correspond to a valid order item, or if the shipment hasn't been delivered
     * yet, retur

     *
     */


    private ZonedDateTime getDeliveryDateForOrderItem(String customerOrderItemId) {
        OrderResultItem orderResultItem = omaClient.getCustomerOrderItemByOrderItemId(customerOrderItemId);

        if (null == orderResultItem) {
            return null;
        }

        OrderResult orderResult = omaClient.getCustomerOrderByOrderId(orderResultItem.getOrderId());

        for (OrderShipment shipment : orderResult.getOrderShipmentList()) {
            for (OrderShipment.ShipmentItem shipmentItem : shipment.getCustomerShipmentItems()) {
                if (shipmentItem.getCustomerOrderItemId().equals(customerOrderItemId)) {
                    return shipment.getDeliveryDate();
                }
            }
        }

        // didn't find a delivery date!
        return null;
    }
}
