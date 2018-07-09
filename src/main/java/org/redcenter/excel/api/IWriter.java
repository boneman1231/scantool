package org.redcenter.excel.api;

import java.io.IOException;
import java.util.List;

public interface IWriter<T>
{
    /**
     * 
     * @param records
     * @param includeHeader
     * @throws IOException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public void write(List<T> records, boolean includeHeader)
            throws IOException, IllegalArgumentException, IllegalAccessException;

    /**
     * 
     * @param records
     * @param includeHeader
     * @param filter
     * @throws IOException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public void write(List<T> records, boolean includeHeader, IFieldFilter filter)
            throws IOException, IllegalArgumentException, IllegalAccessException;
    
    /**
     * 
     * @throws IOException
     */
    public void close() throws IOException;
}
