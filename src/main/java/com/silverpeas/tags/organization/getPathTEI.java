package com.silverpeas.tags.organization;

import javax.servlet.jsp.tagext.*;

public class getPathTEI extends TagExtraInfo {

	public getPathTEI()
	{
		super();
	}

	public VariableInfo[] getVariableInfo(TagData data)
	{
		return new VariableInfo[]
		{
			new VariableInfo(
				data.getAttributeString("name"),
				"java.util.Collection",
				true,
				VariableInfo.NESTED
			),
		};
	}
}