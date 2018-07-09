package org.redcenter.excel.api;

import org.redcenter.excel.filter.ExcelVo;

public interface IFieldFilter
{
    public ExcelVo getExcelVo(Class<?> clazz);
}
