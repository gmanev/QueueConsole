package com.jjinterna.queueconsole.vaadin;

import com.jjinterna.vaadin.vaadinbridge.ApplicationFactory;
import com.vaadin.ui.UI;

public class ApplicationFactoryImpl implements ApplicationFactory {

	String imageURL, callerURL, title;
	
	public ApplicationFactoryImpl(String title, String imageURL, String callerURL) {
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
		return new UIImpl(title, imageURL, callerURL);
	}

	@Override
	public Class<? extends UI> getUIClass() {
		return UIImpl.class;
	}

}
