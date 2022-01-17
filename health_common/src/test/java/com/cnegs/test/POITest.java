package com.cnegs.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author Cnegs
 * @Date 2022/1/12 21:22
 */
public class POITest {

    @Test
    public void test() throws IOException {
        //创建工作薄
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook("G:\\healthproject\\资料-健康项目\\day05\\资源\\预约设置模板文件\\hello.xlsx");
        //获取工作表，可以按工作表的顺序获取，也可以按工作表名称获取
        XSSFSheet sheetAt = xssfWorkbook.getSheetAt(0);
        //遍历工作表获得行对象
        for (Row row : sheetAt) {
            //获取单元格中的值
            for (Cell cell : row) {
                cell.setCellType(Cell.CELL_TYPE_STRING);
                String stringCellValue = cell.getStringCellValue();
                System.out.println(stringCellValue);
            }
        }
        xssfWorkbook.close();
    }


    @Test
    public void test01() throws IOException {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook("G:\\healthproject\\资料-健康项目\\day05\\资源\\预约设置模板文件\\ordersetting_template.xlsx");
        //获取工作表
        XSSFSheet sheetAt = xssfWorkbook.getSheetAt(0);
        //获取工作表的最后一行 从0开始是第一行
        int lastRowNum = sheetAt.getLastRowNum();
        for (int i = 0; i <= lastRowNum; i++) {
            //根据行号获取行对象
            XSSFRow row = sheetAt.getRow(i);
            short lastCellNum = row.getLastCellNum();
            for (int j = 0; j < lastCellNum; j++) {
                String cell = row.getCell(j).getStringCellValue();
                System.out.println(cell);
            }
        }
        xssfWorkbook.close();
    }

    @Test
    public void test02() throws IOException {
        //在内存创建一个excel工作薄
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        //创建一个工作表对象
        XSSFSheet cnegs = xssfWorkbook.createSheet("cnegs");
        XSSFRow row = cnegs.createRow(0);
        //创建第一个单元格
        row.createCell(0).setCellValue("姓名");
        row.createCell(1).setCellValue("年龄");
        row.createCell(2).setCellValue("性别");

        XSSFRow row1 = cnegs.createRow(1);
        row1.createCell(0).setCellValue("小明");
        row1.createCell(1).setCellValue("23");
        row1.createCell(2).setCellValue("男");

        XSSFRow row2 = cnegs.createRow(2);
        row2.createCell(0).setCellValue("小红");
        row2.createCell(1).setCellValue("23");
        row2.createCell(2).setCellValue("女");

        FileOutputStream outputStream = new FileOutputStream(new File("E:\\hello.xlsx"));
        xssfWorkbook.write(outputStream);
        outputStream.flush();
        outputStream.close();
        xssfWorkbook.close();
    }

    @Test
    public void test03(){
        Date date = new Date();
        System.out.println(date);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd");
        String format = simpleDateFormat.format(date);
        System.out.println(format);

    }
}
