package com.space.utils.excel;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Excel导入及导出
 * @author zhuzhe
 * @date 2018/2/5 10:52
 */
public class ExcelUtils {

    /**
     * 获取一个Workbook
     * @param fileName
     * @param is
     * @return
     */
    public static Workbook getWorkbook(String fileName, InputStream is)
            throws IOException {
        Workbook workbook = null;
        if (fileName.endsWith("xls")) {
            workbook = new HSSFWorkbook(is);
        } else if (fileName.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(is);
        }
        return workbook;
    }

    /**
     * 取单元格的值
     * @return
     */
    @SuppressWarnings("deprecation")
    public static String getCellValue(Cell cell, boolean treatAsStr) {
        DecimalFormat df = new DecimalFormat("#");
        DecimalFormat df2 = new DecimalFormat("######0.00");

        if (cell == null ) {
            return "";
        }
        if (treatAsStr) {
            // 虽然excel中设置的都是文本，但是数字文本还被读错，如“1”取成“1.0”
            // 加上下面这句，临时把它当做文本来读取
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }
        if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
            if(HSSFDateUtil.isCellDateFormatted(cell)){
                try{
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    return sdf.format(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()));
                }catch (Exception e){
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    return String.valueOf(cell.getStringCellValue()).trim();
                }
            }
            String result = String.valueOf(cell.getNumericCellValue());
            if(null != result && result.length() > 0){
                if(".0".equals(result.substring(result.length()-2, result.length()))){
                    return df.format(cell.getNumericCellValue());
                }
            }
            return df2.format(cell.getNumericCellValue());
        }
        if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        }
        else {
            return String.valueOf(cell.getStringCellValue()).trim();
        }
    }

    /**
     * 导出
     */
    public static SXSSFWorkbook wb = null;

    public static SXSSFSheet sheet = null;

    public static DataFormat format = null;

    public static SXSSFRow hdRow = null;

    static int listLength = 0;

    /**
     * 设置工作表的格式
     */
    public static void createExcel() {
        wb = new SXSSFWorkbook(1000);
    }

    public static SXSSFWorkbook getWb(){
        return wb;
    }

    public static void createSheet(String sheetName) {
        sheet = wb.createSheet(sheetName);
        format = wb.createDataFormat();
        hdRow = sheet.createRow(0);
        sheet.setDefaultRowHeightInPoints(18);
        sheet.setDefaultColumnWidth(18);
    }

    /**
     * 表头数据
     * @throws Exception
     */
    public static void addHeader(List<String> rowvalues, boolean isFilter) throws Exception {
        listLength = rowvalues.size();
        // 表头样式及背景色
        CellStyle hdStyle = wb.createCellStyle();
        hdStyle.setAlignment(HorizontalAlignment.CENTER);

        String[] title = new String[rowvalues.size()];
        for (int i = 0; i < rowvalues.size(); i++) {
            title[i] = (String) rowvalues.get(i);
        }
        SXSSFRow dtRow = sheet.createRow((0));
        if (isFilter == true) {
            for (int i = 0; i < title.length; i++) {
                SXSSFCell cell1 = hdRow.createCell(i);
                XSSFRichTextString value = new XSSFRichTextString(title[i]);
                cell1.setCellValue(value);
                cell1.setCellStyle(hdStyle);
            }
        } else {
            for (int i = 0; i < title.length; i++) {
                SXSSFCell cell2 = dtRow.createCell(i);
                XSSFRichTextString value2 = new XSSFRichTextString(title[i]);
                cell2.setCellValue(value2);
            }
        }
    }

    /**
     * 给指定的行追加一行数据
     * @param lists2
     * @param row
     */
    public static void insertRow(List<Object> lists2, int row) {

        SXSSFRow dtRow = sheet.createRow(row);
        for (int j = 0; j < lists2.size(); j++) {
            Object cell_data = lists2.get(j);
            SXSSFCell cell = dtRow.createCell(j);
            cell.setCellValue(String.valueOf(cell_data));
        }
    }

    /**
     * 具体文件生成的路径
     * @param file
     * @throws Exception
     */
    public static void exportExcel(String file) throws Exception {
        FileOutputStream fileOut = new FileOutputStream(file);
        wb.write(fileOut);
        fileOut.close();
    }

    /**
     * 输出文件到页面
     * @param resp
     * @throws Exception
     */
    public static void export(HttpServletResponse resp) throws Exception {
        OutputStream fileOut = resp.getOutputStream();
        wb.write(fileOut);
        fileOut.close();
    }
}

