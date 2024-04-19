package com.amazon.ata.deliveringonourpromise;

import com.amazon.ata.deliveringonourpromise.types.Promise;

public interface PromiseClients {

    public Promise getDeliveryPromiseByOrderItemId(String customerOrderItemId);

}