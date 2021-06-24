package domain;

import java.util.List;

import domain.exceptions.EnrollmentRulesViolationException;

public class EnrollCtrl {

	public void enroll(Student s, List<CSE> courses) throws EnrollmentRulesViolationException {

        checkPassed(s, courses);
        checkPrerequisites(s, courses);
        checkExamTimeConflicts(courses);
        checkEnrollDuplication(courses);
        checkAllowedUnitsCount(s, courses);

		for (CSE o : courses)
			s.takeCourse(o.getCourse(), o.getSection());
	}

    private void checkPassed(Student s, List<CSE> courses) throws EnrollmentRulesViolationException {
        for (CSE o : courses) {
            if (s.hasPassed(o.getCourse()))
                throw new EnrollmentRulesViolationException(String.format("The student has already passed %s", o.getCourse().getName()));
        }
    }

    private void checkPrerequisites(Student s, List<CSE> courses) throws EnrollmentRulesViolationException {
        for (CSE o : courses) {
            Course course = o.getCourse();
		    for (Course pre : course.getPrerequisites()) {
			    if (!s.hasPassed(pre))
                    throw new EnrollmentRulesViolationException(String.format("The student has not passed %s as a prerequisite of %s", pre.getName(), o.getCourse().getName()));
    		}
        }
    }

    private void checkExamTimeConflicts(List<CSE> courses) throws EnrollmentRulesViolationException {
		for (CSE o : courses) {
            for (CSE o2 : courses) {
                if (o != o2 && o.hasExamTimeConflict(o2))
                    throw new EnrollmentRulesViolationException(String.format("Two offerings %s and %s have the same exam time", o, o2));             
            }
        } 
    }

    private void checkEnrollDuplication(List<CSE> courses) throws EnrollmentRulesViolationException {
		for (CSE o : courses) {
            for (CSE o2 : courses) {
                if (o != o2 && o.getCourse().equals(o2.getCourse()))
                    throw new EnrollmentRulesViolationException(String.format("%s is requested to be taken twice", o.getCourse().getName()));
            }
        } 
    }

    private void checkAllowedUnitsCount(Student s, List<CSE> courses) throws EnrollmentRulesViolationException {
		int unitsRequested = 0;
		for (CSE o : courses)
			unitsRequested += o.getCourse().getUnits();

		if ((s.getGpa() < 12 && unitsRequested > 14) ||
				(s.getGpa() < 16 && unitsRequested > 16) ||
				(unitsRequested > 20))
			throw new EnrollmentRulesViolationException(String.format("Number of units (%d) requested does not match GPA of %f", unitsRequested, s.getGpa()));
    }
}
