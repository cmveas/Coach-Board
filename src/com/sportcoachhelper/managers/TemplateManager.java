package com.sportcoachhelper.managers;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import android.content.res.AssetManager;

import com.sportcoachhelper.CoachApp;
import com.sportcoachhelper.model.Template;

public class TemplateManager {
	
	
	private static TemplateManager instance = new TemplateManager();
	private ArrayList<Template> templates = new ArrayList<Template>();
	
	private TemplateManager(){
		AssetManager assets = CoachApp.getInstance().getApplicationContext().getAssets();
		loadFormation(assets,"3-4-1-2_template");
		loadFormation(assets,"3-4-3_template");
		loadFormation(assets,"4-3-3_template");
		loadFormation(assets,"4-4-2_template");
		loadFormation(assets,"4-5-1_template");
		loadFormation(assets,"5-3-2_template");
		loadFormation(assets,"5-4-1_template");
		loadFormation(assets,"voley1_template");
		
	}

	private void loadFormation(AssetManager assets,String formation) {
		InputStream is;
		try {
			is = assets.open(formation);
		
		ObjectInputStream stream = new ObjectInputStream(is);
		Template template = (Template)stream.readObject();
		templates.add(template);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static TemplateManager getInstance(){
		return instance;
	}

	public  ArrayList<Template> getTemplates() {
		// TODO Auto-generated method stub
		return templates;
	}

	public Template getTemplateFromName(String name) {
		Template result = null;
		for (Template template : templates) {
			if(template.getName().equals(name)) {
				result = template;
			}
		}
		return result;
	}
	
}
