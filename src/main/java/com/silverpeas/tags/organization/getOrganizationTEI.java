package com.silverpeas.tags.organization;

import javax.servlet.jsp.tagext.*;

public class getOrganizationTEI extends TagExtraInfo {

	public getOrganizationTEI()
	{
		super();
	}

	public VariableInfo[] getVariableInfo(TagData data)
	{
		return new VariableInfo[]
		{
			new VariableInfo(
				data.getAttributeString("name"),
				"com.silverpeas.tags.organization.OrganizationTagUtil",
				true,
				VariableInfo.NESTED
			),
		};
	}
}