package com.silverpeas.tags.homepage;

import com.stratelia.silverpeas.pdc.control.PdcBm;
import com.stratelia.silverpeas.pdc.control.PdcBmImpl;
import com.stratelia.silverpeas.pdc.model.PdcException;
import com.stratelia.silverpeas.pdc.model.SearchAxis;
import com.stratelia.silverpeas.pdc.model.SearchContext;
import com.stratelia.silverpeas.pdc.model.Value;
import com.stratelia.silverpeas.peasCore.MainSessionController;
import com.stratelia.webactiv.beans.admin.OrganizationController;
import com.stratelia.webactiv.homepage.HomePageFunctions;
import com.stratelia.webactiv.util.viewGenerator.html.Encode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

public class CategoriesTreeTag extends TagSupport 
{
	static PdcBm pdcBm;

	protected String m_component_id;
	protected String m_sContext;
	protected String m_spaceOrSubSpace;
	protected String m_selected_axis_id;
	protected String m_selected_axis_path;
	protected String m_state;
	protected OrganizationController m_organizationCtrl;
	protected MainSessionController m_mainSessionCtrl;

	public void setComponent_id(String component_id)
	{
		this.m_component_id = component_id;
	}
	public void setSContext(String sContext)
	{
		this.m_sContext = sContext;
	}
	public void setSpaceOrSubSpace(String spaceOrSubSpace)
	{
		this.m_spaceOrSubSpace = spaceOrSubSpace;
	}
	public void setSelected_axis_id(String selected_axis_id)
	{
		this.m_selected_axis_id = selected_axis_id;
	}
	public void setSelected_axis_path(String selected_axis_path)
	{
		this.m_selected_axis_path = selected_axis_path;
	}
	public void setState(String state)
	{
		this.m_state = state;
	}
	public void setOrganizationCtrl(OrganizationController organizationCtrl)
	{
		this.m_organizationCtrl = organizationCtrl;
	}
	public void setMainSessionCtrl(MainSessionController mainSessionCtrl)
	{
		this.m_mainSessionCtrl = mainSessionCtrl;
	}    

	public int doStartTag() throws JspException
    {
		if (pdcBm == null)
		    pdcBm = (PdcBm) new PdcBmImpl();

		List primaryAxis = null;
		List daughters = null;

		try{
			if ( m_spaceOrSubSpace != null && !m_spaceOrSubSpace.equals("") )
			{
				ArrayList cmps = new ArrayList();
				SearchContext searchContext = new SearchContext();

				if ( m_component_id != null && !"".equals(m_component_id) )
				{
					primaryAxis = pdcBm.getPertinentAxisByInstanceId(searchContext, "P", m_component_id);
				}
				else
				{
					String a[] = m_organizationCtrl.getAvailCompoIds(m_spaceOrSubSpace, m_mainSessionCtrl.getUserId());
					for (int i=0; i<a.length;i++ )
					{
									cmps.add(a[i]);
					}
							if (cmps.size()>0)
							primaryAxis = pdcBm.getPertinentAxisByInstanceIds(searchContext, "P", cmps);
				}

				if ( primaryAxis != null && primaryAxis.size() > 0 )
				{
					if ( !"".equals(m_selected_axis_id) )
					{
						if ( m_component_id != null && !"".equals(m_component_id) )
						{
							daughters = pdcBm.getPertinentDaughterValuesByInstanceId(searchContext, m_selected_axis_id, m_selected_axis_path, m_component_id);
						}
						else
						{
							daughters = pdcBm.getPertinentDaughterValuesByInstanceIds(searchContext, m_selected_axis_id, m_selected_axis_path, cmps);
						}
					}
					else
					{
						SearchAxis searchAxis = (SearchAxis)primaryAxis.get(0);
						String axisId = new Integer(searchAxis.getAxisId()).toString();
						daughters = pdcBm.getPertinentDaughterValuesByInstanceIds(searchContext, axisId, "", cmps);
					}
				}
			}//end if
		}
		catch(PdcException e)
		{
			throw new JspTagException("PdcException occured : " + e.toString());
		}	

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

			// il peut y avoir aucun axe primaire dans un 1er temps
			if ( (primaryAxis != null) && (primaryAxis.size()>0) )
			{
				String text_link = "";

				for (int i=0; i<primaryAxis.size(); i++)
				{
					SearchAxis searchAxis = (SearchAxis)primaryAxis.get(i);
					String axisId = new Integer(searchAxis.getAxisId()).toString();
					String axisRootId = new Integer(searchAxis.getAxisRootId()).toString();
					String axisName = Encode.javaStringToHtmlString(searchAxis.getAxisName());

					int nbPositions = searchAxis.getNbObjects();
					if ( nbPositions == 0 && m_component_id != null )
					{
						continue;
					}
					String objectLinked = "";
					String link = "javascript:top.scriptFrame.axisClick('"+axisId+"','');";
					text_link = m_sContext + "/RpdcSearch/jsp/showaxishfromhomepage?query=&AxisId="+axisId+"&ValueId=/"+axisRootId+"/&SearchContext=isNotEmpty&component_id="+m_component_id+"&space_id="+m_spaceOrSubSpace;
					if ( axisId.equals(m_selected_axis_id) )
					{
						if ( daughters != null )
						{
							if ( "".equals(m_selected_axis_path) )
							{
								if ( "off".equals(m_state) )
								{
									pageContext.getOut().println(HomePageFunctions.urlFactory(link, text_link, axisId, "", objectLinked, axisName, "&nbsp;("+nbPositions+")", HomePageFunctions.AXIS, HomePageFunctions.AXIS_COLLAPSED, "", m_sContext));
									continue;
								}
							}
							pageContext.getOut().println(HomePageFunctions.urlFactory(link, text_link, axisId, "", objectLinked, axisName, "&nbsp;("+nbPositions+")", HomePageFunctions.AXIS, HomePageFunctions.AXIS_EXPANDED, "", m_sContext));
					//**************Begin daughters

							int d_size = daughters.size();

							int[][] tree_gui_represintation = new int[d_size][d_size];
							int selected_x = -1;
							int selected_y = 0;


							for ( int j=0; j<d_size; j++ )
							{
								Value value = (Value) daughters.get(j);
								int valueLevel = value.getLevelNumber();
								String path = value.getFullPath();
								HomePageFunctions.setTreeNode( tree_gui_represintation, valueLevel,  j, d_size );
								if ( m_selected_axis_path.equals(path) )
								{
									selected_x = valueLevel;
									selected_y = j;
								}
							}

							HomePageFunctions.closeTreeNodes( tree_gui_represintation, 0, d_size, d_size );
							if ( selected_x >=0 )
							{
								if ( m_state.equals("on") )
								{
									selected_x++;
									selected_y++;
								}
								HomePageFunctions.collapseTree( tree_gui_represintation, selected_x, selected_y, d_size );
							}

							for (int j = 1; j<d_size; j++)
							{
								Value value = (Value) daughters.get(j);
								String valueName = Encode.javaStringToHtmlString(value.getName());
								int valueLevel = value.getLevelNumber();
								int valueNbObjects = value.getNbObjects();
								if ( valueNbObjects == 0 && m_component_id != null )
								{
									continue;
								}
								String valueFullPath = value.getFullPath();
								link = "javascript:top.scriptFrame.axisClick('"+axisId+"','"+valueFullPath+"');";
								text_link = m_sContext + "/RpdcSearch/jsp/showaxishfromhomepage?query=&AxisId="+axisId+"&ValueId="+valueFullPath+"&SearchContext=isNotEmpty&component_id="+m_component_id+"&space_id="+m_spaceOrSubSpace;
								if ( tree_gui_represintation[0][j] == HomePageFunctions.T_HIDED_NODE )
								{
									continue;
								}
								String offset = HomePageFunctions.getTreeNodeOffset( tree_gui_represintation, j, d_size, m_sContext );
								int node_type;
								if ( tree_gui_represintation[valueLevel][j] == HomePageFunctions.T_OPENED_NODE )
								{
									node_type = HomePageFunctions.AXIS_EXPANDED;
								}
								else
								{
									node_type = HomePageFunctions.AXIS_COLLAPSED;
								}
								if ( m_selected_axis_path.equals("") )
								{
									if ( valueLevel == 1)
									{
										pageContext.getOut().println(HomePageFunctions.urlFactory(link, text_link, valueFullPath, "", objectLinked, valueName, " ("+valueNbObjects+")", HomePageFunctions.AXIS_LAST, HomePageFunctions.AXIS_COLLAPSED, offset, m_sContext));
									}
									continue;
								}
								else
								{
									pageContext.getOut().println(HomePageFunctions.urlFactory(link, text_link, valueFullPath, "", objectLinked, valueName, " ("+valueNbObjects+")", HomePageFunctions.AXIS_LAST, node_type, offset, m_sContext));
								}
							}
						}
					}
					else
					{
						pageContext.getOut().println(HomePageFunctions.urlFactory(link, text_link, axisId, "", objectLinked, axisName, "&nbsp;("+nbPositions+")", HomePageFunctions.AXIS, HomePageFunctions.AXIS_COLLAPSED, "", m_sContext));
					}
				}// fin du for
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