package com.amazon.ata.deliveringonourpromise.comparators;

import com.amazon.ata.deliveringonourpromise.types.Promise;

import java.io.Serializable;
import java.util.Comparator;

public class PromiseAsinComparator implements Comparator<Promise>, Serializable {

    @Override
    public int compare(Promise promise1, Promise promise2) {
        return promise1.getAsin().compareTo(promise2.getAsin());
    }
}
