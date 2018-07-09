package org.redcenter.excel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.redcenter.excel.api.IWriter;
import org.redcenter.excel.filter.ExcelVo;

/**
 * Export excel by POI  
 * Use SXSSFWorkbook to write file by streaming for mass data  
 * @author boneman
 *
 * @param <T>
 */
public class ExcelWriter<T> extends AbstractWriter<T> implements IWriter<T>
{    
    public static final int MAX_ROW_IN_MEM = 100;
    protected SXSSFWorkbook wb;
    private boolean isExported = false;
 
    public ExcelWriter(File file) throws FileNotFoundException 
    {
        super(file);
        wb = new SXSSFWorkbook(MAX_ROW_IN_MEM);
    }
    
    /**
     * 
     * SXSSFWorkbook.write() fails when called more than once
     * The Excel file formats (.xls and .xlsx) are not appendable file formats. 
     * 
     * http://stackoverflow.com/questions/31993679/
     * https://bz.apache.org/bugzilla/show_bug.cgi?id=53515
     */
    @Override
    protected void write(List<T> records, boolean includeHeader, ExcelVo vo)
            throws IOException, IllegalArgumentException, IllegalAccessException
    {
        if (isExported)
        {
            throw new IOException("export() can only be called onece.");
        }
        
        // create sheet 
        String sheetName = vo.getSheetName();
        if (sheetName == null || sheetName.isEmpty())
        {
            Class<?> clazz = records.get(0).getClass();
            sheetName = clazz.getSimpleName();
        }        
        Sheet sheet = wb.getSheet(sheetName);
        if (sheet == null)
        {
            sheet = wb.createSheet(sheetName);
        }

        // create header with bold font
        if (includeHeader)
        {
            createHeader(sheet);
        }

        // create values
        createValue(records, sheet);

        // write to file
//        wb.write(os);
        isExported = true;
    }

    private void createHeader(Sheet sheet)
    {
        // set format 
        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setBold(true);
        style.setFont(font);

        Row row = sheet.createRow(line++);
        int colIndex = 0;
        for (Entry<String, Field> entry : map.entrySet())
        {
            Cell cell = row.createCell(colIndex++);
            cell.setCellValue(entry.getKey());
            cell.setCellStyle(style);
        }
    }

    private void createValue(List<?> records, Sheet sheet) throws IllegalAccessException
    {
        for (Object record : records)
        {
            Row row = sheet.createRow(line++);
            int colIndex = 0;
            for (Entry<String, Field> entry : map.entrySet())
            {
                Cell cell = row.createCell(colIndex++);
                Field field = entry.getValue();                
                Object value = field.get(record);
                if (value == null)
                {                    
                    // avoid NullPointerException for value.toString()
                    value = "";
                }
                cell.setCellValue(value.toString());
            }
        }
    }

    public void close() throws IOException
    {
        wb.write(os);
        super.close(); // close stream
//        wb.close();
        wb.dispose();   // include wb.close() and delete temp file
    }
}
