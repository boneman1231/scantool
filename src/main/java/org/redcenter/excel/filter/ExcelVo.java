package org.redcenter.excel.filter;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;

public class ExcelVo
{
    private String sheetName;
    private LinkedHashMap<String, Field> columnMap;

    public String getSheetName()
    {
        return sheetName;
    }

    public void setSheetName(String sheetName)
    {
        this.sheetName = sheetName;
    }

    public LinkedHashMap<String, Field> getColumnMap()
    {
        return columnMap;
    }

    public void setColumnMap(LinkedHashMap<String, Field> columnMap)
    {
        this.columnMap = columnMap;
    }
}
