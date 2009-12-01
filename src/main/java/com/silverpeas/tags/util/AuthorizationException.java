/*
 * Created on 7 juin 2005
 *
 */
package com.silverpeas.tags.util;

/**
 * @author neysseri
 *
 */
public class AuthorizationException extends RuntimeException {

	public AuthorizationException()
	{
		super();
	}
	
	public AuthorizationException(String message)
	{
		super(message);
	}
}
