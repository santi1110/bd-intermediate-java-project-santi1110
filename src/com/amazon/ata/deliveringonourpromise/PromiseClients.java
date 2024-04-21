package com.amazon.ata.deliveringonourpromise;
import com.amazon.ata.deliveringonourpromise.types.Promise;

public interface PromiseClients {
    /**
     *
     * @param customerOrderItemId type String
     * @return PromiseOrderItemId with customerOrderItemId
     */
    Promise getDeliveryPromiseByOrderItemId(String customerOrderItemId);
}
