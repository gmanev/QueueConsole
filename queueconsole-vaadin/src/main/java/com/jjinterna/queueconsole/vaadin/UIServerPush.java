package com.jjinterna.queueconsole.vaadin;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.jjinterna.pbxevents.model.CallConnect;
import com.jjinterna.pbxevents.model.CallEnterQueue;
import com.jjinterna.pbxevents.model.PBXCallQueueEvent;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("valo")
@Push
public class UIServerPush extends UI implements Calls.BroadcastListener {

	Map<String, CallWindow> windows = new ConcurrentHashMap<String, CallWindow>();

	Image image;
	int step;

	String imageURL, callerURL, title;
	
	public UIServerPush(String title, String imageURL, String callerURL) {
		this.imageURL = imageURL;
		this.callerURL = callerURL;
		this.title = title;
	}

	@Override
	protected void init(VaadinRequest request) {
		
		request.getWrappedSession().setMaxInactiveInterval(-1);
		
		getPage().setTitle(title);
		
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		setContent(layout);

		if (imageURL != null) {
			image = new Image();
			image.setSource(new ExternalResource(imageURL));
			image.setVisible(true);
			layout.addComponent(image);
			layout.setComponentAlignment(image, Alignment.MIDDLE_CENTER);
		}

		Calls.register(this);
	}

	@Override
	public void detach() {
		Calls.unregister(this);
	}

	private void updateCall(PBXCallQueueEvent callEvent) {

		if (callEvent instanceof CallConnect || callEvent instanceof CallEnterQueue) {
			CallWindow window = windows.get(callEvent.getCallId());
			if (window == null) {
				window = new CallWindow();
				window.setCallerURL(callerURL);
				window.setShowTimers(false);
				windows.put(callEvent.getCallId(), window);
				UI.getCurrent().addWindow(window);
			}
			window.update(callEvent);
		}
		else {
			CallWindow window = windows.get(callEvent.getCallId());
			if (window != null) {
				window.close();
				windows.remove(callEvent.getCallId());
			}
		}

		int i = 0;
		for (Iterator<CallWindow> iter = windows.values().iterator(); iter.hasNext();) {
			CallWindow window = iter.next();
			int col = i % 4;
			int row = i / 4;
			window.setPositionX(10 + col * 320);
			window.setPositionY(10 + row * 320);
			i++;					
		}
	}

	@Override
	public void receiveBroadcast(final PBXCallQueueEvent callEvent) {
		access(new Runnable() {

			@Override
			public void run() {
				updateCall(callEvent);
			}
			
		});		
	}


}
