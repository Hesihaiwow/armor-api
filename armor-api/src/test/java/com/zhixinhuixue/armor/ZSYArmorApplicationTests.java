package com.zhixinhuixue.armor;

import com.zhixinhuixue.armor.exception.ZSYServiceException;
import org.apache.poi.ss.usermodel.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.zhixinhuixue.armor.service.impl.ZSYMantisBugService.getExcelWorkbook;
import static com.zhixinhuixue.armor.service.impl.ZSYMantisBugService.getSheetByNum;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ZSYArmorApplicationTests {

	@Test
	public void contextLoads() {
		System.out.println(UUID.randomUUID().toString().replaceAll("-",""));
	}

	@Test
	public void test() throws ParseException {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		String weekFirstDayStr = DateHelper.getThisWeekFirstDay();
//		System.out.println("weekFirstDayStr = " + weekFirstDayStr);
//
//		double x = 89.0;
//		double y = x/30;
//		System.out.println("y = " + y);
//		int xx = (int)Math.floor(y);
//		System.out.println("xx = " + xx);
//		double z = x%30;
//		System.out.println("z = " + z);
//
//		long time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2019-09-27 23:00:00").getTime();
//		long time1 = new Date().getTime();
//		long cha = (time-time1)/1000/3600/24;
//		BigDecimal cha2 = BigDecimal.valueOf(time-time1).divide(BigDecimal.valueOf(86400000),2,BigDecimal.ROUND_HALF_UP);
//		System.out.println("cha = " + cha);
//
//		if (cha2.compareTo(BigDecimal.ZERO)>=0 && cha2.compareTo(BigDecimal.valueOf(3))<0){
//			System.out.println("cha2 = " + cha2);
//		}
		Date createTime = dateFormat.parse("2019-09-25 00:00:00");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(createTime);
		int createTimeYear = calendar.get(Calendar.YEAR);
		System.out.println("createTimeYear = " + createTimeYear);
		int createTimeMonth = calendar.get(Calendar.MONTH) + 1;
		System.out.println("createTimeMonth = " + createTimeMonth);
		int createTimeDay = calendar.get(Calendar.DAY_OF_MONTH);
		System.out.println("createTimeDay = " + createTimeDay);
		int i = calendar.get(Calendar.DAY_OF_WEEK);
		System.out.println("i = " + i);


		Date beginTime = dateFormat.parse("2019-09-24 00:00:00");
		calendar.setTime(beginTime);
		int beginTimeYear = calendar.get(Calendar.YEAR);
		int beginTimeMonth = calendar.get(Calendar.MONTH) + 1;
		int beginTimeDay = calendar.get(Calendar.DAY_OF_MONTH);
		LocalDate end = LocalDate.of(beginTimeYear, beginTimeMonth, beginTimeDay);
		LocalDate start = LocalDate.of(createTimeYear, createTimeMonth, createTimeDay);
		System.out.println("相差天数"+(end.toEpochDay() - start.toEpochDay() + 1));
	}

	@Test
	public void read(){
		File file = new File("D:\\test.xlsx");
		String suffix = "." + getUploadSuffix(file.getName());
		if (!isExcel(suffix)){
			throw new ZSYServiceException("只能上传Excel");
		}
		Workbook book = null;
		try {
			String file_dir = file.getAbsolutePath();
			book = getExcelWorkbook(file_dir);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Sheet sheet = getSheetByNum(book,0);
		int lastRowNum = sheet.getLastRowNum();


		List<String> fields = new ArrayList<>();
		Row row = null;
		row = sheet.getRow(0);
		if( row != null ){
			int lastCellNum = row.getLastCellNum();
			Cell cell = null;
			for( int j = 0 ; j <= lastCellNum ; j++ ){
				cell = row.getCell(j);
				if( cell != null ){
//						cell.setCellType(CellType.STRING);
					String cellValue = cell.getStringCellValue();
					fields.add(cellValue);
				}
			}
		}
		String dateStr = fields.get(0);
		String dateStr1 = dateStr.substring(11, 21);
		String dateStr2 = dateStr.substring(24);
		System.out.println("dateStr1 = " + dateStr1);
		System.out.println("dateStr2 = " + dateStr2);

		List<List<String>> userCheckList = new ArrayList<>();
		for(int i = 3 ; i <= lastRowNum ; i++){
			List<String> checkTimeList = new ArrayList<>();
			Row row2 = null;
			row2 = sheet.getRow(i);
			if( row2 != null ){
				int lastCellNum = row2.getLastCellNum();
				Cell cell = null;
				for( int j = 0 ; j <= lastCellNum ; j++ ){
					cell = row2.getCell(j);
					if( cell != null ){
//						cell.setCellType(CellType.STRING);
						String cellValue = cell.getStringCellValue();
						checkTimeList.add(cellValue);
					}
				}
			}
			userCheckList.add(checkTimeList);
//			fields.forEach(s -> System.out.println("s = " + s));
		}

		for (List<String> userCheck : userCheckList) {
			String[] split = userCheck.get(12).split("  \n");
			System.out.println("user = " + userCheck.get(0)+" "+userCheck.get(2)+" "
					+split[0].trim()+" "+split[1].trim()
			+" "+userCheck.get(12).length());
//			System.out.println("user = " + userCheck.get(0)+" "+userCheck.get(2)+" "
//					+userCheck.get(12).substring(0,5)+" "+userCheck.get(12).substring(8,13)
//			+" "+userCheck.get(12).length());
		}
	}

	/**
	 * 获取上传文件后缀名
	 *
	 * @param uploadName
	 * @return
	 */
	public String getUploadSuffix(String uploadName) {
		return uploadName.substring(uploadName.lastIndexOf(".") + 1);
	}

	//判断是否是excel
	public static boolean isExcel(String url){
		Pattern p=Pattern.compile("\\.(xls|XLS)");
		Matcher m=p.matcher(url);
		if(m.find()){
			return true;
		}
		return false;
	}
}
