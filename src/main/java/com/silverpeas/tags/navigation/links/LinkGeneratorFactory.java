package com.silverpeas.tags.navigation.links;

import com.silverpeas.tags.navigation.config.Configurateur;

public class LinkGeneratorFactory {
	private static LinkGeneratorFactory instance;
	private static Class<?> linkGeneratorImplClass;
	
	private LinkGeneratorFactory() {
        super();
        String linkGeneratorImplClassName = Configurateur.getConfigValue("linkGeneratorImplementation");
        try {
			linkGeneratorImplClass = Class.forName(linkGeneratorImplClassName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
    }
	
	public static LinkGeneratorFactory getInstance() {
        if (instance == null) {
            instance = new LinkGeneratorFactory();
        }
        return instance;
    }
	
	public LinkGenerator newLinkGenerator() throws Exception {		
		return (LinkGenerator) linkGeneratorImplClass.newInstance();			
    }
}
