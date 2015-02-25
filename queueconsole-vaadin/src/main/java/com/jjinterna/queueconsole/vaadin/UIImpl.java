package com.jjinterna.queueconsole.vaadin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import com.jjinterna.pbxevents.model.PBXCallQueueEvent;
import com.vaadin.annotations.Theme;
import com.vaadin.event.UIEvents.PollEvent;
import com.vaadin.event.UIEvents.PollListener;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@Theme("valo")
public class UIImpl extends UI {

	Map<String, CallWindow> windows = new ConcurrentHashMap<String, CallWindow>();

	Label timeLabel;
	Image image;
	int step;

	String imageURL, callerURL, title;
	Calls calls = Webservice.calls;
	
	public UIImpl(String title, String imageURL, String callerURL) {
		this.imageURL = imageURL;
		this.callerURL = callerURL;
		this.title = title;
	}

	@Override
	protected void init(VaadinRequest request) {
		getPage().setTitle(title);
		
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		setContent(layout);

		timeLabel = new Label(getTime());
		timeLabel.setStyleName(ValoTheme.LABEL_H1);
		timeLabel.setSizeUndefined();
		layout.addComponent(timeLabel);
		layout.setComponentAlignment(timeLabel, Alignment.MIDDLE_CENTER);

		if (imageURL != null) {
			image = new Image();
			image.setSource(new ExternalResource(imageURL));
			image.setVisible(false);
			layout.addComponent(image);
			layout.setComponentAlignment(image, Alignment.MIDDLE_CENTER);
		}

		addPollListener(new PollListener() {

			@Override
			public void poll(PollEvent event) {
				step++;

				if ((image != null) && (step % 10) == 0) {
					timeLabel.setVisible(!timeLabel.isVisible());
					image.setVisible(!timeLabel.isVisible());
				}

				timeLabel.setValue(getTime());

				updateCalls();
			}
		});

		setPollInterval(1000);
		
	}

	public String getTime() {
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		Date date = new Date();
		String d = dateFormat.format(date);
		return d;
	}

	private void updateCalls() {

		// remove closed calls
		Iterator<Entry<String, CallWindow>> entries = windows.entrySet().iterator();
		while (entries.hasNext()) {
			Entry<String, CallWindow> thisEntry = entries.next();
			if (!calls.getCalls().containsKey(thisEntry.getKey())) {
				thisEntry.getValue().close();
				entries.remove();
			}
		}
		
		// add new calls
		for (PBXCallQueueEvent call : calls.getCalls().values()) {
			CallWindow window = windows.get(call.getCallId());
			if (window == null) {
				window = new CallWindow();
				window.setCallerURL(callerURL);
				windows.put(call.getCallId(), window);
				UI.getCurrent().addWindow(window);
			}
			window.update(call);
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

}
