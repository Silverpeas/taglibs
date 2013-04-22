/**
 * Copyright (C) 2000 - 2012 Silverpeas
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * As a special exception to the terms and conditions of version 3.0 of
 * the GPL, you may redistribute this Program in connection with Free/Libre
 * Open Source Software ("FLOSS") applications as described in Silverpeas's
 * FLOSS exception.  You should have received a copy of the text describing
 * the FLOSS exception, and it is also available here:
 * "http://www.silverpeas.org/legal/licensing"
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.silverpeas.tags;

import java.util.Hashtable;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.IndexedPropertyDescriptor;
import java.beans.IntrospectionException;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public class BeanUtil {

  public static final Object[] sNoParams = new Object[0];
  public static Hashtable sGetPropToMethod = new Hashtable(100);
  public static Hashtable sSetPropToMethod = new Hashtable(100);

  public static Object getObjectPropertyValue(Object obj, String propName, Object index)
      throws InvocationTargetException, IllegalAccessException, IntrospectionException,
      NoSuchMethodException {
    Method m = getGetPropertyMethod(obj, propName, null == index ? null : index.getClass());
    if (null == index) {
      return m.invoke(obj, sNoParams);
    } else {
      Object[] params = new Object[1];
      params[0] = index;
      return m.invoke(obj, params);
    }
  }

  public static Method getGetPropertyMethod(Object obj, String propName, Class paramClass)
      throws IntrospectionException, NoSuchMethodException {
    Class oClass = obj.getClass();
    MethodKey key = new MethodKey(propName, oClass, paramClass);
    Method rc = (Method) sGetPropToMethod.get(key);
    if (rc != null) {
      return rc;
    }

    BeanInfo info = Introspector.getBeanInfo(oClass);
    PropertyDescriptor[] pd = info.getPropertyDescriptors();

    if (null != pd) {
      for (int i = 0; i < pd.length; i++) {
        if (pd[i] instanceof IndexedPropertyDescriptor) {
          if (null == paramClass || !propName.equals(pd[i].getName())) {
            continue;
          }
          IndexedPropertyDescriptor ipd = (IndexedPropertyDescriptor) pd[i];
          Method m = ipd.getIndexedReadMethod();
          if (null == m) {
            continue;
          }
          Class[] params = m.getParameterTypes();
          if ((1 == params.length) && params[0].equals(paramClass)) {
            rc = m;
            break;
          }
        } else {
          if (null != paramClass || !propName.equals(pd[i].getName())) {
            continue;
          }
          rc = pd[i].getReadMethod();
          break;
        }
      }
    }

    /*
     * Looking into the propety descriptor failed. It can be becuase that there are two properties
     * with the same name and different indices (for example the "string" property in a ResultSet)
     * Look into the methods.
     */
    if (null == rc) {
      StringBuffer methodName = new StringBuffer();
      methodName.append("get");
      methodName.append(propName.substring(0, 1).toUpperCase());
      methodName.append(propName.substring(1));

      if (null == paramClass) {
        rc = oClass.getMethod(methodName.toString(), new Class[0]);
      } else {
        rc = oClass.getMethod(methodName.toString(), new Class[] { paramClass });
      }
    }

    if (null == rc) {
      throw new NoSuchMethodException("NO_GET_PROPERTY_METHOD");
    }
    sGetPropToMethod.put(key, rc);
    return rc;
  }

  /*
   * We are not dealing with indexed properties.
   */
  public static Method getSetPropertyMethod(Object obj, String propName)
      throws IntrospectionException, NoSuchMethodException {
    Class oClass = obj.getClass();
    MethodKey key = new MethodKey(propName, oClass, null);
    Method rc = (Method) sSetPropToMethod.get(key);
    if (rc != null) {
      return rc;
    }

    BeanInfo info = Introspector.getBeanInfo(oClass);
    PropertyDescriptor[] pd = info.getPropertyDescriptors();

    if (null != pd) {
      for (int i = 0; i < pd.length; i++) {
        if (propName.equals(pd[i].getName())) {
          if (!(pd[i] instanceof IndexedPropertyDescriptor)) {
            Method m = pd[i].getWriteMethod();
            if (null == m) {
              continue;
            }
            Class[] params = m.getParameterTypes();
            if (1 == params.length) {
              rc = m;
              break;
            }
          }
        }
      }
    }

    if (null == rc) {
      throw new NoSuchMethodException("NO_SET_PROPERTY_METHOD");
    }
    sSetPropToMethod.put(key, rc);
    return rc;
  }
}