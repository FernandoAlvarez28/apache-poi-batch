package alvarez.fernando.poi.batch.profile;

import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.Optional;

public enum Profiles {
	
	NOT_OPTIMIZED(ProfilesValues.NOT_OPTIMIZED, "Not optimized implementation, which is easier to implement but can use a lot of memory"),
	OPTIMIZED(ProfilesValues.OPTIMIZED, "More optimized implementation, which (tries) to prevent memory usage issues"),
	;
	
	public static final String NOT_OPTIMIZED_VALUE = ProfilesValues.NOT_OPTIMIZED;
	public static final String OPTIMIZED_VALUE = ProfilesValues.OPTIMIZED;
	
	private final String name;
	private final String description;
	
	Profiles(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public static Optional<Profiles> identifyProfile(@Nullable String givenProfile) {
		if (!StringUtils.hasText(givenProfile)) {
			return Optional.empty();
		}
		
		for (Profiles profile : values()) {
			if (profile.name.equals(givenProfile)) {
				return Optional.of(profile);
			}
		}
		
		return Optional.empty();
	}
	
}