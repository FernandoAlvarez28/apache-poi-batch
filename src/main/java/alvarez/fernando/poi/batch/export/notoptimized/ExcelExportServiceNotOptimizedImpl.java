package alvarez.fernando.poi.batch.export.notoptimized;

import alvarez.fernando.poi.batch.export.ExcelExportService;
import alvarez.fernando.poi.batch.profile.Profiles;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
@Profile(Profiles.NOT_OPTIMIZED_VALUE)
public class ExcelExportServiceNotOptimizedImpl implements ExcelExportService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelExportServiceNotOptimizedImpl.class);
	
	@Override
	public <T> void exportToStream(List<String> headers, List<T> values, Function<T, Map<String, String>> valueMapper, OutputStream outputStream) throws IOException {
		try (final Workbook workbook = new XSSFWorkbook()) {
			//HSSFWorkbook only supports up to 65536 rows
			final Sheet sheet = workbook.createSheet();
			
			this.createHeader(workbook, sheet, headers);
			
			this.createRowsFromData(workbook, sheet, headers, values, valueMapper);
			
			LOGGER.info("Resizing columns...");
			this.autoSizeSheet(sheet, headers);
			
			workbook.write(outputStream);
		}
	}
	
	private void createHeader(Workbook workbook, Sheet sheet, List<String> headers) {
		final CellStyle styleHeader = workbook.createCellStyle();
		final Font font = workbook.createFont();
		font.setBold(true);
		styleHeader.setFont(font);
		
		final Row header = sheet.createRow(0);
		
		this.freezeHeader(sheet);
		this.setAutoFilterForHeader(sheet, headers);
		
		for (int i = 0 ; i < headers.size(); i++) {
			final Cell cell = header.createCell(i);
			cell.setCellStyle(styleHeader);
			cell.setCellValue(headers.get(i));
		}
	}
	
	private void freezeHeader(Sheet sheet) {
		sheet.createFreezePane(0,1);
	}
	
	private void setAutoFilterForHeader(Sheet sheet, Collection<String> headers) {
		sheet.setAutoFilter(new CellRangeAddress(0, 0, 0, headers.size() - 1));
	}
	
	private void autoSizeSheet(Sheet sheet, Collection<String> headers) {
		for (int i = 0; i < headers.size(); i++) {
			sheet.autoSizeColumn(i);
		}
	}
	
	private <T> void createRowsFromData(Workbook workbook, Sheet sheet, List<String> headers, List<T> values, Function<T, Map<String, String>> valueMapper) {
		final CellStyle bodyStyle = workbook.createCellStyle();
		bodyStyle.setWrapText(true);
		
		for (int i = 0; i < values.size(); i++) {
			final Row row = sheet.createRow(i + 1);
			final Map<String, String> dataMappedByHeader = valueMapper.apply(values.get(i));
			
			for (int h = 0; h < headers.size(); h++) {
				final String cellValue = dataMappedByHeader.get(headers.get(h));
				
				if (StringUtils.isBlank(cellValue)) {
					continue;
				}
				
				final Cell cell = row.createCell(h);
				
				cell.setCellStyle(bodyStyle);
				cell.setCellValue(cellValue);
			}
			
			if (i % 1_000 == 0) {
				LOGGER.info("{}/{} data mapped and inserted", i, values.size());
			}
			
			values.set(i, null); //Free the object to be collected by the Garbage Collector
		}
	}
	
}