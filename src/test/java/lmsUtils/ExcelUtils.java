package lmsUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {

	public static FileInputStream excelFile;
	public static XSSFWorkbook excelWbook;
	public static XSSFSheet excelWSheet;
	public static XSSFCell cell;
	public static XSSFRow row;

	// This method is to set the File path and to open the Excel file
	// , Pass Excel Path and Sheetname as Arguments to this method

	public static void setexcelFileInfo(String sheetname) throws Exception {

		// Open Excel File
		excelFile = new FileInputStream(LMSAppConstants.excel_Path + LMSAppConstants.excel_FileName);

		// Open Excel Workbook
		excelWbook = new XSSFWorkbook(excelFile);

		// Access the required test data sheet
		excelWSheet = excelWbook.getSheet(sheetname);
		System.out.println(LMSAppConstants.excel_Path + " " + LMSAppConstants.excel_FileName + " " + sheetname);
	}

	public static String[][] getDataFromExcelSheet(String sheetname) throws Exception {
		try {
			setexcelFileInfo(sheetname);

			int rowCount = getRowCount();
			int cellCount = getColumnCount(1);

			String[][] jobData = new String[rowCount][cellCount];
			for (int i = 1; i <= rowCount; i++) {
				for (int j = 0; j < cellCount; j++) {
					jobData[i - 1][j] = getCellData(i, j);
				}
			}
			return jobData;
		} catch (Exception e) {
			return new String[0][0];
		} finally {
			if (excelWbook != null)
				excelWbook.close();
			if (excelFile != null)
				excelFile.close();
		}
	}

	// This method is to read the test data from the Excel cell, in this we are
	// passing parameters as Row num and Col num
	public static int getRowCount() throws Exception {
		try {
			int rowCount = excelWSheet.getLastRowNum();
			return rowCount;
		} catch (Exception e) {
			return 0;
		}
	}

	public static int getColumnCount(int RowNum) throws Exception {
		try {
			// Row = ExcelWSheet.getRow(RowNum).getLastCellNum();
			row = excelWSheet.getRow(RowNum);
			int cellCount = row.getLastCellNum();
			return cellCount;
		} catch (Exception e) {
			return 0;
		}
	}

	public static String getCellData(int RowNum, int ColNum) throws Exception {
		try {
			cell = excelWSheet.getRow(RowNum).getCell(ColNum);

			DataFormatter formatter = new DataFormatter();
			String CellData = formatter.formatCellValue(cell);

			return CellData;
		} catch (Exception e) {
			return "";
		}
	}

	public static String[] getProgramIDFromExcelSheet(String sheetname) throws IOException {
		try {
			setexcelFileInfo(sheetname);

			int rowCount = getRowCount();

			String[] jobData = new String[rowCount];
			for (int i = 1; i <= rowCount; i++) {
				jobData[i - 1] = getCellData(i, 0);
			}

			return jobData;
		} catch (Exception e) {
			return new String[0];
		} finally {
			if (excelWbook != null)
				excelWbook.close();
			if (excelFile != null)
				excelFile.close();
		}

	}

	// This method is to write in the Excel cell, Row num and Col num are the
	// parameters
	public static void setCellData(Integer RowNum, int ColNum, String Result) throws Exception {
		try {
			row = excelWSheet.getRow(RowNum);
			cell = row.getCell(ColNum, MissingCellPolicy.RETURN_BLANK_AS_NULL);
			if (cell == null) {
				cell = row.createCell(ColNum);
				cell.setCellValue(Result);
			} else {
				cell.setCellValue(Result);
			}

			// Constant variables Test Data path and Test Data file name
			FileOutputStream fileOut = new FileOutputStream(
					LMSAppConstants.excel_Path + LMSAppConstants.excel_FileName);
			excelWbook.write(fileOut);
			fileOut.flush();
			fileOut.close();
		} catch (Exception e) {
			throw (e);

		} finally {
			if (excelWbook != null)
				excelWbook.close();
			if (excelFile != null)
				excelFile.close();
		}

	}
}
