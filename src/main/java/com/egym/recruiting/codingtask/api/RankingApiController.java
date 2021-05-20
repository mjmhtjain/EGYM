package com.egym.recruiting.codingtask.api;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.NativeWebRequest;

import com.egym.recruiting.codingtask.model.RankingUser;
import com.egym.recruiting.codingtask.service.ExerciseService;

@javax.annotation.Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2021-04-25T15:49:22.268+02:00[Europe/Berlin]")

@Controller
@RequestMapping("${openapi.eGym Coding Task.base-path:}")
public class RankingApiController implements RankingApi {

	private final NativeWebRequest request;

	@Autowired
	private ExerciseService exerciseService;

	@org.springframework.beans.factory.annotation.Autowired
	public RankingApiController(NativeWebRequest request) {
		this.request = request;
	}

	@Override
	public Optional<NativeWebRequest> getRequest() {
		return Optional.ofNullable(request);
	}

	@Override
	public ResponseEntity<List<RankingUser>> ranking(final List<Long> userIds) {

		return ResponseEntity.status(HttpStatus.OK).body(exerciseService.ranking(userIds));
	}
}
