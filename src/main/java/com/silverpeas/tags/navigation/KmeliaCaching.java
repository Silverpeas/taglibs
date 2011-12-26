package com.silverpeas.tags.navigation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import com.silverpeas.tags.kmelia.KmeliaTagUtil;
import com.silverpeas.tags.navigation.config.Configurateur;
import com.stratelia.webactiv.util.node.model.NodeDetail;

public class KmeliaCaching {
	
	private static KmeliaCaching instance;	
	private KmeliaTagUtil themetracker = null;
	
	// Cache treeview
	private HashMap<String, Collection<NodeDetail>> treeViewCache = new HashMap<String, Collection<NodeDetail>>();
	private long treeViewCacheAge = System.currentTimeMillis();
	
	private KmeliaCaching(KmeliaTagUtil themetracker) {
        super();
        this.themetracker = themetracker;
    }	
	
	public static KmeliaCaching getInstance(KmeliaTagUtil themetracker) {
        if (instance == null) {
            instance = new KmeliaCaching(themetracker);
        }
        return instance;
    }
	
	public NodeDetail getTopic(int topicId) throws RemoteException {		
		manageCache();
		Iterator<Collection<NodeDetail>> iTree = treeViewCache.values().iterator();
		while (iTree.hasNext()) {
			Collection<NodeDetail> trees = (Collection<NodeDetail>) iTree.next();
			Iterator<NodeDetail> iNodes = trees.iterator();
			while (iNodes.hasNext()) {
				NodeDetail node = (NodeDetail) iNodes.next();
				if (node.getId() == topicId) return node;
			}
		}
		
		return themetracker.getTopic(String.valueOf(topicId));
	}
	
	@SuppressWarnings("unchecked")
	public Collection<NodeDetail> getTreeView(String topicId) throws RemoteException {
		manageCache();		
		Collection<NodeDetail> treeView = treeViewCache.get(topicId);
		if (treeView == null) {			
			treeView = (Collection<NodeDetail>) themetracker.getTreeView(topicId);
			treeViewCache.put(topicId, treeView);
		}
		return treeView;
	}

	private void manageCache() {		
		String cacheActivate = Configurateur.getConfigValue("topicsCache");
		String refreshDelay = Configurateur.getConfigValue("topicsCache.refreshDelay");
		if (refreshDelay == null) refreshDelay = "3600000";
		if (cacheActivate == null) cacheActivate = "true";
		if ((System.currentTimeMillis()-treeViewCacheAge >= Long.valueOf(refreshDelay)) || cacheActivate.equals("false")) {
			treeViewCache.clear();
			treeViewCacheAge = System.currentTimeMillis();
		}
	}
}
