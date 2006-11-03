/*
* NOTE: This copyright does *not* cover user programs that use HQ
* program services by normal system calls through the application
* program interfaces provided as part of the Hyperic Plug-in Development
* Kit or the Hyperic Client Development Kit - this is merely considered
* normal use of the program, and does *not* fall under the heading of
* "derived work".
*
* Copyright (C) [2004, 2005, 2006], Hyperic, Inc.
* This file is part of HQ.
*
* HQ is free software; you can redistribute it and/or modify
* it under the terms version 2 of the GNU General Public License as
* published by the Free Software Foundation. This program is distributed
* in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
* even the implied warranty of MERCHANTABILITY or FITNESS FOR A
* PARTICULAR PURPOSE. See the GNU General Public License for more
* details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
* USA.
*/

package org.hyperic.hq.product;

import org.hyperic.hibernate.PersistedObject;
import org.hyperic.hq.product.shared.PluginValue;

/**
 * Plugin generated by hbm2java
 */
public class Plugin extends PersistedObject
{

    // Fields    

    private String name;
    private String path;
    private String md5;
    private long creationTime;

    // Constructors

    /**
     * default constructor
     */
    public Plugin()
    {
        super();
    }

    public Plugin(PluginValue pv)
    {
        super();
        setPluginValue(pv);
    }

    // Property accessors
    public String getName()
    {
        return this.name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getPath()
    {
        return this.path;
    }

    public void setPath(String path)
    {
        this.path = path;
    }

    public String getMD5()
    {
        return this.md5;
    }

    public void setMD5(String md5)
    {
        this.md5 = md5;
    }

    public long getCreationTime()
    {
        return this.creationTime;
    }

    public void setCreationTime(long ctime)
    {
        this.creationTime = ctime;
    }

    /**
     * @deprecated use getCreationTime()
     */
    public long getCtime()
    {
        return getCreationTime();
    }

    /**
     * @deprecated use setCreationTime()
     * @param cTime
     */
    public void setCtime(long cTime)
    {
        setCreationTime(cTime);
    }

    private PluginValue pluginValue = new PluginValue();
    /**
     * @deprecated use (this) Plugin object
     * @return
     */
    public PluginValue getPluginValue()
    {
        pluginValue.setId(getId());
        pluginValue.setName(
            (getName() == null) ? "" : getName());
        pluginValue.setPath(
            (getPath() == null) ? "" : getPath());
        pluginValue.setMD5(
            (getMD5() == null) ? "" : getMD5());
        pluginValue.setCtime(getCtime());
        return pluginValue;
    }

    public void setPluginValue(PluginValue valueHolder)
    {
        setName( valueHolder.getName() );
        setPath( valueHolder.getPath() );
        setMD5( valueHolder.getMD5() );
        setCtime( valueHolder.getCtime() );
    }

    public boolean equals(Object obj)
    {
        if (!(obj instanceof Plugin) || !super.equals(obj)) {
            return false;
        }
        Plugin o = (Plugin) obj;
        return
               ((name == o.getName()) ||
                (name != null && o.getName() != null &&
                 name.equals(o.getName())));
    }

    public int hashCode()
    {
        int result = super.hashCode();

        result = 37*result + (name != null ? name.hashCode() : 0);

        return result;
    }
}


