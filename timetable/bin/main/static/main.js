var app = new Vue({
		  el: '#app',
		  data: {
		  	count: '0',
// isActive: [false, false]
		  	isActive: false,
		  	timetables: null
		  },
		  methods: {
		  	open: function(event){
		  		// this.isActive[id] = !this.isActive[id];
		  		this.isActive = !this.isActive;
		  	}
		  },
		  mounted () {
			 axios
			 .get(Timetable.BASE_URI + '/timetables?id=1&id=10&id=19&id=28&id=33&id=48&id=58&id=83&id=93')
			 .then(response => {
				 var tempTimetables = [];
				 var timetable = {};
				 response.data.forEach(function(item, index){
					 timetable = {};
					 item.forEach(function(course){
						 course.schedules.forEach(function(schedule){
							 if(!timetable[schedule.dayOfWeek]){
								 timetable[schedule.dayOfWeek] = {};
							 }
							 timetable[schedule.dayOfWeek][schedule.timeslot] = course.subject.name;
						 });
					 });
					 tempTimetables[index] = timetable;
				 });
				 this.timetables = tempTimetables;
				 this.count = response.data.length;
			 })
		  }
		});