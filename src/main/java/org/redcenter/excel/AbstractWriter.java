package org.redcenter.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;

import org.redcenter.excel.api.IWriter;
import org.redcenter.excel.api.IFieldFilter;
import org.redcenter.excel.filter.CustFieldFilter;
import org.redcenter.excel.filter.ExcelVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractWriter<T> implements IWriter<T>
{
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected OutputStream os;
    protected int line = 0;
    protected LinkedHashMap<String, Field> map = null;

    public AbstractWriter(File file) throws FileNotFoundException
    {
        os = new FileOutputStream(file);
    }

    public void write(List<T> records, boolean includeHeader)
            throws IOException, IllegalArgumentException, IllegalAccessException
    {
        // filter by customized annotation by default
        write(records, includeHeader, new CustFieldFilter());
        // export(records, includeHeader, new JpaFieldFilter());
    }

    public void write(List<T> records, boolean includeHeader, IFieldFilter filter)
            throws IOException, IllegalArgumentException, IllegalAccessException
    {
        if (records == null || records.size() == 0)
        {
            return;
        }
        if (filter == null)
        {
            filter = new CustFieldFilter();
        }

        // prepare columns by fields
        Class<?> clazz = records.get(0).getClass();
        // map = filter.getFieldMap(clazz);
        ExcelVo vo = filter.getExcelVo(clazz);
        if (vo == null)
        {
            // write empty excel file
            vo = new ExcelVo();
            vo.setColumnMap(new LinkedHashMap<String, Field>());
        }
        map = vo.getColumnMap();

        // start to process
        write(records, includeHeader, vo);
    }

    /**
     * 
     * @param records
     * @param includeHeader
     * @param vo
     * @throws IOException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    protected abstract void write(List<T> records, boolean includeHeader, ExcelVo vo)
            throws IOException, IllegalArgumentException, IllegalAccessException;

    public void close() throws IOException
    {
        os.close();
    }
}