package org.sparvnastet.rnf;

import android.view.MotionEvent;

/**
 * The input broker is a thread safe, aggregating proxy of input events that is
 * pushed from the view to some other, non UI, thread.
 */
public interface IMotionEventBroker {

    /**
     * Add a new event to the brokers queue.
     * 
     * @param motionEvent
     */
    void put(MotionEvent motionEvent);

    /**
     * Get all enqueued events and remove them from the broker. The events are
     * ordered in the same way as they were pushed into the broker, the oldest
     * event first and the latest event last.
     * 
     * @return
     */
    MotionEvent[] takeBundle();
}