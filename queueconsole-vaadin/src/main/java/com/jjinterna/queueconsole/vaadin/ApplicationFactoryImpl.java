package com.jjinterna.queueconsole.vaadin;

import com.jjinterna.vaadin.vaadinbridge.ApplicationFactory;
import com.vaadin.ui.UI;

public class ApplicationFactoryImpl implements ApplicationFactory {

	String imageURL, callerURL, title;
	Boolean serverPush;
	
	public ApplicationFactoryImpl(String title, String imageURL, String callerURL, Boolean serverPush) {
		if (!"".equals(imageURL)) {
			this.imageURL = imageURL;
		}
		if (!"".equals(callerURL)) {
			this.callerURL = callerURL;
		}
		this.title = title;
		this.serverPush = serverPush;
	}
	@Override
	public UI getInstance() {
		return new UIImpl(title, imageURL, callerURL, serverPush);
	}

	@Override
	public Class<? extends UI> getUIClass() {
		return UIImpl.class;
	}

}
