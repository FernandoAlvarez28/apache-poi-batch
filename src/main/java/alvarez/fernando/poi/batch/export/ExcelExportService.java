package alvarez.fernando.poi.batch.export;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface ExcelExportService {
	
	/**
	 * Convert the {@code values} using the {@code valueMapper} to insert in a spreadsheet.
	 * @param headers Strings to be used as first row and to get each row's collumn.
	 * @param values Objects to be mapped and inserted in the spreadsheet.
	 * @param valueMapper Function to map a value to a map by the header's name.
	 * @param outputStream Stream to receive the generated spreadshet.
	 * @param <T> Type of the value that will be mapped and inserted.
	 * @throws IOException Any exception during the write to the stream.
	 */
	<T> void exportToStream(List<String> headers, List<T> values, Function<T, Map<String, String>> valueMapper, OutputStream outputStream) throws IOException;

}