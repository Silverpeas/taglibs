package com.silverpeas.tags.navigation;

import java.util.concurrent.ConcurrentHashMap;

import com.silverpeas.tags.kmelia.KmeliaTagUtil;
import com.silverpeas.tags.navigation.config.Configurateur;
import com.stratelia.webactiv.util.publication.model.PublicationDetail;

public class PublicationCache {
	
	private static PublicationCache instance;	
	private KmeliaTagUtil themetracker = null;
	
	// Cache treeview
	private ConcurrentHashMap<String, PublicationDetail> publicationCache = new ConcurrentHashMap<String, PublicationDetail>();
	private long publicationCacheAge = System.currentTimeMillis();
	
	private PublicationCache(KmeliaTagUtil themetracker) {
        super();
        this.themetracker = themetracker;
    }	
	
	public static PublicationCache getInstance(KmeliaTagUtil themetracker) {
        if (instance == null) {
            instance = new PublicationCache(themetracker);
        }
        return instance;
    }
	
	public PublicationDetail getPublication(String pubId) throws Exception {
		manageCache();
		
		PublicationDetail pub = publicationCache.get(pubId);
		if (pub == null) {
			synchronized(this) {			
				pub = themetracker.getPublicationDetail(pubId);				
				publicationCache.putIfAbsent(pubId, pub);
			}
		}		
		
		return pub;	
	}
	
	private void manageCache() {		
		String cacheActivate = Configurateur.getConfigValue("publicationsCache");
		String refreshDelay = Configurateur.getConfigValue("publicationsCache.refreshDelay");
		if (refreshDelay == null) refreshDelay = "3600";
		if (cacheActivate == null) cacheActivate = "true";
		if ((System.currentTimeMillis()-publicationCacheAge >= Long.valueOf(refreshDelay)*1000) || cacheActivate.equals("false")) {
			publicationCache.clear();
			publicationCacheAge = System.currentTimeMillis();
		}
	}
}
