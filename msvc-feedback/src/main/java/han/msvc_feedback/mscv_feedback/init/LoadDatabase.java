package han.msvc_feedback.mscv_feedback.init;

import han.msvc_feedback.mscv_feedback.model.entity.Feedback;
import han.msvc_feedback.mscv_feedback.repository.FeedbackRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initFeedback(FeedbackRepository feedbackRepository) {
        return args -> {
            if (feedbackRepository.count() == 0) {
                Feedback f1 = new Feedback(null, null, "Excelente producto, llegó rápido", 1L, 1L);
                Feedback f2 = new Feedback(null, null, "No era lo que esperaba", 2L, 2L);
                Feedback f3 = new Feedback(null, null, "Muy buena calidad, recomendable", 3L, 1L);

                log.info("Creating feedback {}",feedbackRepository.save(f1));
                log.info("Creating feedback {}",feedbackRepository.save(f2));
                log.info("Creating feedback {}",feedbackRepository.save(f3));


            }
        };
    }
}

