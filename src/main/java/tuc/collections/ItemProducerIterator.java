/*
 * Copyright 2010 Torkjel Hongve. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *    1. Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *
 *    2. Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the
 *       documentation and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDER ``AS IS'' AND ANY EXPRESS
 * OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package tuc.collections;

import java.util.Iterator;

/**
 * An {@link Iterator} over the items produced by an {@link ItemProducer}.
 *
 * @author torkjelh
 *
 * @param <U>
 */
public class ItemProducerIterator<U> implements Iterable<U>, Iterator<U> {

    private ItemProducer<U> producer;
    private U next;

    public ItemProducerIterator(ItemProducer<U> producer) {
        this.producer = producer;
    }

    @Override
    public Iterator<U> iterator() {
        return this;
    }

    @Override
    public boolean hasNext() {
        if (next == null)
            next = producer.produce();
        return next != null;
    }

    @Override
    public U next() {
        U i = next != null ? next : producer.produce();
        next = null;
        return i;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }

}
