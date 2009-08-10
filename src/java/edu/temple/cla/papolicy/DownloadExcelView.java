/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.temple.cla.papolicy;

import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.web.servlet.view.document.AbstractExcelView;

/**
 *
 * @author Paul Wolfgang
 */
public class DownloadExcelView extends AbstractExcelView {

    @SuppressWarnings("unchecked")
    protected void buildExcelDocument(
            Map model,
            HSSFWorkbook wb,
            HttpServletRequest request,
            HttpServletResponse response) throws Exception {

        HSSFSheet sheet;
        HSSFRow sheetRow;
        HSSFCell cell;
        List<Map<String, Object>> theList = (List<Map<String, Object>>)model.get("theList");
        sheet = wb.createSheet("Download");
        Map<String, Object> firstRow = theList.get(0);
        int r = 0;
        int c = 0;
        for (String heading : firstRow.keySet()) {
            cell = getCell(sheet, r, c);
            setText(cell, heading);
            ++c;
        }
        for (Map<String, Object> row : theList) {
            ++r;
            c = 0;
            for (Object obj : row.values()) {
                cell = getCell(sheet, r, c);
                if (obj == null) {
                    cell.setCellValue(new HSSFRichTextString("null"));
                } else if (obj instanceof Number) {
                    cell.setCellValue(((Number)obj).doubleValue());
                } else {
                    cell.setCellValue(new HSSFRichTextString(obj.toString()));
                }
                ++c;
            }
        }
    }
}

