package com.silverpeas.tags.organization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import com.silverpeas.admin.ejb.AdminBm;
import com.silverpeas.admin.ejb.AdminBmRuntimeException;
import com.silverpeas.tags.kmelia.KmeliaTagUtil;
import com.silverpeas.tags.util.EJBDynaProxy;
import com.silverpeas.tags.util.SiteTagUtil;
import com.stratelia.silverpeas.peasCore.MainSessionController;
import com.stratelia.silverpeas.peasCore.URLManager;
import com.stratelia.webactiv.beans.admin.ComponentInstLight;
import com.stratelia.webactiv.beans.admin.SpaceAndChildren;
import com.stratelia.webactiv.beans.admin.SpaceInstLight;
import com.stratelia.webactiv.util.JNDINames;
import com.stratelia.webactiv.util.exception.SilverpeasRuntimeException;
import com.stratelia.webactiv.util.node.model.NodeDetail;
import com.stratelia.webactiv.util.publication.model.PublicationDetail;
import java.util.Map;

public class OrganizationTagUtil implements java.io.Serializable
{
	private String				visibilityString	= null;
	private AdminBm				adminBm				= null;
	private MainSessionController mainSessionCtrl 	= null;
	private String				userId				= null;

	public OrganizationTagUtil(String visibilityString, MainSessionController mainSessionCtrl)
	{
		this.visibilityString = visibilityString;
		this.mainSessionCtrl = mainSessionCtrl;
	}

    public OrganizationTagUtil(String visibilityString)
    {
		this.visibilityString = visibilityString;
    }

	public OrganizationTagUtil()
    {
    }

	private AdminBm getAdminBm() 
	{
		if (adminBm == null) {
			try
			{			
				adminBm = (AdminBm)EJBDynaProxy.createProxy(JNDINames.ADMINBM_EJBHOME, AdminBm.class);
			}
			catch (Exception e)
			{
				throw new AdminBmRuntimeException("OrganizationTagUtil.getAdminBm", SilverpeasRuntimeException.ERROR,"root.EX_CANT_GET_REMOTE_OBJECT",e);
			}
		}
		return adminBm;
    }

	/**
	 * 
	 *
	 * return a Collection of SpaceInst object
	 */
	public Collection getOrgaTreeView() throws Exception
	{
		return getOrgaLimitedTreeView(1000);
	}

	public Collection getOrgaLimitedTreeViewStr(String depth) throws Exception {
		return getOrgaLimitedTreeView(Integer.parseInt(depth));
	}

	public Collection getOrgaLimitedTreeView(int depth) throws Exception
	{
		depth = depth - 1;

		ArrayList	spaceIds	= null;
		if(mainSessionCtrl == null)
			spaceIds = getAdminBm().getAllRootSpaceIds();
		else {
			String[] spaceIdsTemp = mainSessionCtrl.getUserAvailSpaceIds();
			spaceIds = new ArrayList();
			String spaceId = null;
			for (int i=0; i<spaceIdsTemp.length; i++)
			{
				spaceId = spaceIdsTemp[i];
				spaceIds.add(spaceId);
			}			
		}
			
		String		spaceId		= null;
		ArrayList	menuItems	= new ArrayList();
		SpaceInstLight   spaceInstLight	= null;
		int			level		= 0;

		for (int i=0; i<spaceIds.size(); i++)
		{
			spaceId		= (String) spaceIds.get(i);
			spaceInstLight	= getAdminBm().getSpaceInstLight(spaceId);

			if(mainSessionCtrl != null && spaceInstLight.getFatherId().equals("0"))
			{
				if (isVisible(spaceInstLight.getName()))
				{
					//put spaceInst
					menuItems.add(spaceInst2MenuItem(spaceInstLight));

					Map spaceTrees = getAdminBm().getTreeView(mainSessionCtrl.getUserId(), spaceId);

					//manage componentInsts
					menuItems = processComponentInsts(menuItems, spaceInstLight, spaceTrees);

					if (level < depth)
						menuItems	= processSpaceSubTree(menuItems, spaceInstLight, spaceTrees, level, depth);
				}
			}
			else
			{
				if (isVisible(spaceInstLight.getName()) && mainSessionCtrl == null)
				{
					//put spaceInst
					menuItems.add(spaceInst2MenuItem(spaceInstLight));

					Map spaceTrees = getAdminBm().getTreeView(getUserId(), spaceId);

					//manage componentInsts
					menuItems = processComponentInsts(menuItems, spaceInstLight, spaceTrees);

					if (level < depth)
						menuItems	= processSpaceSubTree(menuItems, spaceInstLight, spaceTrees,level, depth);
				}
			}
		}

		return menuItems;
	}
	
	/**
	 * @param spaceId - if of the space which is the root of the tree
	 * @param leafItem - MenuItem's type where to stop (0 : Space, 1 : Component, 2 : Topic or 3 : Publication)
	 * @return an ordered list of MenuItem
	 * @throws Exception
	 */
	public Collection getOrgaSubTreeViewToItem(String spaceIdAndLeafItem) throws Exception
	{
		StringTokenizer tokenizer = new StringTokenizer(spaceIdAndLeafItem, ",");
		int i = 1;
		String param		= "";
		String spaceId		= "";
		String sLeafItem	= null;
		while (tokenizer.hasMoreTokens()) {
			param = tokenizer.nextToken();
			if (i == 1)
				spaceId = param;
			else if (i == 2)
				sLeafItem = param;
			i++;
		}
					
		int leafItem = Integer.parseInt(sLeafItem);
		
		ArrayList	menuItems	= new ArrayList();
		int			level		= 0;
		String 		theUserId 		= getUserId();
		
		// Step 1 - build hashtable for recursivity
		Map spaceTrees = getAdminBm().getTreeView(theUserId, spaceId);
				
		// Step 2 - Use hashtable to recurse
		SpaceInstLight rootSpace = getAdminBm().getSpaceInstLight(spaceId);

		if (isVisible(rootSpace.getName()))
		{
			//put spaceInst
			menuItems.add(spaceInst2MenuItem(rootSpace));

			//manage componentInsts
			menuItems = processComponentInsts(menuItems, rootSpace, spaceTrees, leafItem);

			menuItems = processSpaceSubTree(menuItems, rootSpace, spaceTrees, level, 900, leafItem);
		}

		return menuItems;
	}

	public Collection getOrgaSubTreeView(String spaceId) throws Exception
	{
		System.out.println("getOrgaSubTreeView "+spaceId);
		ArrayList	menuItems	= new ArrayList();
		int			level		= 0;
		String 		theUserId 		= getUserId();
		
		// Step 1 - build hashtable for recursivity
		Map spaceTrees = getAdminBm().getTreeView(theUserId, spaceId);
		
		if (spaceTrees != null)
			System.out.println("spaceTrees "+spaceTrees.size());
		else
			System.out.println("spaceTrees : null");	
				
		// Step 2 - Use hashtable to recurse
		SpaceInstLight rootSpace = getAdminBm().getSpaceInstLight(spaceId);

		if (rootSpace != null)
			System.out.println("rootSpace "+rootSpace.getName());
		else
			System.out.println("rootSpace : null");

		if (isVisible(rootSpace.getName()))
		{
			//put spaceInst
			menuItems.add(spaceInst2MenuItem(rootSpace));

			//manage componentInsts
			menuItems = processComponentInsts(menuItems, rootSpace, spaceTrees);

			menuItems = processSpaceSubTree(menuItems, rootSpace, spaceTrees, level, 900);
		}

		return menuItems;
	}

	public Collection getItemPath(MenuItem item) throws Exception
	{
		ArrayList		path			= new ArrayList();
		
		String spaceId		= item.getSpaceId();
		String componentId	= item.getComponentId();

		if (componentId == null || componentId.length() == 0)
		{
			//the path of a space or a subspace is asked
			path = getSpacePath(path, spaceId);
		}
		else
		{
			ComponentInstLight componentInst = getAdminBm().getComponentInstLight(componentId);

			//the path of a component is asked
			path = getComponentPath(path, componentInst);

			String objectId = item.getId();
			if (objectId != null || objectId.length() > 0)
			{		
				//the path of a component's content is asked
				path.addAll(getContentPath(item, componentInst));
			}
		}

		return path;
	}

	private ArrayList getSpacePath(ArrayList path, String spaceId) throws Exception
	{
		SpaceInstLight spaceInst = getAdminBm().getSpaceInstLight(spaceId);
		path.add(0, spaceInst2MenuItem(spaceInst));
		if (!spaceInst.getFatherId().equals("0"))
		{
			path = getSpacePath(path, spaceInst.getFatherId());
		}
		return path;
	}

	private ArrayList getComponentPath(ArrayList path, ComponentInstLight componentInst) throws Exception
	{
		path.add(componentInst2MenuItem(componentInst));
		path = getSpacePath(path, componentInst.getDomainFatherId());
		return path;
	}

	private ArrayList getContentPath(MenuItem item, ComponentInstLight componentInst)
	{
		if (componentInst.getName().startsWith("kmelia"))
		{
			return (ArrayList) getKmeliaContentPath(item);
		} else {
			return new ArrayList();
		}
	}

	private Collection getKmeliaContentPath(MenuItem item)
	{
		KmeliaTagUtil	ktu				= new KmeliaTagUtil(item.getSpaceId(), item.getComponentId(), getUserId(), false);
		ArrayList		publicationPath = null;
		ArrayList		itemPath		= new ArrayList();
		NodeDetail		node			= null;
		try
		{
			publicationPath = (ArrayList) ktu.getPublicationPath(item.getId());
			//we shift the root theme
			for (int i=1; i<publicationPath.size(); i++)
			{
				node = (NodeDetail) publicationPath.get(i);
				itemPath.add(nodeDetail2MenuItem(node));
			}
		}
		catch (Exception e)
		{
			System.out.println(e.toString());
		}
		return itemPath;
	}
	
	private ArrayList processSpaceSubTree(ArrayList treeView, SpaceInstLight spaceInst, Map spaceTrees, int level, int depth) throws Exception
	{
		return processSpaceSubTree(treeView, spaceInst, spaceTrees, level, depth, MenuItem.TYPE_COMPONENT_CONTENT);
	}

	private ArrayList processSpaceSubTree(ArrayList treeView, SpaceInstLight spaceInst, Map spaceTrees, int level, int depth, int leafItem) throws Exception
	{
		SpaceInstLight   	subSpaceInst	= null;

		//recursive starts here
		SpaceAndChildren space = (SpaceAndChildren) spaceTrees.get(spaceInst.getFullId());
		Collection subSpaces = new ArrayList();
		if (space != null)
		    subSpaces = space.getSubspaces();
		
		level++;
		Iterator it = subSpaces.iterator();
		while (it.hasNext())
		{
            subSpaceInst = (SpaceInstLight) it.next();

			if (isVisible(subSpaceInst.getName()))
			{
				//put spaceInst
				subSpaceInst.setLevel(level);
				treeView.add(spaceInst2MenuItem(subSpaceInst));

				//manage componentInsts
				treeView = processComponentInsts(treeView, subSpaceInst,
						spaceTrees, leafItem);

				if (level < depth)
					treeView = processSpaceSubTree(treeView, subSpaceInst,
							spaceTrees, level, depth, leafItem);
			}//end if
        }
		return treeView;
	}
	
	private ArrayList processComponentInsts(ArrayList treeView, SpaceInstLight spaceInst, Map spaceTrees)
	{
		return processComponentInsts(treeView, spaceInst, spaceTrees, MenuItem.TYPE_COMPONENT_CONTENT);
	}
	
	private ArrayList processComponentInsts(ArrayList treeView, SpaceInstLight spaceInst, Map spaceTrees, int leafItem)
	{
		if (leafItem >= MenuItem.TYPE_COMPONENT)
		{
		    SpaceAndChildren space = (SpaceAndChildren) spaceTrees.get(spaceInst.getFullId());
		    if (space != null)
		    {
			    Iterator it = space.getComponents().iterator();
				while (it.hasNext())
				{
					ComponentInstLight componentInst = (ComponentInstLight) it.next();
					if (isVisible(componentInst.getLabel()))
					{
						treeView.add(componentInst2MenuItem(componentInst, spaceInst.getLevel()+1));
				
						//manage component organization and content
						treeView = processComponentContent(treeView, componentInst, spaceInst.getLevel()+1, leafItem);
					}
				}
		    }
		}

		return treeView;
	}
	
	private ArrayList processComponentContent(ArrayList treeView, ComponentInstLight componentInst, int level, int leafItem)
	{
		if (leafItem >= MenuItem.TYPE_COMPONENT_ORGA)
		{
			String componentName = componentInst.getName();
			if (componentName.startsWith("kmelia"))
			{
				treeView = processKmeliaContent(treeView, componentInst, level, leafItem);
			}
		}
		return treeView;
	}

	private ArrayList processKmeliaContent(ArrayList treeView, ComponentInstLight componentInst, int level, int leafItem) 
	{
		try
		{
			KmeliaTagUtil ktu = new KmeliaTagUtil("WA"+componentInst.getDomainFatherId(), componentInst.getId(), getUserId(), false);
			
			//get the full theme treeview
			ArrayList kmeliaTreeView = (ArrayList) ktu.getTreeView("0");

			NodeDetail			node		= null;
			PublicationDetail	publication = null;

			for (int i=0; i<kmeliaTreeView.size(); i++)
			{
				node = (NodeDetail) kmeliaTreeView.get(i);

				if (node.getNodePK().getId().equals("1") || node.getNodePK().getId().equals("2"))
				{
					//we do not process this topics
				}
				else
				{
					if (!node.getNodePK().getId().equals("0"))
					{
						//put the theme in the treeview
						treeView.add(nodeDetail2MenuItem(node, level-1));
					}
					
					if (leafItem == MenuItem.TYPE_COMPONENT_CONTENT)
					{
						//for the current theme, get the publications
					  	ArrayList publications = (ArrayList) ktu.getPublicationsByTopic(node.getNodePK().getId());

					  	for (int p=0; p<publications.size(); p++)
					  	{
							publication = (PublicationDetail) publications.get(p);

						  	//put each publications in the treeview
						  	treeView.add(publicationDetail2MenuItem(publication, level+node.getLevel()));
					  	}
					}
				}
			}
		}
		catch (Exception e)
		{
			System.out.println(e.toString());
		}

		return treeView;
	}

	private MenuItem spaceInst2MenuItem(SpaceInstLight spaceInst)
	{
		MenuItem item = new MenuItem(spaceInst.getName(), spaceInst.getDescription(), spaceInst.getLevel(), MenuItem.TYPE_SPACE, spaceInst.getFullId(), spaceInst.getFatherId());
		item.setSpaceId(spaceInst.getFullId());
		return item;
	}

	private MenuItem componentInst2MenuItem(ComponentInstLight componentInst)
	{
		return componentInst2MenuItem(componentInst, 0);
	}

	private MenuItem componentInst2MenuItem(ComponentInstLight componentInst, int level)
	{
		MenuItem item = new MenuItem(componentInst.getLabel(), componentInst.getDescription(), level, MenuItem.TYPE_COMPONENT, componentInst.getId(), componentInst.getDomainFatherId());
		item.setComponentId(componentInst.getId());
		if(mainSessionCtrl != null)
		{
			item.setIconPath("/util/icons/component/" + componentInst.getName() + "Small.gif");
			item.setHrefPath(URLManager.getURL(null, componentInst.getId()) + "Main.jsp");
			item.setHrefTitle(componentInst.getDescription());
		}
		return item;
	}

	private MenuItem nodeDetail2MenuItem(NodeDetail node)
	{
		return nodeDetail2MenuItem(node, 0);
	}

	private MenuItem nodeDetail2MenuItem(NodeDetail node, int level)
	{
		MenuItem item = new MenuItem(node.getName(), node.getDescription(), level+node.getLevel(), MenuItem.TYPE_COMPONENT_ORGA, node.getNodePK().getId(), node.getFatherPK().getId());
		item.setSpaceId(node.getNodePK().getSpace());
		item.setComponentId(node.getNodePK().getComponentName());
		return item;
	}

	private MenuItem publicationDetail2MenuItem(PublicationDetail publication, int level)
	{
		MenuItem item = new MenuItem(publication.getName(), publication.getDescription(), level, MenuItem.TYPE_COMPONENT_CONTENT, publication.getPK().getId(), "unknown");
		item.setSpaceId(publication.getPK().getSpace());
		item.setComponentId(publication.getPK().getComponentName());
		return item;
	}

	private boolean isVisible(String itemName)
	{
		return ((visibilityString == null) || !itemName.startsWith(visibilityString));
	}

	public String getUserId()
	{
		if (userId != null)
        {
            return userId;
        }
        if (mainSessionCtrl == null)
        {
            return SiteTagUtil.getUserId();
        } 
        else
        {
            return mainSessionCtrl.getUserId();
        }
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

}