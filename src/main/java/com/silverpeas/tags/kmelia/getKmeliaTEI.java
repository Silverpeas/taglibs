package com.silverpeas.tags.kmelia;

import javax.servlet.jsp.tagext.*;

public class getKmeliaTEI extends TagExtraInfo {

	public getKmeliaTEI()
	{
		super();
	}

	public VariableInfo[] getVariableInfo(TagData data)
	{
		return new VariableInfo[]
		{
			new VariableInfo(
				data.getAttributeString("name"),
				"com.silverpeas.tags.kmelia.KmeliaTagUtil",
				true,
				VariableInfo.NESTED
			),
		};
	}
}