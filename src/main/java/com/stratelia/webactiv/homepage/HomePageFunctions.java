package com.stratelia.webactiv.homepage;

public class HomePageFunctions
{
	// Constants used by urlFactory
	public final static int SPACE      = 0;
	public final static int COMPONENT  = 1;

	public final static int SPACE_COLLAPSE             = 2;
	public final static int SPACE_EXPANDED             = 3;
	public final static int SPACE_COMPONENT            = 4;
	public final static int SUBSPACE_COMPONENT         = 5;
	public final static int SUBSPACE_LAST_COMPONENT    = 6;

	public final static int AXIS			= 7;
	public final static int AXIS_COLLAPSED = 8;
	public final static int AXIS_EXPANDED	= 9;
	public final static int AXIS_LAST		= 10;

	//---------------------------------------//
	public static final int T_OUT = -1;
    public static final int T_TREE_T_IMAGE = 0;
    public static final int T_TREE_L_IMAGE = 1;
    public static final int T_TREE_I_IMAGE = 3;
    public static final int T_TREE_SPACE_IMAGE = 2;
    public static final int T_OPENED_NODE = 5;
    public static final int T_CLOSED_NODE = 7;
    public static final int T_HIDED_NODE = 9;

	public static String urlFactory(String link, String text_link, String elementLabel, String id,
							  String imageLinked, String labelLinked, String labelLinkedNotBold,
							  int elementType, int imageType, String level, String m_sContext)
	{
		String imageOn      = m_sContext +"/util/icons/component/"+ imageLinked;
		StringBuffer result = new StringBuffer();
		String target       = "";
		String textlink_target = "";
		String boldStart    = "";
		String boldEnd  = "";
		String offset   = "";
		boolean is_axis = false;
		boolean isComponentOfSubSpace = false;

		imageLinked = "<img name=\""+ elementLabel +"\" src=\""+ m_sContext +"/util/icons/component/"+ imageLinked
					+ "Small.gif\" border=\"0\" onLoad=\"\" align=\"absmiddle\">";

		switch (elementType)
		{
		case SPACE :
				target  = "";
				boldStart = "<b>";
				boldEnd   = "</b>";
				break;

		case COMPONENT :
				target  = "TARGET=\"MyMain\"";
				boldStart = "<font color=666666>";
				boldEnd   = "</font>";
				break;
		case AXIS :
				textlink_target = "TARGET=\"MyMain\"";
				boldStart = "<b>";
				boldEnd   = "</b>";
				offset += level;
				break;
			case AXIS_LAST :
				textlink_target = "TARGET=\"MyMain\"";
				boldStart = "<b>";
				boldEnd   = "</b>";
				offset += level;
				break;
		}//end switch

		switch (imageType)
		{
			case AXIS_COLLAPSED :
				result.append(offset).append("<a href=\"").append(link).append("\" ").append(target).append(" class=\"txtnote\"><img src=").append(m_sContext).append("/pdcPeas/jsp/icons/pdcPeas_maximize.gif border=0 align=\"absmiddle\"></a>");
				imageLinked = "<img name=\""+elementLabel+"\" src=\"icons/1px.gif\" width=1 height=1 border=0 align=\"absmiddle\">";
				imageOn = m_sContext +"/util/icons/noColorPix/16px.gif";
				is_axis = true;
				break;

			case AXIS_EXPANDED :
				result.append(offset).append("<a href=\"").append(link).append("\" ").append(target).append("  class=\"txtnote\"><img src=\"").append(m_sContext).append("/pdcPeas/jsp/icons/pdcPeas_minimize.gif\" border=0 align=\"absmiddle\"></a>");
				imageLinked = "<img name=\""+elementLabel+"\" src=\"icons/1px.gif\" width=1 height=1 border=0 align=\"absmiddle\">";
				imageOn = m_sContext +"/util/icons/noColorPix/16px.gif";
				is_axis = true;
				break;
			case AXIS_LAST :
				result.append(offset).append("<a href=\"").append(link).append("\" ").append(target).append("  class=\"txtnote\"><img src=\"").append(m_sContext).append("/pdcPeas/jsp/icons/pdcPeas_minimize.gif\" border=0 align=\"absmiddle\"></a>");
				imageLinked = "<img name=\""+elementLabel+"\" src=\"icons/1px.gif\" width=1 height=1 border=0 align=\"absmiddle\">";
				imageOn = m_sContext +"/util/icons/noColorPix/16px.gif";
				is_axis = true;
				break;

			case SPACE_COLLAPSE :
				result.append("<a href=\"").append(link).append("\" ").append(target).append(" class=\"txtnote\"><img src=").append(m_sContext).append("/util/icons/plusTree.gif border=0 align=\"absmiddle\"></a>");
				imageLinked = "<img name=\""+elementLabel+"\" src=\"icons/1px.gif\" width=1 height=1 border=0 align=\"absmiddle\">";
				imageOn = m_sContext +"/util/icons/noColorPix/16px.gif";
				break;

			case SPACE_EXPANDED :
				result.append("<a href=\"").append(link).append("\" ").append(target).append("  class=\"txtnote\"><img src=\"").append(m_sContext).append("/util/icons/minusTree.gif\" border=0 align=\"absmiddle\"></a>");
				imageLinked = "<img name=\""+elementLabel+"\" src=\"icons/1px.gif\" width=1 height=1 border=0 align=\"absmiddle\">";
				imageOn = m_sContext +"/util/icons/noColorPix/16px.gif";
				break;

			case SPACE_COMPONENT :
				break;

			case SUBSPACE_COMPONENT :
				isComponentOfSubSpace = true;
				result.append("<img src=\"").append(m_sContext).append("/util/icons/minusTreeT.gif\" border=0 align=\"absmiddle\">");
				break;

			case SUBSPACE_LAST_COMPONENT :
				isComponentOfSubSpace = true;
				result.append("<img src=\"").append(m_sContext).append("/util/icons/minusTreeL.gif\" border=0 align=\"absmiddle\">");
				break;
		}//end switch

		// if link is a javascript method, remove the target
		// necessary for called method visibility
		if (link.startsWith("javascript")) target = "";

		if ( text_link == null || "".equals(text_link) )
		{
			text_link = link;
			textlink_target = target;
		}

		if ( is_axis )
		{
			if ( id != null && !id.equals("") )
			{
				result.append("<a href=\"").append(link).append("\" ").append(target).append(" onClick=\"top.scriptFrame.setComponent('").append(id).append("','").append(elementLabel).append("','").append(imageOn).append("');return true\" class=\"txtnote\">").append(imageLinked).append("<img src=icons/ComponentsPoints.gif border=0 align=\"absmiddle\"></a>");
				result.append("<a href=\"").append(text_link).append("\" ").append(textlink_target).append(" class=\"txtnote\">").append(boldStart).append(labelLinked).append(boldEnd).append(labelLinkedNotBold).append("</a>");
				result.append("<br>");
			}
			else
			{
				result.append("<a href=\"").append(link).append("\" ").append(target).append(" onClick=\"top.scriptFrame.rollActiv('").append(elementLabel).append("','").append(imageOn).append("');return true\" class=\"txtnote\">").append(imageLinked).append("<img src=icons/ComponentsPoints.gif border=0 align=\"absmiddle\"></a>");
				result.append("<a href=\"").append(text_link).append("\" ").append(textlink_target).append(" class=\"txtnote\">").append(boldStart).append(labelLinked).append(boldEnd).append(labelLinkedNotBold).append("</a>");
				result.append("<br>");
			}
		}
		else
		{
			if ( id != null && !id.equals("") )
			{
				result.append("<a href=\"").append(link).append("\" ").append(target).append(" onClick=\"top.scriptFrame.setComponent('").append(id).append("','").append(elementLabel).append("','").append(imageOn).append("',").append(isComponentOfSubSpace).append(");return true\" class=\"txtnote\">").append(imageLinked).append("<img src=icons/ComponentsPoints.gif border=0 align=\"absmiddle\"></a>");
				result.append("<a href=\"").append(text_link).append("\" ").append(textlink_target).append(" onClick=\"top.scriptFrame.setComponent('").append(id).append("','").append(elementLabel).append("','").append(imageOn).append("',").append(isComponentOfSubSpace).append(");return true\" class=\"txtnote\">").append(boldStart).append(labelLinked).append(boldEnd).append(labelLinkedNotBold).append("</a>");
				result.append("<br>");
			}
			else
			{
				result.append("<a href=\"").append(link).append("\" ").append(target).append(" onClick=\"top.scriptFrame.rollActiv('").append(elementLabel).append("','").append(imageOn).append("');return true\" class=\"txtnote\">").append(imageLinked).append("<img src=icons/ComponentsPoints.gif border=0 align=\"absmiddle\"></a>");
				result.append("<a href=\"").append(text_link).append("\" ").append(textlink_target).append(" onClick=\"top.scriptFrame.rollActiv('").append(elementLabel).append("','").append(imageOn).append("');return true\" class=\"txtnote\">").append(boldStart).append(labelLinked).append(boldEnd).append(labelLinkedNotBold).append("</a>");
				result.append("<br>");
			}
		}
		return result.toString();
	}//end method



	public static void closeTreeNodes( int[][] tree, int x, int y, int d_size )
    {
        if ( y > 1 && x < (d_size - 1) )
        {
            int i;
            boolean closed;
            while ( x < (d_size - 1) )
            {
                i = y - 1;
                closed = false;
                while ( i > 0 )
                {
                    if ( tree[x][i] == T_TREE_T_IMAGE )
                    {
                        if ( tree[x+1][i] == T_OPENED_NODE )
                        {
                            if ( ! closed )
                            {
                                tree[x][i] = T_TREE_L_IMAGE;
                                closed = true;
                            }
                            else
                            {
                                tree[x][i] = T_TREE_T_IMAGE;
                            }
                        }
                        else if ( tree[x+1][i] == T_TREE_SPACE_IMAGE )
                        {
                            tree[x][i] = T_TREE_I_IMAGE;
                        }
                        else
                        {
                            if ( !closed )
                            {
                                tree[x][i] = T_TREE_SPACE_IMAGE;
                            }
                            else
                            {
                                tree[x][i] = T_TREE_I_IMAGE;
                            }
                        }
                    }
                    else
                    {
                        break;
                    }
                    i--;
                }
                x++;
            }
        }
    }//end method

    
	public static void setTreeNode( int[][] tree, int level, int y, int d_size )
    {
        int i = 0;
        while ( i < d_size )
        {
            if ( i < level )
            {
                tree[i][y] = T_TREE_T_IMAGE;
            }
            else if ( i == level )
            {
                tree[i][y] = T_OPENED_NODE;
            }
            else
            {
                tree[i][y] = T_OUT;
            }
            i++;
        }
        closeTreeNodes( tree, level, y, d_size );
    }//end method

    
	public static void collapseTree( int[][] tree, int x, int y, int d_size )
    {
        boolean bottom = false;
        if ( x >= d_size )
        {
            x = d_size - 1;
        }
        if ( y >= d_size )
        {
            y = d_size - 1;
            bottom = true;
        }

        int i = x, j = y;
        boolean state = true;

        if ( !bottom )
        {
            while ( j < d_size && i >=0 )
            {
                if ( tree[i][j] == T_OPENED_NODE )
                {
                    tree[i][j++] = T_CLOSED_NODE;
                }
                else if ( tree[i][j] != T_OUT )
                {
                    tree[0][j++] = T_HIDED_NODE;
                }
                else
                {
                    i--;
                }
            }
        }

        i = x;
        j = y - 1;

        while ( j >=0 && i >= 0 )
        {
            if ( tree[i][j] == T_OPENED_NODE )
            {
                if ( state )
                {
                    state = false;
                    j--;
                }
                else
                {
                    tree[i][j--] = T_CLOSED_NODE;
                }
            }
            else if ( tree[i][j] != T_OUT )
            {
                tree[0][j--] = T_HIDED_NODE;
            }
            else
            {
                i--;
                state = true;
            }
        }
    }//end method


	public static String getTreeNodeOffset( int[][] tree, int y, int d_size, String m_context )
    {
        String offset = "";
        int i = 0;
        while ( i < d_size && tree[i][y] != T_OPENED_NODE && tree[i][y] != T_CLOSED_NODE && tree[i][y] != T_OUT )
        {
            switch ( tree[i++][y])
            {
                case T_TREE_SPACE_IMAGE:
                    offset += "<img src=\"icons/1px.gif\" width=16 height=16 border=0 align=\"absmiddle\">";
                    break;
                case T_TREE_T_IMAGE:
                    offset += "<img src=\""+ m_context+ "/util/icons/minusTreeT.gif\" border=0 align=\"absmiddle\">";
                    break;
                case T_TREE_L_IMAGE:
                    offset += "<img src=\""+ m_context+ "/util/icons/minusTreeL.gif\" border=0 align=\"absmiddle\">";
                    break;
                case T_TREE_I_IMAGE:
                    offset += "<img src=\""+ m_context+ "/util/icons/minusTreeI.gif\" border=0 align=\"absmiddle\">";
                    break;
            }
        }
        return offset;
    }//end method

	public static String getTabSpaces(int deep)
	{
		StringBuffer spacesSpaces = new StringBuffer();
		int i;

		for (i = 0; i < deep - 1; i++)
		{
			spacesSpaces.append("&nbsp&nbsp");
		}
		return spacesSpaces.toString();
	}//end method

	 /**
	 * Return 23 for parameter kmelia23
	 */
	 public static String getDriverComponentId(String sClientComponentId)
	{
		String sTableClientId = "";

			// Remove the component name to get the table client id
			char[] cBuf = sClientComponentId.toCharArray();
			for(int nI = 0; nI < cBuf.length && sTableClientId.length() == 0; nI++)
				if(cBuf[nI] == '0' || cBuf[nI] == '1' || cBuf[nI] == '2' || cBuf[nI] == '3' || cBuf[nI] == '4' || cBuf[nI] == '5'||
				   cBuf[nI] == '6' || cBuf[nI] == '7' || cBuf[nI] == '8' || cBuf[nI] == '9')
					sTableClientId = sClientComponentId.substring(nI);

			return sTableClientId;
	}


};