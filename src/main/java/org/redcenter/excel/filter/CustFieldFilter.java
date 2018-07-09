package org.redcenter.excel.filter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import org.redcenter.excel.annotation.ExcelColumn;
import org.redcenter.excel.annotation.ExcelSheet;
import org.redcenter.excel.api.IFieldFilter;

public class CustFieldFilter implements IFieldFilter
{
    /**
     * 
     * @param clazz
     * @return null if not annotated
     */
    public ExcelVo getExcelVo(Class<?> clazz)
    {
        ExcelVo vo = new ExcelVo();

        // setup sheet name
        String sheetName = clazz.getSimpleName();
        ExcelSheet classAnnotation = clazz.getAnnotation(ExcelSheet.class);
        if (classAnnotation == null)
        {
            // skip non JPA classes
            return null;
        }
        if (classAnnotation.name() != null && !classAnnotation.name().isEmpty())
        {
            sheetName = classAnnotation.name();
        }
        vo.setSheetName(sheetName);

        // setup columns
        setupColumns(clazz, vo);

        return vo;
    }

    private void setupColumns(Class<?> clazz, ExcelVo vo)
    {
        List<ExcelColumnVo> columnVoList = new ArrayList<ExcelColumnVo>();
        Class<?> superClass = clazz;
        do
        {
            for (Field field : superClass.getDeclaredFields())
            {   
                addExcelColumnVo(columnVoList, field);
            }

            // traverse class hierarchy
            superClass = superClass.getSuperclass();
        }
        while (superClass != null);

        // sort by column order and add to LinkedHashMap
        LinkedHashMap<String, Field> map = new LinkedHashMap<String, Field>();
        Collections.sort(columnVoList, new Comparator<ExcelColumnVo>() {
            public int compare(ExcelColumnVo o1, ExcelColumnVo o2)
            {
                return o1.getOrder() - o2.getOrder();
            }
        });
        for (ExcelColumnVo excelColumnVo : columnVoList)
        {
            map.put(excelColumnVo.getName(), excelColumnVo.getFiedl());
        }
        
        vo.setColumnMap(map);
    }

    private void addExcelColumnVo(List<ExcelColumnVo> columnVoList, Field field)
    {
        ExcelColumn columnAnnotation = field.getAnnotation(ExcelColumn.class);
        if (columnAnnotation != null)
        {
            // get name by annotation
            String name = columnAnnotation.name();
            if (name == null || name.isEmpty())
            {
                name = field.getName();
                name = name.substring(0, 1).toUpperCase() + name.substring(1);
            }
            else
            {
//                name = name.toUpperCase();
            }

            // get order by annotation
            int order = columnAnnotation.order();

            // add to list
            ExcelColumnVo columnVo = new ExcelColumnVo();
            columnVo.setName(name);
            columnVo.setOrder(order);
            field.setAccessible(true);
            columnVo.setFiedl(field);
            columnVoList.add(columnVo);
        }
    }
}
