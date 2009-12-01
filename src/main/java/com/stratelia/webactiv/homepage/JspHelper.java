package com.stratelia.webactiv.homepage;

import com.stratelia.webactiv.beans.admin.ComponentInst;
import com.stratelia.webactiv.util.ResourceLocator;
import com.stratelia.silverpeas.peasCore.MainSessionController;

import java.util.ArrayList;
import java.util.Vector;

public class JspHelper {

    public static ArrayList sortComponentList(Vector vAllowedComponents, ArrayList alCompoInst) {

          ArrayList sortedComponents = new ArrayList();
          ArrayList labels = new ArrayList(vAllowedComponents.size());
          ArrayList indexes = new ArrayList();
          for(int idx = 0; idx < vAllowedComponents.size(); idx++) {
            int nK  = ((Integer)vAllowedComponents.get(idx)).intValue();
            String label = ((ComponentInst)alCompoInst.get(nK)).getLabel();
            labels.add(label);
            indexes.add(new Integer(idx));
          }

          int lastIndex = -1;
          while (labels.size() != 0) {
              int maxIdx = getMaxString(labels, lastIndex);
              if (maxIdx <0 ) {
                  break;
              }

              sortedComponents.add(vAllowedComponents.get(((Integer)indexes.get(maxIdx)).intValue()) /* vAllowedComponents.elementAt(maxIdx)*/);
              lastIndex = maxIdx;
          }

          return sortedComponents;
      }

          /**
           * @return max String index
           */
      public static int getMaxString(ArrayList labels, int currindx) {
              if (labels == null || (labels != null && labels.size() <= 0) ) {
                  return -1;
              }

              String currMaxString = null;
              String lastFoundedString = null;
              int currIdx = -1;

              if (currindx != -1) {
                  lastFoundedString = (String)labels.get(currindx);
              }

              for(int i = 0; i < labels.size(); i++) {
                  String str = (String)labels.get(i);

                  if (currMaxString == null) {
                      if (currindx == -1 || ( str.compareToIgnoreCase(lastFoundedString) > 0 )
                              || ( str.compareToIgnoreCase(lastFoundedString) == 0 && currindx < i)  ) {
                          currIdx = i;
                          currMaxString = str;
                      }
                      continue;
                  }


                  if ( (str.compareToIgnoreCase(currMaxString) < 0 && (lastFoundedString == null || str.compareToIgnoreCase(lastFoundedString) > 0) )
                          || ( str.compareToIgnoreCase(currMaxString) == 0  && currindx < i )) {
                      currIdx = i;
                      currMaxString = str;
                  }

              }

              return currIdx;
      }


   /**
    * @return formatted caption string for axes in DomainsBar.jsp
    */
    //public static String formatAxesCaption(String component_id, String domain, String subDomain, ResourceLocator message, MainSessionController m_MainSessionCtrl) {
	public static String formatAxesCaption(String component_id, String askingDomain, ResourceLocator message, MainSessionController m_MainSessionCtrl) {
        String additionalMessage = "";
        if (component_id == null || "".equals(component_id) ) {

            //String askingDomain = (subDomain == null || "".equals(subDomain) || "".equals(subDomain))? domain: subDomain;

			if (askingDomain != null && askingDomain.length() > 0)
				additionalMessage = m_MainSessionCtrl.getOrganizationController().getSpaceInstById(askingDomain).getName();

        } else {
            ComponentInst inst = m_MainSessionCtrl.getOrganizationController().getComponentInst(component_id);
            if (inst != null) {
                additionalMessage = inst.getLabel();
            } else {
                additionalMessage = "";
            }
        }

        return message.getString("AxisCollaboration") + " " + additionalMessage;
    }

}