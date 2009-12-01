package com.silverpeas.tags;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

public class HomeTEI extends TagExtraInfo {

	public VariableInfo[] getVariableInfo(TagData data)
	{
		return new VariableInfo[]
		{
			new VariableInfo(
				data.getId(),
				data.getAttributeString("type"),
				true,
				VariableInfo.AT_BEGIN
			),
		};
	}
}