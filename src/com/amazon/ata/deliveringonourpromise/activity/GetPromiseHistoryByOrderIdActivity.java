package com.amazon.ata.deliveringonourpromise.activity;

import com.amazon.ata.deliveringonourpromise.comparators.PromiseAsinComparator;
import com.amazon.ata.deliveringonourpromise.dao.ReadOnlyDao;
import com.amazon.ata.deliveringonourpromise.types.Order;
import com.amazon.ata.deliveringonourpromise.types.OrderItem;
import com.amazon.ata.deliveringonourpromise.types.Promise;
import com.amazon.ata.deliveringonourpromise.types.PromiseHistory;

import java.util.List;

/**
 * Activity class, handling the GetPromiseHistoryByOrderId API.
 */
public class GetPromiseHistoryByOrderIdActivity {
    private ReadOnlyDao<String, Order> orderDao;
    private ReadOnlyDao<String, List<Promise>> promiseDao;

    /**
     * Instantiates an activity for handling the API, accepting the relevant DAOs to
     * perform its work.
     *
     * @param orderDao   data access object fo retrieving Orders by order ID
     * @param promiseDao data access object for retrieving Promises by order item ID
     */
    public GetPromiseHistoryByOrderIdActivity(ReadOnlyDao<String, Order> orderDao,
                                              ReadOnlyDao<String, List<Promise>> promiseDao) {
        this.orderDao = orderDao;
        this.promiseDao = promiseDao;
    }

    /**
     * Returns the PromiseHistory for the given order ID, if the order exists. If the order does
     * not exist a PromiseHistory with a null order and no promises will be returned.
     *
     * @param orderId The order ID to fetch PromiseHistory for
     * @return PromiseHistory containing the order and promise history for that order
     */
    public PromiseHistory getPromiseHistoryByOrderId(String orderId) {
        if (orderId == null) {
            throw new IllegalArgumentException("Order ID cannot be null");
        }

        Order order = orderDao.get(orderId);
        if (order == null) {
            // Handle the case when the order is not found
            return new PromiseHistory(null);
        }

        List<OrderItem> customerOrderItems = order.getCustomerOrderItemList();
        PromiseHistory history = new PromiseHistory(order);

        for (OrderItem customerOrderItem : customerOrderItems) {
            List<Promise> promises = promiseDao.get(customerOrderItem.getCustomerOrderItemId());
            System.out.println("Number of promises fetched for order item ID " +
                    customerOrderItem.getCustomerOrderItemId() + ": " + promises.size());

            // Sort promises by ASIN using your custom comparator
            promises.sort(new PromiseAsinComparator());

            for (Promise promise : promises) {
                promise.setConfidence(customerOrderItem.isConfidenceTracked(), customerOrderItem.getConfidence());
                history.addPromise(promise);
            }
        }
        return history;
    }
}
