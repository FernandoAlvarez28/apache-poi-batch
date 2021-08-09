package alvarez.fernando.poi.batch.export;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class DataHeader {
	
	public static final String ID = "ID";
	public static final String UUID = "UUID";
	public static final String NAME = "Name";
	public static final String EMAIL = "E-mail";
	public static final String PHONE_NUMBER = "Phone number";
	public static final String COMPANY = "Company";
	public static final String JOB = "Job";
	public static final String COUNTRY = "Country";
	public static final String BIRTHDAY = "Birthday";
	public static final String AGE = "Age";
	
	private static final List<String> AS_LIST = Arrays.asList(
			ID
			, UUID
			, NAME
			, EMAIL
			, PHONE_NUMBER
			, COMPANY
			, JOB
			, COUNTRY
			, BIRTHDAY
			, AGE
	);
	
	public static final int SIZE = AS_LIST.size();
	
	private DataHeader() {}
	
	public static List<String> getAsList() {
		return Collections.unmodifiableList(AS_LIST);
	}
	
}