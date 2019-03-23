package com.pausehyeon.timetable.api.course;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
		// 초기 데이터 적재
		saveCourseAndSchedules(1, 1, 1, 0, 2, 1, 1);
		saveCourseAndSchedules(1, 1, 1, 1, 3, 1, 1);
		saveCourseAndSchedules(1, 2, 2, 1, 3, 3, 3);
		saveCourseAndSchedules(2, 3, 3, 2, 4, 2, 2);
		saveCourseAndSchedules(2, 3, 3, 0, 2, 1, 1);
		saveCourseAndSchedules(2, 3, 3, 1, 1, 3, 4);
		saveCourseAndSchedules(3, 1, 2, 0, 0, 6, 7);
		saveCourseAndSchedules(3, 2, 2, 4, 0, 6, 7);
		saveCourseAndSchedules(3, 3, 2, 3, 3, 6, 7);
		saveCourseAndSchedules(4, 4, 4, 0, 0, 2, 2, 1, 2, 1, 2);
		saveCourseAndSchedules(4, 4, 4, 1, 1, 3, 3, 1, 2, 1, 2);
		saveCourseAndSchedules(4, 4, 4, 1, 1, 3, 3, 7, 8, 7, 8);
		saveCourseAndSchedules(5, 5, 5, 1, 1, 3, 3, 3, 4, 3, 4);
		saveCourseAndSchedules(5, 5, 5, 0, 0, 2, 2, 3, 4, 3, 4);
		saveCourseAndSchedules(5, 6, 5, 0, 0, 2, 2, 3, 4, 3, 4);
		saveCourseAndSchedules(6, 6, 6, 1, 1, 3, 3, 4, 5, 4, 5);
		saveCourseAndSchedules(6, 6, 6, 1, 1, 3, 3, 6, 7, 6, 7);
		saveCourseAndSchedules(6, 6, 6, 0, 0, 2, 2, 6, 7, 6, 7);
		saveCourseAndSchedules(7, 7, 7, 1, 1, 3, 3, 3, 4, 3, 4);
		saveCourseAndSchedules(7, 7, 7, 0, 0, 2, 2, 3, 4, 3, 4);
		saveCourseAndSchedules(7, 7, 7, 0, 0, 2, 2, 3, 4, 3, 4);
		saveCourseAndSchedules(8, 8, 8, 1, 1, 3, 3, 4, 5, 4, 5);
		saveCourseAndSchedules(8, 8, 8, 1, 1, 3, 3, 6, 7, 6, 7);
		saveCourseAndSchedules(8, 8, 8, 0, 0, 2, 2, 6, 7, 6, 7);
		saveCourseAndSchedules(9, 10, 9, 4, 4, 2, 1);
		saveCourseAndSchedules(9, 10, 9, 4, 4, 4, 3);
		saveCourseAndSchedules(9, 10, 9, 2, 2, 8, 7);
		saveCourseAndSchedules(10, 10, 9, 4, 4, 3, 4);
		saveCourseAndSchedules(10, 10, 9, 4, 4, 5, 6);
		saveCourseAndSchedules(10, 10, 9, 2, 2, 5, 6);
	}

	@Transactional(readOnly = true)
	@Override
	public Object getCourses() {
		return (List<Course>) repository.findAll();
	}

	@Override
	public Object getTimetables(List<Long> ids) {
		List<List<Course>> result = new ArrayList<List<Course>>();
		List<Course> courses = (List<Course>) repository.findByIdIn(ids);

		logger.debug("대상 조회결과: " + courses.toString());

		final int MAX_CREDIT = 21;
		final int MIN_CREDIT = 18;
		int min = courses.size();
		int max = courses.size();
		int sum;

		// 최대 과목수: 학점이 낮은 순으로 더해서, 21학점을 넘지 않는 갯수
		// credit 오름차순 정렬
		courses.sort(new Comparator<Course>() {
			@Override
			public int compare(Course o1, Course o2) {
				if (o1.getSubject().getCredit() > o2.getSubject().getCredit()) {
					return 1;
				} else if (o1.getSubject().getCredit() < o2.getSubject().getCredit()) {
					return -1;
				} else {
					return 0;
				}
			}
		});

		sum = 0;
		for (int i = 0; i < courses.size(); i++) {
			if (sum == MAX_CREDIT) {
				max = i;
				break;
			} else if (sum > MAX_CREDIT) {
				max = i - 1;
				break;
			}
			sum += courses.get(i).getSubject().getCredit();
		}

		// 최소 과목수: 학점이 높은 순으로 더해서, 처음 18학점 이상이 되는 갯수
		// credit 내림차순 정렬
		Collections.reverse(courses);

		sum = 0;
		for (int i = 0; i < courses.size(); i++) {
			if (sum >= MIN_CREDIT) {
				min = i;
				break;
			}
			sum += courses.get(i).getSubject().getCredit();
		}
		logger.debug("max=" + max);
		logger.debug("min=" + min);

		int n;
		int r;
		int[] combArr;

		for (int i = min; i < max + 1; i++) {
			n = courses.size();
			r = i;
			combArr = new int[n];

			doCombination(combArr, n, r, 0, 0, courses, result);
		}

		return result;
	}

	private void doCombination(int[] combArr, int n, int r, int index, int target, List<Course> courses, List<List<Course>> result) {
		if (r == 0) {
			List<Course> list = new ArrayList<Course>();

			// 다 뽑은 경우
			for (int i = 0; i < index; i++) {
				list.add(courses.get(combArr[i]));
				logger.debug("" + courses.get(combArr[i]));
			}
			result.add(list);
			logger.debug("-");

		} else if (target == n) {
			// 끝까지 다 검사한 경우
			return;
		} else {
			combArr[index] = target;

			// 타겟이 조건에 맞지 않는 경우, 뽑는 경우를 생략한다.
			if (isTargetOk(combArr, index, target, courses)) {
				// (i) 뽑는 경우
				doCombination(combArr, n, r - 1, index + 1, target + 1, courses, result);
			}

			// (ii) 안 뽑는경우
			doCombination(combArr, n, r, index, target + 1, courses, result);
		}
	}

	private boolean isTargetOk(int[] combArr, int index, int target, List<Course> courses) {

		Long targetSubjectId = courses.get(target).getSubjectId();
		List<Schedule> targetSchedules = courses.get(target).getSchedules();
		for (int i = 0; i < index; i++) {
			// 과목이 겹치는 경우
			if (courses.get(i).getSubjectId() == targetSubjectId) {
				logger.debug("target="+ + courses.get(target).getId() +" compared=" + courses.get(i).getId() + " subject=" + courses.get(target).getSubject().getName());
				return false;
			}

			// 시간이 겹치는 경우
			for (Schedule comparedSchedule : courses.get(i).getSchedules()) {
				for (Schedule targetSchedule : targetSchedules) {
					if (targetSchedule.getDayOfWeek() == comparedSchedule.getDayOfWeek()
							&& targetSchedule.getTimeslot() == comparedSchedule.getTimeslot()) {
						logger.debug("target="+ + courses.get(target).getId() +" compared=" + courses.get(i).getId() + " timeslot=" + targetSchedule.getDayOfWeek() + "요일 "
								+ targetSchedule.getTimeslot() + "교시");
						return false;
					}
				}
			}

		}

		return true;
	}

	private void saveCourseAndSchedules(int subjectId, int teacherId, int classRoomId, int... args) {
		Course course = repository.save(Course.builder()
				.subjectId(new Long(subjectId))
				.teacherId(new Long(teacherId))
				.classRoomId(new Long(classRoomId))
				.build());
		logger.debug("coures saved: " + course.toString());

		List<Schedule> schedules = new ArrayList<Schedule>();

		logger.debug("args="+Arrays.toString(args));
		logger.debug("args.length / 2="+args.length / 2);
		for (int i = 0; i < (args.length / 2); i++) {
			schedules.add(
					Schedule.builder()
							.courseId(course.getId())
							.dayOfWeek(args[i])
							.timeslot(args[i + (args.length / 2)])
							.build());
		}

		course.setSchedules(schedules);
		repository.save(course);
		logger.debug("coures saved: " + course.toString());
	}
}
