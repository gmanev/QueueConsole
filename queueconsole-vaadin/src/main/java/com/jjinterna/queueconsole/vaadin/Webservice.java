package com.jjinterna.queueconsole.vaadin;

import javax.jws.WebMethod;
import javax.jws.WebService;

import com.jjinterna.pbxevents.model.PBXCallQueueEvent;
import com.jjinterna.pbxevents.model.PBXEvent;

@WebService(targetNamespace = "http://soap.action.pbxevents.jjinterna.com/")
public class Webservice {

	static final Calls calls = new Calls();

	@WebMethod
	public void updateEvent(PBXEvent event) {
		if (event instanceof PBXCallQueueEvent) {
			PBXCallQueueEvent callEvent = (PBXCallQueueEvent) event;
			calls.update(callEvent);
		}
	}

}
