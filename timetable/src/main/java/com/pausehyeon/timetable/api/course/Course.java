package com.pausehyeon.timetable.api.course;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.pausehyeon.timetable.api.ClassRoom;
import com.pausehyeon.timetable.api.Schedule;
import com.pausehyeon.timetable.api.Subject;
import com.pausehyeon.timetable.api.Teacher;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder()
@Getter
@ToString
@EqualsAndHashCode
public class Course {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	private Long subjectId;
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="subjectId", insertable=false, updatable=false)
	private Subject subject;
	
	private Long teacherId;
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="teacherId", insertable=false, updatable=false)
	private Teacher teacher;
	
	private Long classRoomId;
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="classRoomId", insertable=false, updatable=false)
	private ClassRoom classRoom;
	
	@Setter
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	@JoinColumn(name="courseId")
	List<Schedule> schedules;
}
