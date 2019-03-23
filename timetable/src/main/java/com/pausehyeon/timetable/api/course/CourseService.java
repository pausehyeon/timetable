package com.pausehyeon.timetable.api.course;

import java.util.List;

public interface CourseService {
	public Object getCourses();

	public Object getTimetables(List<Long> ids);
}
