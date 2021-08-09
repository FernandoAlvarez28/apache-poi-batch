package alvarez.fernando.poi.batch.data;

import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Repository
class DataRepositoryFakeImpl implements DataRepository {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DataRepositoryFakeImpl.class);
	
	private final int dataQuantity;
	
	private final Random idGenerator;
	
	public DataRepositoryFakeImpl(@Value("${data.quantity:300000}") int dataQuantity) {
		this.dataQuantity = dataQuantity;
		this.idGenerator = new Random();
	}
	
	@Override
	public List<Data> load() {
		final List<Data> dataList = new ArrayList<>(this.dataQuantity);
		final var faker = new Faker();
		
		for (int i = 0; i < this.dataQuantity; i++) {
			dataList.add(new Data.DataBuilder()
					.id(idGenerator.nextLong())
					.uuid(UUID.randomUUID())
					.name(faker.name().fullName())
					.email(faker.internet().emailAddress())
					.phoneNumber(faker.phoneNumber().cellPhone())
					.company(faker.company().name())
					.job(faker.job().title())
					.country(faker.country().name())
					.birthday(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
					.build());
			
			if (i % 1_000 == 0) {
				LOGGER.info("{}/{} data created", i, this.dataQuantity);
			}
		}
		
		return dataList;
	}
	
}