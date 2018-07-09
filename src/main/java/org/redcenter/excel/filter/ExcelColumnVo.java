package org.redcenter.excel.filter;

import java.lang.reflect.Field;

public class ExcelColumnVo
{
    private Field fiedl;
    private String name;
    private int order;

    public Field getFiedl()
    {
        return fiedl;
    }

    public void setFiedl(Field fiedl)
    {
        this.fiedl = fiedl;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getOrder()
    {
        return order;
    }

    public void setOrder(int order)
    {
        this.order = order;
    }

}
