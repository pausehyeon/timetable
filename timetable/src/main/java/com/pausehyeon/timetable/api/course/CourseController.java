package com.pausehyeon.timetable.api.course;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
	
	@GetMapping("/timetable")
	public Object getTimetable() throws BusinessException {
		logger.debug("start");
		return service.getTimetables();
	}
}
