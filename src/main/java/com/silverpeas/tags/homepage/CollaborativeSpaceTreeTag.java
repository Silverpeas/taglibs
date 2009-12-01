package com.silverpeas.tags.homepage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import com.stratelia.silverpeas.peasCore.MainSessionController;
import com.stratelia.silverpeas.peasCore.URLManager;
import com.stratelia.webactiv.beans.admin.ComponentInst;
import com.stratelia.webactiv.beans.admin.OrganizationController;
import com.stratelia.webactiv.beans.admin.SpaceInst;
import com.stratelia.webactiv.homepage.HomePageFunctions;

public class CollaborativeSpaceTreeTag extends TagSupport 
{
	protected String m_sPrivateDomain;
	protected OrganizationController m_organizationCtrl;
	protected String[] m_asPrivateDomainsIds;
	protected String m_subDomain;
	protected String m_sContext;
	protected MainSessionController m_mainSessionCtrl;

	public void setSPrivateDomain(String sPrivateDomain)
	{
		this.m_sPrivateDomain = sPrivateDomain;
	}
	public void setOrganizationCtrl(OrganizationController organizationCtrl)
	{
		this.m_organizationCtrl = organizationCtrl;
	}
	public void setAsPrivateDomainsIds(String[] asPrivateDomainsIds)
	{
		this.m_asPrivateDomainsIds = asPrivateDomainsIds;
	}
	public void setSubDomain(String subDomain)
	{
		this.m_subDomain = subDomain;
	}
	public void setSContext(String sContext)
	{
		this.m_sContext = sContext;
	}
	public void setMainSessionCtrl(MainSessionController mainSessionCtrl)
	{
		this.m_mainSessionCtrl = mainSessionCtrl;
	}

	public int doStartTag() throws JspException
    {
        try
		{
			pageContext.getOut().println("<tr class='intfdcolor51'>");
			pageContext.getOut().println("	<td width='100%'>");
			pageContext.getOut().println("		<table border='0' cellspacing='0' cellpadding='0' width='100%'>");
			pageContext.getOut().println("			<tr>");
			pageContext.getOut().println("				<td>&nbsp;</td>");
			pageContext.getOut().println("				<td width='100%'><span class='txtnote'>");
			pageContext.getOut().println("					<table cellpadding=0 cellspacing=0 border=0 width='100%'>");
			pageContext.getOut().println("						<tr><td>");

			int elementId = 0;
			String elementLabel = "";
			int spaceId = 0;
			String objectLinked = null;
			String link = null;

			if ((m_sPrivateDomain != null) && (m_sPrivateDomain.length()>0))
			{
			  // Get all sub spaces
			  String[] asSubSpaceIds = m_organizationCtrl.getAllSubSpaceIds(m_sPrivateDomain);
			  String   sSubSpaceId;
			  ArrayList alSubSpaces = new ArrayList();

			  for (int nI = 0; nI < asSubSpaceIds.length; nI++)
			  {
					alSubSpaces.add(asSubSpaceIds[nI]);
			  }
			  // Keep only those available to current user
			  while (alSubSpaces.size() > 0)
			  {
				  boolean bFound = false;
				  sSubSpaceId = (String)alSubSpaces.remove(0);
				  asSubSpaceIds = m_organizationCtrl.getAllSubSpaceIds(sSubSpaceId);
				  for (int nI = 0; nI < asSubSpaceIds.length; nI++)
				  {
						alSubSpaces.add(nI,asSubSpaceIds[nI]);
				  }

				  for (int nJ = 0; nJ < m_asPrivateDomainsIds.length && !bFound; nJ++)
				  {
					  if (sSubSpaceId.equals(m_asPrivateDomainsIds[nJ]))
					  {
						  bFound = true;

						  if (m_subDomain!=null && m_subDomain.equals(sSubSpaceId))
													  {
							  SpaceInst spaceInst = m_organizationCtrl.getSpaceInstById(sSubSpaceId);
							  String label = spaceInst.getName();
							  String spaceLabel = "space"+spaceId;

							  objectLinked = "";

							  link = "javascript:top.scriptFrame.changeSubSpace('','"+m_sContext+"/admin/jsp/Main.jsp');";
							  pageContext.getOut().println(HomePageFunctions.getTabSpaces(spaceInst.getLevel()) + HomePageFunctions.urlFactory(link, "", spaceLabel, "", objectLinked, label, "", HomePageFunctions.SPACE, HomePageFunctions.SPACE_EXPANDED, "", m_sContext));
							  String[] asAvailCompoForCurUser = m_organizationCtrl.getAvailDriverCompoIds(spaceInst.getId(), m_mainSessionCtrl.getUserId());

							  // Get all the component instances for the space
							  ArrayList alCompoInst = spaceInst.getAllComponentsInst();

							  String[] asCompoNames = new String[alCompoInst.size()];
							  Vector vAllowedComponents = new Vector();

							  // Build Vector of indexes of allowed components
							  for(int nK = 0; nK <asCompoNames.length; nK++)
							  {
								  // Check if the component is accessible to the user
								  boolean bAllowed = false;
								  for(int nL=0; asAvailCompoForCurUser != null && nL < asAvailCompoForCurUser.length; nL++)
									  if( HomePageFunctions.getDriverComponentId( ( (ComponentInst)alCompoInst.get(nK) ).getId()).equals(asAvailCompoForCurUser[nL]) )
										  bAllowed = true;

								  if(bAllowed)
										vAllowedComponents.add(new Integer(nK));
							  }

							  // Print the allowed components
							  String id;

							  // ArrayList sortedList  = JspHelper.sortComponentList(vAllowedComponents, alCompoInst);
							  Vector sortedList  = vAllowedComponents;

							  for (int nAC=0; nAC<sortedList.size(); nAC++)
							  {
								  int nK = ((Integer) sortedList.get(nAC)).intValue();
								  label = ((ComponentInst)alCompoInst.get(nK)).getLabel();
								  if ((label == null) || (label.length() == 0))
									  label = ((ComponentInst)alCompoInst.get(nK)).getName();
								  id = ((ComponentInst)alCompoInst.get(nK)).getId();
								  elementId++;
								  elementLabel = "element"+elementId;
								  objectLinked = ((ComponentInst)alCompoInst.get(nK)).getName();
								  link = m_sContext + URLManager.getURL(((ComponentInst)alCompoInst.get(nK)).getName(), sSubSpaceId, ((ComponentInst)alCompoInst.get(nK)).getId()) + "Main";
								  
								  // Afffiche les sous espaces
								  if (nAC == (sortedList.size()-1)) {
									  pageContext.getOut().println(HomePageFunctions.getTabSpaces(spaceInst.getLevel()) + HomePageFunctions.urlFactory(link, "", elementLabel, id, objectLinked, label, "", HomePageFunctions.COMPONENT, HomePageFunctions.SUBSPACE_LAST_COMPONENT, "", m_sContext));
								  } else {
									  pageContext.getOut().println(HomePageFunctions.getTabSpaces(spaceInst.getLevel()) + HomePageFunctions.urlFactory(link, "", elementLabel, id, objectLinked, label, "", HomePageFunctions.COMPONENT, HomePageFunctions.SUBSPACE_COMPONENT, "", m_sContext));
								  }
							  }
						  }
						  else
						  {
							  spaceId++;
							  SpaceInst spaceInst = m_organizationCtrl.getSpaceInstById(sSubSpaceId);
							  String label = spaceInst.getName();
							  String spaceLabel = "space"+spaceId;
							  objectLinked = "";
							  link = "javascript:top.scriptFrame.changeSubSpace('"+ sSubSpaceId +"','"+m_sContext+"/admin/jsp/Main.jsp');";
							  pageContext.getOut().println(HomePageFunctions.getTabSpaces(spaceInst.getLevel()) + HomePageFunctions.urlFactory(link, "", spaceLabel, "", objectLinked, label, "", HomePageFunctions.SPACE, HomePageFunctions.SPACE_COLLAPSE, "", m_sContext));
							}
					  }
				  }
			  }

			  SpaceInst spaceInst = m_organizationCtrl.getSpaceInstById(m_sPrivateDomain);
			  String[] asAvailCompoForCurUser = m_organizationCtrl.getAvailDriverCompoIds(spaceInst.getId(), m_mainSessionCtrl.getUserId());

			  // Get all the component instances for the space
			  ArrayList alCompoInst = spaceInst.getAllComponentsInst();

			  String[] asCompoNames = new String[alCompoInst.size()];
			  String id;
			  Vector allowedComponents = new Vector();

			  for(int nI = 0; nI <asCompoNames.length; nI++)
			  {
				  // Check if the component is accassible to the user
				  boolean bAllowed = false;

				  for(int nJ=0; asAvailCompoForCurUser != null && nJ < asAvailCompoForCurUser.length; nJ++)
					  if( HomePageFunctions.getDriverComponentId( ( (ComponentInst)alCompoInst.get(nI) ).getId()).equals(asAvailCompoForCurUser[nJ]) )
						  bAllowed = true;

				  if(bAllowed)
				  {
					  allowedComponents.add(new Integer(nI));
				  }
			  }

			  // ArrayList sortedComponents = JspHelper.sortComponentList(allowedComponents, alCompoInst);
			  Vector sortedComponents = allowedComponents;
			  for(int nI = 0; nI < sortedComponents.size(); nI++)
			  {
				  int nK = ((Integer) sortedComponents.get(nI)).intValue();

				  String label = ((ComponentInst)alCompoInst.get(nK)).getLabel();
				  if ((label == null) || (label.length() == 0))
					  label = ((ComponentInst)alCompoInst.get(nK)).getName();
				  id = ((ComponentInst)alCompoInst.get(nK)).getId();
				  elementId++;
				  elementLabel = "element"+elementId;
				  objectLinked = ((ComponentInst)alCompoInst.get(nK)).getName();
				  link = m_sContext + URLManager.getURL(((ComponentInst)alCompoInst.get(nK)).getName(), m_sPrivateDomain, ((ComponentInst)alCompoInst.get(nK)).getId()) + "Main";
				  pageContext.getOut().println(HomePageFunctions.urlFactory(link, "", elementLabel, id, objectLinked, label, "", HomePageFunctions.COMPONENT, HomePageFunctions.SPACE_COMPONENT, "", m_sContext));
			  }

		  }



			pageContext.getOut().println("						</td></tr>");
			pageContext.getOut().println("					</table>");
			pageContext.getOut().println("				</span></td>");
			pageContext.getOut().println("			</tr>");
			pageContext.getOut().println("		</table>");
			pageContext.getOut().println("	</td>");
			pageContext.getOut().println("	<td><img src='icons/1px.gif'></td>");
			pageContext.getOut().println("	<td class='intfdcolor'><img src='icons/1px.gif'></td>");
			pageContext.getOut().println("</tr>");
		} 
		catch(IOException ioe) 
		{
            // User probably disconnected ...
            throw new JspTagException("IO_ERROR");
        }

        return SKIP_BODY;
    }

}