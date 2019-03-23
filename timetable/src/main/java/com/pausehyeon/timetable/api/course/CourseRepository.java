package com.pausehyeon.timetable.api.course;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
	public List<Course> findByIdIn(List<Long> ids);
}
