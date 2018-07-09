package org.redcenter.excel;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map.Entry;

import org.redcenter.excel.api.IWriter;
import org.redcenter.excel.filter.ExcelVo;

/**
 * 
 * @author boneman
 *
 * @param <T>
 */
public class CsvWriter<T> extends AbstractWriter<T> implements IWriter<T>
{
    public static final int MAX_ROW_IN_MEM = 100;
    public static final String NEW_LINE = "\r\n";
    public static final String SEPARATOR = ",";    
    private BufferedOutputStream bos;

    public CsvWriter(File file) throws FileNotFoundException
    {
        super(file);
        bos = new BufferedOutputStream(os, MAX_ROW_IN_MEM * 1024);
    }

    @Override
    protected void write(List<T> records, boolean includeHeader, ExcelVo vo)
            throws IOException, IllegalAccessException
    {
        StringBuilder sb = new StringBuilder();

        // create header
        if (includeHeader)
        {
            createHeader(sb);
        }

        // create values
        createValue(records, sb);

        // write to file
        bos.write(sb.toString().getBytes());
        bos.flush();
    }

    private void createHeader(StringBuilder sb)
    {
        for (Entry<String, Field> entry : map.entrySet())
        {
            sb.append(entry.getKey() + SEPARATOR);
        }
        sb.append(NEW_LINE);
        line++;
    }

    private void createValue(List<T> records, StringBuilder sb) throws IllegalAccessException
    {
        for (Object record : records)
        {
            for (Entry<String, Field> entry : map.entrySet())
            {
                Field field = entry.getValue();
                Object value = field.get(record);
                if (value == null)
                {
                    // avoid NullPointerException for value.toString()
                    value = "";
                }
                sb.append(value + SEPARATOR);
            }
            sb.append(NEW_LINE);
            line++;
        }
    }
    
    public void close() throws IOException
    {
        bos.close();
        super.close();
    }
}
