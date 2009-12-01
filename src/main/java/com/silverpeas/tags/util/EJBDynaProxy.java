package com.silverpeas.tags.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.ejb.EJBHome;
import javax.rmi.PortableRemoteObject;
import java.rmi.NoSuchObjectException;

import com.stratelia.silverpeas.silvertrace.SilverTrace;
import com.stratelia.webactiv.util.EJBUtilitaire;

import org.omg.CORBA.OBJECT_NOT_EXIST;

public class EJBDynaProxy {

	public static Object createProxy(String jndiName, Class componentInterface) throws Exception
	{
		InvocationHandler handler = new InvocationInterceptor(jndiName);
		return Proxy.newProxyInstance(EJBDynaProxy.class.getClassLoader(), new Class[]{componentInterface}, handler);
	}

	private static class InvocationInterceptor implements InvocationHandler
	{
		private static final Object[] NO_ARGS = new Object[0];
		private static final Class[]  NO_ARGS_SIGNATURE = new Class[0];

		private Object target;
		private String jndiName;

		private int errors = 0;

		InvocationInterceptor(String jndiName) throws Exception{
			this.jndiName = jndiName;
			this.target = createTarget();
		}

		public synchronized Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
			try {
				return method.invoke(target, args);
			}
			catch (InvocationTargetException e) {
				SilverTrace.error("util","EJBDynaProxy.invoke()",
						"root.MSG_GEN_PARAM_VALUE",
						"Exception sur l'invocation de " + target + "." + method.getName(), e);
				Throwable te = e.getTargetException();
				if ( (te instanceof OBJECT_NOT_EXIST) || (te instanceof NoSuchObjectException) ){
					if (errors++ == 0){
						try {
							this.target = createTarget();
							Object result = invoke(proxy, method, args);

							SilverTrace.info("util","EJBDynaProxy.invoke()",
									"root.MSG_GEN_PARAM_VALUE",
									"Recovery réussie sur l'invocation de " + target + " " + method.getName());
							return result;
						}
						finally {
							errors = 0;
						}
					}
					else {
						SilverTrace.error("util","EJBDynaProxy.invoke()",
								"root.MSG_GEN_PARAM_VALUE",
								"Recovery échouée sur l'invocation de " + target + " " + method.getName());
						throw new RuntimeException("OBJECT_NOT_EXIST levé 2 fois. Le protocole de recovery a échoué.");
					}
				}
				else {
					throw te;
				}
			}
		}

		private Object createTarget() throws Exception {
			SilverTrace.info("util","EJBDynaProxy.invoke()",
					"root.MSG_GEN_PARAM_VALUE",
					"Création de la cible pour " + jndiName);
			EJBHome home = (EJBHome) EJBUtilitaire.getEJBObjectRef(jndiName, EJBHome.class);
			Class actualHomeClass = home.getEJBMetaData().getHomeInterfaceClass();
			Object narrowedHome = PortableRemoteObject.narrow(home, actualHomeClass);
			Object result = actualHomeClass.getMethod("create", NO_ARGS_SIGNATURE).invoke(narrowedHome,NO_ARGS);
			SilverTrace.info("util","EJBDynaProxy.invoke()",
					"root.MSG_GEN_PARAM_VALUE",
					"Création de la cible pour " + jndiName);
			return result;
		}

	}
}