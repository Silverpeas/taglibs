package com.silverpeas.tags.navigation.links;

import java.rmi.RemoteException;

import javax.servlet.jsp.PageContext;

import com.silverpeas.tags.kmelia.KmeliaTagUtil;
import com.stratelia.webactiv.util.node.model.NodeDetail;
import com.stratelia.webactiv.util.publication.model.PublicationDetail;

public interface LinkGenerator {
	public String generateFullSemanticPath(PageContext pageContext, KmeliaTagUtil themetracker, NodeDetail node, String idTopicRoot, PublicationDetail pub, String prefixId) throws RemoteException;
}
