package com.ader1y.template.core.support;

import com.ader1y.template.core.support.base.BadCode;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.write.metadata.WriteSheet;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ExcelExportUtil {

    private static final Logger logger = LoggerFactory.getLogger(ExcelExportUtil.class);

    /**
     * 将多份数据导出到多个sheet中
     *
     * @param templatePath 模板路径 e.g. (resources/excel can use excel/)
     * @param fileName     文件名称
     * @param map          非列表数据
     * @param sheetDataMap sheet对应的数据 < key-sheet name, value-sheet data>
     */
    public static <T> void exportExcel(HttpServletResponse response, String templatePath, String fileName, Map<String, Object> map, Map<String, List<T>> sheetDataMap) {
        try (InputStream inputStream = ExcelExportUtil.class.getClassLoader().getResourceAsStream(templatePath)) {
            com.alibaba.excel.ExcelWriter excelWriter = EasyExcelFactory.write(response.getOutputStream()).withTemplate(inputStream).build();
            Assert.notEmpty(sheetDataMap, "Excel填充数据为空");
            for (Map.Entry<String, List<T>> entry : sheetDataMap.entrySet()) {
                WriteSheet writeSheet = EasyExcelFactory.writerSheet(entry.getKey()).build();
                excelWriter.fill(entry.getValue(), writeSheet);
                if (MapUtils.isNotEmpty(map)) {
                    excelWriter.fill(map, writeSheet);
                }
            }

            fileName = StringUtils.isBlank(fileName) ? UUID.randomUUID() + LocalDate.now().toString() : fileName;
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".xlsx", StandardCharsets.UTF_8));
            excelWriter.finish();
            excelWriter.close();
        } catch (IOException e) {
            BadCode.EXPORT_ERROR.throwEx();
        }
    }

    public static void exportExcel(HttpServletResponse response, String templatePath, String fileName, Map<String, Object> map, List<?> rows) {
        try (InputStream inputStream = ExcelExportUtil.class.getClassLoader().getResourceAsStream(templatePath)) {
            com.alibaba.excel.ExcelWriter excelWriter = EasyExcelFactory.write(response.getOutputStream()).withTemplate(inputStream).build();
            WriteSheet writeSheet = EasyExcelFactory.writerSheet().build();
            if (!CollectionUtils.isEmpty(rows)) {
                excelWriter.fill(rows, writeSheet);
            }
            if (!ObjectUtils.isEmpty(map)) {
                excelWriter.fill(map, writeSheet);
            }
            fileName = StringUtils.isBlank(fileName) ? UUID.randomUUID() + LocalDate.now().toString() : fileName;
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName + ".xlsx", StandardCharsets.UTF_8));
            excelWriter.finish();
            excelWriter.close();
        } catch (IOException e) {
            BadCode.EXPORT_ERROR.throwEx();
        }
    }

}
