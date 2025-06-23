package han.msvc_feedback.mscv_feedback.assembler;

import han.msvc_feedback.mscv_feedback.controller.FeedbackControllerV2;
import han.msvc_feedback.mscv_feedback.model.entity.Feedback;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class FeedbackModelAssembler implements RepresentationModelAssembler<Feedback, EntityModel<Feedback>> {

    @Override
    public EntityModel<Feedback> toModel(Feedback feedback) {

        return EntityModel.of(feedback,

                linkTo(methodOn(FeedbackControllerV2.class).findByIdFeedback(feedback.getFeedbackId())).withSelfRel(),


                linkTo(methodOn(FeedbackControllerV2.class).updateByIdFeedback(feedback.getFeedbackId(), null)).withRel("update"),


                linkTo(methodOn(FeedbackControllerV2.class).deleteByIdFeedback(feedback.getFeedbackId())).withRel("delete"),


                linkTo(methodOn(FeedbackControllerV2.class).findAllFeedback()).withRel("all-feedbacks"),


                linkTo(methodOn(FeedbackControllerV2.class).findByAllFeedbackProduct(feedback.getProductIdFeedback())).withRel("feedbacksForProduct")
        );
    }
}