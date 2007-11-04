/**
 * $Id$
 * $Name:  $
 *
 * Copyright (c) 2006  University of Massachusetts Boston
 *
 * Authors: Jacob K Asiedu
 *
 * This file is part of the UMB Electronic Field Guide.
 * UMB Electronic Field Guide is free software; you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2, or
 * (at your option) any later version.
 *
 * UMB Electronic Field Guide is distributed in the hope that it will be
 * useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with the UMB Electronic Field Guide; see the file COPYING.
 * If not, write to:
 * Free Software Foundation, Inc.
 * 59 Temple Place, Suite 330
 * Boston, MA 02111-1307
 * USA
 */

package project.efg.server.utils;

import project.efg.efgDocument.*;
import project.efg.util.interfaces.EFGDataObject;

public class EFGDataObjectImpl
    implements EFGDataObject
{

    public EFGDataObjectImpl()
    {
        order = -1;
    }

    public void setOrder(int order)
    {
        this.order = order;
    }

    public int getOrder()
    {
        return order;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getLegalName()
    {
        return legalName;
    }

    public void setLegalName(String legalName)
    {
        this.legalName = legalName;
    }

    public boolean equals(EFGDataObject object)
    {
        return getOrder() == object.getOrder();
    }

    public int hashCode()
    {
        return getOrder();
    }

    public ItemsType getStates()
    {
        return statesList;
    }

    public void setStates(ItemsType list)
    {
        if(list == null);
        statesList = list;
    }

    public StatisticalMeasuresType getStatisticalMeasures()
    {
        return statsList;
    }

    public void setStatisticalMeasures(StatisticalMeasuresType list)
    {
        statsList = list;
    }

    public EFGListsType getEFGLists()
    {
        return efgLists;
    }

    public void setEFGListsType(EFGListsType lists)
    {
        efgLists = lists;
    }

    public MediaResourcesType getMediaResources()
    {
        return media;
    }

    public void setMediaResources(MediaResourcesType mediaResources)
    {
        media = mediaResources;
    }

    private ItemsType statesList;
    private StatisticalMeasuresType statsList;
    private MediaResourcesType media;
    private EFGListsType efgLists;
    private String name;
    private String legalName;
    private int order;
}
