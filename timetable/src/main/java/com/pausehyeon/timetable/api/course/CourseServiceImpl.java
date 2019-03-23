package com.pausehyeon.timetable.api.course;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pausehyeon.timetable.api.Schedule;
import com.pausehyeon.timetable.api.ScheduleRepository;

@Service
public class CourseServiceImpl implements CourseService {
	private static final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);

	@Autowired
	CourseRepository repository;

	@Autowired
	ScheduleRepository scheduleRepository;
	
	@PostConstruct
	public void saveDefault() {
		saveCourseAndSchedules(1, 1, 1, 0, 2, 2, 2);
		saveCourseAndSchedules(1, 2, 2, 1, 3, 3, 3);
		saveCourseAndSchedules(2, 1, 3, 0, 3, 2, 3, 2, 4);
		saveCourseAndSchedules(3, 3, 3, 4, 1, 4, 2, 4, 3, 4, 4);
	}
	
	private void saveCourseAndSchedules(int subjectId, int teacherId, int classRoomId, int... args) {
		Course course = repository.save(Course.builder()
				.subjectId(new Long(subjectId))
				.teacherId(new Long(teacherId))
				.classRoomId(new Long(classRoomId))
				.build()
				);
		logger.debug("coures saved: " + course.toString());
		
		List<Schedule> schedules = new ArrayList<Schedule>();
		
		for(int i=0; i<args.length/2; i++) {
			schedules.add(
					Schedule.builder()
					.courseId(course.getId())
					.dayOfWeek(args[i])
					.timeslot(args[i==0?1:i*2])
					.build()
					);
		}
		
		course.setSchedules(schedules);
		repository.save(course);
		logger.debug("coures saved: " + course.toString());
	}

	@Transactional(readOnly = true)
	@Override
	public Object getCourses() {
		return (List<Course>) repository.findAll();
	}
	
	@Override
	public Object getTimetables() {
		List<Course> courses = (List<Course>) repository.findAll();
		
		//4C2 combination
		int[] arr = { 1, 2, 3, 4};
		int n = arr.length;
		int r = 2;
		int[] combArr = new int[n];

		doCombination(combArr, n, r, 0, 0, arr, courses);
		
		return null;
	}
	
	private void doCombination(int[] combArr, int n, int r, int index, int target, int[] arr, List<Course> courses) {
//		System.out.println("=> " + n + " " + r + " " + index + " " + target);

		if (r == 0) {
			// 다 뽑은 경우
//			System.out.println(Arrays.toString(combArr));
			for (int i = 0; i < index; i++)
//				System.out.print(arr[combArr[i]] + " ");
				logger.debug(""+courses.get(combArr[i]));

			logger.debug("-");
		} else if (target == n) {
			// 끝까지 다 검사한 경우

			return;

		} else {
			combArr[index] = target;
			// (i) 뽑는 경우
			doCombination(combArr, n, r - 1, index + 1, target + 1, arr, courses);

			// (ii) 안 뽑는경우
			doCombination(combArr, n, r, index, target + 1, arr, courses);
		}
	}
}
