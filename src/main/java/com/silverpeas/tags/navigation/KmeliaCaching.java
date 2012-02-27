package com.silverpeas.tags.navigation;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;

import com.silverpeas.tags.kmelia.KmeliaTagUtil;
import com.silverpeas.tags.navigation.config.Configurateur;
import com.stratelia.webactiv.util.node.model.NodeDetail;

public class KmeliaCaching {
	
	private static KmeliaCaching instance;	
	private KmeliaTagUtil themetracker = null;
	
	// Cache treeview
	private ConcurrentHashMap<String, Collection<NodeDetail>> treeViewCache = new ConcurrentHashMap<String, Collection<NodeDetail>>();
	
	// Cache topic
	private ConcurrentHashMap<String, NodeDetail> topicCache = new ConcurrentHashMap<String, NodeDetail>();
	
	private long cacheAge = System.currentTimeMillis();
	
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
		
		NodeDetail topic = topicCache.get(String.valueOf(topicId));
		if (topic == null) {
			Iterator<Collection<NodeDetail>> iTree = treeViewCache.values().iterator();
			while (iTree.hasNext()) {
				Collection<NodeDetail> trees = (Collection<NodeDetail>) iTree.next();
				Iterator<NodeDetail> iNodes = trees.iterator();
				while (iNodes.hasNext()) {
					NodeDetail node = (NodeDetail) iNodes.next();
					if (node.getId() == topicId) return node;
				}
			}
			
			synchronized(this) {
				topic = themetracker.getTopic(String.valueOf(topicId));
				topicCache.putIfAbsent(String.valueOf(topicId), topic);				
			}			
		} 
		return topic;
	}
	
	@SuppressWarnings("unchecked")
	public Collection<NodeDetail> getTreeView(String topicId) throws RemoteException {
		manageCache();
		Collection<NodeDetail> treeView = treeViewCache.get(topicId);
		if (treeView == null) {
			synchronized(this) {
				treeView = (Collection<NodeDetail>) themetracker.getTreeView(topicId);
				treeViewCache.putIfAbsent(topicId, treeView);
			}
		}
		return treeView;
	}

	private void manageCache() {		
		String cacheActivate = Configurateur.getConfigValue("topicsCache");
		String refreshDelay = Configurateur.getConfigValue("topicsCache.refreshDelay");
		if (refreshDelay == null) refreshDelay = "3600";
		if (cacheActivate == null) cacheActivate = "true";
		if ((System.currentTimeMillis()-cacheAge >= Long.valueOf(refreshDelay)*1000) || cacheActivate.equals("false")) {
			treeViewCache.clear();
			topicCache.clear();
			cacheAge = System.currentTimeMillis();
		}
	}
}
