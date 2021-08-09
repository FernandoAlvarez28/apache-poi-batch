package alvarez.fernando.poi.batch.export;

import alvarez.fernando.poi.batch.data.Data;
import alvarez.fernando.poi.batch.data.DataService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class DataExportService implements ApplicationRunner {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DataExportService.class);
	
	private static final String FOLDER_PATH_PARAMETER = "destination";
	
	private static final String NULL_PLACEHOLDER = "--";
	
	private final DataService dataService;
	
	private final ExcelExportService excelExportService;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		final List<Data> data = dataService.load();
		
		LOGGER.info("Data loaded. Starting spreadsheet export...");
		
		final String folderPath = this.getValidatedFolderPathArgument(args);
		final String fileName = "Exported spreadsheet " + LocalDateTime.now() + ".xlsx";
		
		try (final var fileOutputStream = new FileOutputStream(folderPath + File.separator + fileName)) {
			this.excelExportService.exportToStream(DataHeader.getAsList(), data, this::mapData, fileOutputStream);
		}
		
		LOGGER.info("Spreadsheet successfully exported to \"{}{}{}\"!", folderPath, File.separator, fileName);
	}
	
	private String getValidatedFolderPathArgument(ApplicationArguments args) throws IllegalArgumentException {
		final List<String> argsOptionValues = args.getOptionValues(FOLDER_PATH_PARAMETER);
		
		if (CollectionUtils.isEmpty(argsOptionValues)) {
			throw new IllegalArgumentException("No argument for \"" + FOLDER_PATH_PARAMETER + "\". Please provide a folder path to export the Excel spreadsheet");
		}
		
		final String folderPath = argsOptionValues.get(0);
		if (!StringUtils.hasText(folderPath)) {
			throw new IllegalArgumentException("Empty argument for \"" + FOLDER_PATH_PARAMETER + "\"");
		}
		
		final File file;
		try {
			file = Paths.get(folderPath).toFile();
		} catch (InvalidPathException e) {
			throw new IllegalArgumentException("\"" + folderPath + "\" is not a valid path", e);
		}
		
		if (!file.exists()) {
			throw new IllegalArgumentException("Folder \"" + folderPath + "\" don't exists");
		}
		
		if (!file.isDirectory()) {
			throw new IllegalArgumentException("\"" + folderPath + "\" is not a folder");
		}
		
		if (!file.canWrite()) {
			throw new IllegalArgumentException("Cannot write inside \"" + folderPath + "\"");
		}
		
		return folderPath;
	}
	
	private Map<String, String> mapData(Data data) {
		final Map<String, String> dataMap = new HashMap<>(DataHeader.SIZE);
		
		this.putData(dataMap, DataHeader.ID, data.getId());
		this.putData(dataMap, DataHeader.UUID, data.getUuid());
		this.putData(dataMap, DataHeader.NAME, data.getName());
		this.putData(dataMap, DataHeader.EMAIL, data.getEmail());
		this.putData(dataMap, DataHeader.PHONE_NUMBER, data.getPhoneNumber());
		this.putData(dataMap, DataHeader.COMPANY, data.getCompany());
		this.putData(dataMap, DataHeader.JOB, data.getJob());
		this.putData(dataMap, DataHeader.COUNTRY, data.getCountry());
		this.putData(dataMap, DataHeader.BIRTHDAY, data.getBirthday());
		this.putData(dataMap, DataHeader.AGE, data.getAge());
		
		return dataMap;
	}
	
	private void putData(Map<String, String> map, String header, @Nullable Object value) {
		final boolean isValueNull;
		if (value instanceof String) {
			isValueNull = !StringUtils.hasText(((String) value));
		} else {
			isValueNull = value == null;
		}
		map.put(header, isValueNull ? NULL_PLACEHOLDER : value.toString());
	}
	
}