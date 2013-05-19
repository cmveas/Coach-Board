package com.sportcoachhelper.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Template implements Serializable {

	public ArrayList<TemplateItem> templateItems = new ArrayList<TemplateItem>();

	public void add(TemplateItem item) {
		templateItems.add(item);

	}

}
