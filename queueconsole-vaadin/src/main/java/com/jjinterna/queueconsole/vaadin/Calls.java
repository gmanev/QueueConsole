package com.jjinterna.queueconsole.vaadin;

import java.util.Collections;
import java.util.Map;
import java.util.Observable;
import java.util.concurrent.ConcurrentHashMap;

import com.jjinterna.pbxevents.model.CallConnect;
import com.jjinterna.pbxevents.model.CallEnterQueue;
import com.jjinterna.pbxevents.model.PBXCallQueueEvent;

public class Calls extends Observable {

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
		
		setChanged();
		notifyObservers(callEvent);
	}

	public Map<String, PBXCallQueueEvent> getCalls() {
		return Collections.unmodifiableMap(calls);
	}
}
