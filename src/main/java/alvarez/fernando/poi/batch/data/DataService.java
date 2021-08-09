package alvarez.fernando.poi.batch.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DataService.class);
	
	private final DataRepository dataRepository;
	
	private List<Data> cachedData;
	
	public DataService(DataRepository dataRepository) {
		this.dataRepository = dataRepository;
	}
	
	public List<Data> load() {
		if (this.cachedData != null) {
			return this.getCachedData();
		}
		
		this.cachedData = dataRepository.load();
		LOGGER.info("{} data loaded", this.cachedData.size());
		return this.getCachedData();
	}
	
	private List<Data> getCachedData() {
		//I would like to return with Collections.unmodifiableList, but I set null on the values I iterate to optimize it further
		return this.cachedData;
	}
	
}