package com.jjinterna.queueconsole.vaadin;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import com.jjinterna.pbxevents.model.CallConnect;
import com.jjinterna.pbxevents.model.CallEnterQueue;
import com.jjinterna.pbxevents.model.PBXCallQueueEvent;

public class Calls {

//	static ExecutorService executorService = Executors
//			.newSingleThreadExecutor();

	private final Map<String, PBXCallQueueEvent> calls =
			new ConcurrentHashMap<>();

	public void update(final PBXCallQueueEvent callEvent) {
		if (callEvent instanceof CallConnect || callEvent instanceof CallEnterQueue) {
			calls.put(callEvent.getCallId(), callEvent);
		} else {
			calls.remove(callEvent.getCallId());
		}
		broadcast(callEvent);
	}

	public Map<String, PBXCallQueueEvent> getCalls() {
		return Collections.unmodifiableMap(calls);
	}

    private static final List<BroadcastListener> listeners = new CopyOnWriteArrayList<BroadcastListener>();

    public static void register(BroadcastListener listener) {
        listeners.add(listener);
    }

    public static void unregister(BroadcastListener listener) {
        listeners.remove(listener);
    }

    private static void broadcast(final PBXCallQueueEvent callEvent) {
        for (BroadcastListener listener : listeners) {
            listener.receiveBroadcast(callEvent);
        }
    }

    public interface BroadcastListener {
        public void receiveBroadcast(PBXCallQueueEvent callEvent);
    }
}
