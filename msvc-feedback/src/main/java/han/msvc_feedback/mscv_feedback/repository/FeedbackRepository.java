package han.msvc_feedback.mscv_feedback.repository;

import han.msvc_feedback.mscv_feedback.model.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}
