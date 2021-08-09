package alvarez.fernando.poi.batch.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Data {
	
	private final Long id;
	
	private final UUID uuid;
	
	private final String name;
	
	private final String email;
	
	private final String phoneNumber;
	
	private final String company;
	
	private final String job;
	
	private final String country;
	
	private final LocalDate birthday;
	
	public int getAge() {
		return Period.between(this.birthday, LocalDate.now()).getYears();
	}
	
}