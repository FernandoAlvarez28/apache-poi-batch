package alvarez.fernando.poi.batch.profile;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;

import java.util.Optional;

@Configuration
@Order
class ProfileConfiguration {
	
	public ProfileConfiguration(Environment environment) {
		final String[] activeProfiles = environment.getActiveProfiles();
		
		//TODO Spring interrupts the initialization before when there is no active profile because no ExcelExportService impl is found
		
		if (activeProfiles.length == 0) {
			this.printAvailableProfiles("No \"spring.profiles.active\" property or \"-Dspring.profiles.active\" VM argument given");
		}
		
		final String activeProfile = activeProfiles[0];
		final Optional<Profiles> identifiedProfile = Profiles.identifyProfile(activeProfile);
		
		if (identifiedProfile.isEmpty()) {
			this.printAvailableProfiles("Profile \"" + activeProfile + "\" not identified");
		}
		
	}
	
	private void printAvailableProfiles(String message) {
		final var stringBuilder = new StringBuilder("\n")
				.append(message)
				.append(". Please use one of the following profiles:");
		
		final Profiles[] availableProfiles = Profiles.values();
		for (int i = 0; i < availableProfiles.length; i++) {
			Profiles availableProfile = availableProfiles[i];
			stringBuilder.append("\n - ")
					.append(availableProfile.getName())
					.append(": ")
					.append(availableProfile.getDescription())
					.append(i == availableProfiles.length - 1 ? '.' : ';');
		}
		
		System.out.println(stringBuilder); //NOSONAR
		System.exit(1);
		
	}
	
}