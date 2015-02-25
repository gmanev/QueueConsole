package com.jjinterna.queueconsole.vaadin;

import com.jjinterna.vaadin.vaadinbridge.ApplicationFactory;
import com.vaadin.ui.UI;

public class ApplicationFactoryServerPush implements ApplicationFactory {

	String imageURL, callerURL, title;
	
	public ApplicationFactoryServerPush(String title, String imageURL, String callerURL) {
		if (!"".equals(imageURL)) {
			this.imageURL = imageURL;
		}
		if (!"".equals(callerURL)) {
			this.callerURL = callerURL;
		}
		this.title = title;
	}

	@Override
	public UI getInstance() {
		return new UIServerPush(title, imageURL, callerURL);
	}

	@Override
	public Class<? extends UI> getUIClass() {
		return UIServerPush.class;
	}


}
