package com.pausehyeon.timetable.api.course;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pausehyeon.timetable.exception.BusinessException;

@RestController
public class CourseController {
	private static final Logger logger = LoggerFactory.getLogger(CourseController.class);

	@Autowired
	CourseService service;
	
	@GetMapping("/courses")
	public Object getReservations() throws BusinessException {
		logger.debug("start");
		return service.getCourses();
	}
	
	//ex. timetables?id=1&id=4&id=11
	@GetMapping("/timetables")
	public Object getTimetables(@RequestParam(value="id", required=true) List<Long> ids) throws BusinessException {
		logger.debug("start");
		for(Long id : ids) {
			logger.debug("id="+id);
		}
		return service.getTimetables(ids);
	}
}
