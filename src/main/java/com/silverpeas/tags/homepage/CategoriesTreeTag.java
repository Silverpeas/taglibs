/**
 * Copyright (C) 2000 - 2012 Silverpeas
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU Affero General Public License as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of the GPL, you may
 * redistribute this Program in connection with Free/Libre Open Source Software ("FLOSS")
 * applications as described in Silverpeas's FLOSS exception. You should have received a copy of the
 * text describing the FLOSS exception, and it is also available here:
 * "http://www.silverpeas.org/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */
package com.silverpeas.tags.homepage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

import org.silverpeas.core.admin.OrganisationController;

import com.silverpeas.util.EncodeHelper;
import com.silverpeas.util.StringUtil;

import com.stratelia.silverpeas.pdc.control.PdcBm;
import com.stratelia.silverpeas.pdc.control.PdcBmImpl;
import com.stratelia.silverpeas.pdc.model.PdcException;
import com.stratelia.silverpeas.pdc.model.SearchAxis;
import com.stratelia.silverpeas.pdc.model.SearchContext;
import com.stratelia.silverpeas.pdc.model.Value;
import com.stratelia.silverpeas.peasCore.MainSessionController;
import com.stratelia.webactiv.beans.admin.OrganizationController;
import com.stratelia.webactiv.homepage.HomePageFunctions;

public class CategoriesTreeTag extends TagSupport {

  static PdcBm pdcBm;
  protected String componentId;
  protected String webContext;
  protected String m_spaceOrSubSpace;
  protected String selectedAxisId;
  protected String selectedAxisPath;
  protected String state;
  protected OrganisationController oganisationController;
  protected MainSessionController mainSessionController;

  public void setComponent_id(String component_id) {
    this.componentId = component_id;
  }

  public void setSContext(String sContext) {
    this.webContext = sContext;
  }

  public void setSpaceOrSubSpace(String spaceOrSubSpace) {
    this.m_spaceOrSubSpace = spaceOrSubSpace;
  }

  public void setSelected_axis_id(String selected_axis_id) {
    this.selectedAxisId = selected_axis_id;
  }

  public void setSelected_axis_path(String selected_axis_path) {
    this.selectedAxisPath = selected_axis_path;
  }

  public void setState(String state) {
    this.state = state;
  }

  public void setOrganizationCtrl(OrganizationController organizationCtrl) {
    this.oganisationController = organizationCtrl;
  }

  public void setMainSessionCtrl(MainSessionController mainSessionCtrl) {
    this.mainSessionController = mainSessionCtrl;
  }

  @Override
  public int doStartTag() throws JspException {
    if (pdcBm == null) {
      pdcBm = (PdcBm) new PdcBmImpl();
    }

    List<SearchAxis> primaryAxis = null;
    List daughters = null;

    try {
      if (StringUtil.isDefined(m_spaceOrSubSpace)) {
        List<String> availableComponentIds = new ArrayList<String>();
        SearchContext searchContext = new SearchContext(mainSessionController.getUserId());

        if (StringUtil.isDefined(componentId)) {
          primaryAxis = pdcBm.getPertinentAxisByInstanceId(searchContext, "P", componentId);
        } else {
          String a[] = oganisationController.getAvailCompoIds(m_spaceOrSubSpace,
              mainSessionController.getUserId());
          availableComponentIds = Arrays.asList(a);
          if (!availableComponentIds.isEmpty()) {
            primaryAxis = pdcBm.getPertinentAxisByInstanceIds(searchContext, "P",
                availableComponentIds);
          }
        }

        if (primaryAxis != null && !primaryAxis.isEmpty()) {
          if (!"".equals(selectedAxisId)) {
            if (StringUtil.isDefined(componentId)) {
              daughters = pdcBm.
                  getPertinentDaughterValuesByInstanceId(searchContext, selectedAxisId,
                  selectedAxisPath, componentId);
            } else {
              daughters = pdcBm.getPertinentDaughterValuesByInstanceIds(searchContext,
                  selectedAxisId, selectedAxisPath, availableComponentIds);
            }
          } else {
            SearchAxis searchAxis = primaryAxis.get(0);
            String axisId = new Integer(searchAxis.getAxisId()).toString();
            daughters =
                pdcBm.getPertinentDaughterValuesByInstanceIds(searchContext, axisId, "",
                availableComponentIds);
          }
        }
      }// end if
    } catch (PdcException e) {
      throw new JspTagException("PdcException occured : " + e.toString());
    }

    try {
      pageContext.getOut().println("<tr class='intfdcolor51'>");
      pageContext.getOut().println("	<td width='100%'>");
      pageContext.getOut().println(
          "		<table border='0' cellspacing='0' cellpadding='0' width='100%'>");
      pageContext.getOut().println("			<tr>");
      pageContext.getOut().println("				<td>&nbsp;</td>");
      pageContext.getOut().println("				<td width='100%'><span class='txtnote'>");
      pageContext.getOut()
          .println("					<table cellpadding=0 cellspacing=0 border=0 width='100%'>");
      pageContext.getOut().println("						<tr><td>");

      // il peut y avoir aucun axe primaire dans un 1er temps
      if ((primaryAxis != null) && !primaryAxis.isEmpty()) {
        String text_link = "";
        for (SearchAxis searchAxis : primaryAxis) {
          String axisId = new Integer(searchAxis.getAxisId()).toString();
          String axisRootId = new Integer(searchAxis.getAxisRootId()).toString();
          String axisName = EncodeHelper.javaStringToHtmlString(searchAxis.getAxisName());

          int nbPositions = searchAxis.getNbObjects();
          if (nbPositions == 0 && componentId != null) {
            continue;
          }
          String objectLinked = "";
          String link = "javascript:top.scriptFrame.axisClick('" + axisId + "','');";
          text_link =
              webContext + "/RpdcSearch/jsp/showaxishfromhomepage?query=&AxisId=" + axisId
              + "&ValueId=/" + axisRootId + "/&SearchContext=isNotEmpty&component_id="
              + componentId + "&space_id=" + m_spaceOrSubSpace;
          if (axisId.equals(selectedAxisId)) {
            if (daughters != null) {
              if ("".equals(selectedAxisPath)) {
                if ("off".equals(state)) {
                  pageContext.getOut().println(HomePageFunctions.urlFactory(link, text_link, axisId,
                      "", objectLinked, axisName, "&nbsp;(" + nbPositions + ")",
                      HomePageFunctions.AXIS, HomePageFunctions.AXIS_COLLAPSED, "", webContext));
                  continue;
                }
              }
              pageContext.getOut().println(
                  HomePageFunctions.urlFactory(link, text_link, axisId, "", objectLinked, axisName,
                  "&nbsp;(" + nbPositions + ")", HomePageFunctions.AXIS,
                  HomePageFunctions.AXIS_EXPANDED, "", webContext));
              // **************Begin daughters

              int d_size = daughters.size();

              int[][] tree_gui_represintation = new int[d_size][d_size];
              int selected_x = -1;
              int selected_y = 0;

              for (int j = 0; j < d_size; j++) {
                Value value = (Value) daughters.get(j);
                int valueLevel = value.getLevelNumber();
                String path = value.getFullPath();
                HomePageFunctions.setTreeNode(tree_gui_represintation, valueLevel, j, d_size);
                if (selectedAxisPath.equals(path)) {
                  selected_x = valueLevel;
                  selected_y = j;
                }
              }

              HomePageFunctions.closeTreeNodes(tree_gui_represintation, 0, d_size, d_size);
              if (selected_x >= 0) {
                if (state.equals("on")) {
                  selected_x++;
                  selected_y++;
                }
                HomePageFunctions.collapseTree(tree_gui_represintation, selected_x, selected_y,
                    d_size);
              }

              for (int j = 1; j < d_size; j++) {
                Value value = (Value) daughters.get(j);
                String valueName = EncodeHelper.javaStringToHtmlString(value.getName());
                int valueLevel = value.getLevelNumber();
                int valueNbObjects = value.getNbObjects();
                if (valueNbObjects == 0 && componentId != null) {
                  continue;
                }
                String valueFullPath = value.getFullPath();
                link = "javascript:top.scriptFrame.axisClick('" + axisId + "','" + valueFullPath
                    + "');";
                text_link = webContext + "/RpdcSearch/jsp/showaxishfromhomepage?query=&AxisId="
                    + axisId + "&ValueId=" + valueFullPath
                    + "&SearchContext=isNotEmpty&component_id=" + componentId + "&space_id="
                    + m_spaceOrSubSpace;
                if (tree_gui_represintation[0][j] == HomePageFunctions.T_HIDED_NODE) {
                  continue;
                }
                String offset =
                    HomePageFunctions.getTreeNodeOffset(tree_gui_represintation, j, d_size,
                    webContext);
                int node_type;
                if (tree_gui_represintation[valueLevel][j] == HomePageFunctions.T_OPENED_NODE) {
                  node_type = HomePageFunctions.AXIS_EXPANDED;
                } else {
                  node_type = HomePageFunctions.AXIS_COLLAPSED;
                }
                if (selectedAxisPath.equals("")) {
                  if (valueLevel == 1) {
                    pageContext.getOut().println(
                        HomePageFunctions.urlFactory(link, text_link, valueFullPath, "",
                        objectLinked, valueName, " (" + valueNbObjects + ")",
                        HomePageFunctions.AXIS_LAST, HomePageFunctions.AXIS_COLLAPSED, offset,
                        webContext));
                  }
                  continue;
                } else {
                  pageContext.getOut().println(
                      HomePageFunctions.urlFactory(link, text_link, valueFullPath, "",
                      objectLinked, valueName, " (" + valueNbObjects + ")",
                      HomePageFunctions.AXIS_LAST, node_type, offset, webContext));
                }
              }
            }
          } else {
            pageContext.getOut().println(
                HomePageFunctions.urlFactory(link, text_link, axisId, "", objectLinked, axisName,
                "&nbsp;(" + nbPositions + ")", HomePageFunctions.AXIS,
                HomePageFunctions.AXIS_COLLAPSED, "", webContext));
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
    } catch (IOException ioe) {
      throw new JspTagException("IO_ERROR");
    }

    return SKIP_BODY;
  }
}