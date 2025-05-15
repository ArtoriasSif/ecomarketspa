package han.msvc_feedback.mscv_feedback;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class MscvFeedbackApplication {

	public static void main(String[] args) {
		SpringApplication.run(MscvFeedbackApplication.class, args);
	}

}
