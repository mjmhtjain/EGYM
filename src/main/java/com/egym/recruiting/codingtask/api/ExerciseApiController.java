package com.egym.recruiting.codingtask.api;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import com.egym.recruiting.codingtask.service.ExerciseService;
import com.egym.recruiting.codingtask.dto.ExerciseDTO;
import com.egym.recruiting.codingtask.model.Exercise;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-04-25T15:49:22.268+02:00[Europe/Berlin]")

@Controller
@RequestMapping("${openapi.eGym Coding Task.base-path:}")
public class ExerciseApiController implements ExerciseApi {

	private final NativeWebRequest request;

	@Autowired
	private ExerciseService exerciseService;

	@Autowired
	public ExerciseApiController(NativeWebRequest request) {
		this.request = request;
	}

	@Override
	public Optional<NativeWebRequest> getRequest() {
		return Optional.ofNullable(request);
	}

	@Override
	public ResponseEntity<Exercise> insert(final ExerciseDTO exercise) {
		return ResponseEntity.status(HttpStatus.CREATED).body(exerciseService.insert(exercise));
	}

	@Override
	public ResponseEntity<Exercise> update(final Long exerciseId, final ExerciseDTO exercise) {
		return ResponseEntity.status(HttpStatus.OK).body(exerciseService.update(exerciseId, exercise));

	}

}
