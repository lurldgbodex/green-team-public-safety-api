package sgcor.tech.green_team_public_safety_api;

import org.springframework.boot.SpringApplication;

public class TestGreenTeamPublicSafetyApiApplication {

	public static void main(String[] args) {
		SpringApplication.from(GreenTeamPublicSafetyApiApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
