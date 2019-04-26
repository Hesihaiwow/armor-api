package com.zhixinhuixue.armor;

import com.zhixinhuixue.armor.helper.DateHelper;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;
import sun.misc.Cleaner;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author SCH
 * @date 2019/2/21 15:36
 */
public class ExcelOperate2 {
    public static void main(String[] args) {
        ExcelOperate2 obj = new ExcelOperate2();
        // 此处为我创建Excel路径：E:/zhanhj/studysrc/jxl下
        File file = new File("E:\\花名册2019.2.21.xls");
        List excelList = obj.readExcel(file);
        System.out.println("list中的数据打印出来");
        for (int i = 0; i < excelList.size(); i++) {
            List list = (List) excelList.get(i);
            /*for (int j = 0; j < list.size(); j++) {
                System.out.print(list.get(j));
            }*/
            System.out.println(list.get(0));
            System.out.println(list.get(1));
            System.out.println();
        }
    }
    // 去读Excel的方法readExcel，该方法的入口参数为一个File对象
    public List readExcel(File file) {
        try {
            // 创建输入流，读取Excel
            InputStream is = new FileInputStream(file.getAbsolutePath());
            // jxl提供的Workbook类
            Workbook wb = Workbook.getWorkbook(is);
            // Excel的页签数量
            int sheet_size = wb.getNumberOfSheets();
            for (int index = 0; index < sheet_size; index++) {
                List<List> outerList=new ArrayList<List>();
                // 每个页签创建一个Sheet对象
                Sheet sheet = wb.getSheet(index);
                // sheet.getRows()返回该页的总行数
                for (int i = 0; i < sheet.getRows(); i++) {
                    List innerList=new ArrayList();
                    // sheet.getColumns()返回该页的总列数
                    for (int j = 0; j < sheet.getColumns(); j++) {
                        String cellinfo = sheet.getCell(j, i).getContents();
                        if(cellinfo.isEmpty()){
                            continue;
                        }
                        innerList.add(cellinfo);
                    }
                    outerList.add(i, innerList);
                }
                return outerList;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test
    public void getDate(){
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE,1);
        int i = calendar.get(Calendar.DAY_OF_WEEK);
        System.out.println(i);
    }

}
