package domain;

import java.util.ArrayList;
import java.util.List;

import domain.exceptions.EnrollmentRulesViolationException;

public class EnrollCtrl {
    static final int LIMITED_GPA = 12;
    static final int MIN_A_GPA = 16;

    static final int LIMITED_MAX_ALLOWED_UNITS = 14;
    static final int BELOW_A_MAX_ALLOWED_UNITS = 16;
    static final int MAX_ALLOWED_UNITS = 20;

    private List<EnrollmentRulesViolationException> violations;

	public List<EnrollmentRulesViolationException> enroll(Student s, List<Offering> offerings) {
        violations = new ArrayList<>();

        checkPassed(s, offerings);
        checkPrerequisites(s, offerings);
        checkExamTimeConflicts(offerings);
        checkEnrollDuplication(offerings);
        checkAllowedUnitsCount(s, offerings);

        if (violations.isEmpty()) {
            for (Offering o : offerings)
			    s.takeCourse(o.getCourse(), o.getSection());
        }

        return violations;
	}

    private void checkPassed(Student s, List<Offering> offerings) {
        for (Offering o : offerings) {
            if (s.hasPassed(o.getCourse()))
                violations.add(new EnrollmentRulesViolationException(
                    String.format("The student has already passed %s", o.getCourse().getName()))
                );
        }
    }

    private void checkPrerequisites(Student s, List<Offering> offerings) {
        for (Offering o : offerings) {
            Course course = o.getCourse();
		    for (Course pre : course.getPrerequisites()) {
			    if (!s.hasPassed(pre))
                violations.add(new EnrollmentRulesViolationException(
                    String.format("The student has not passed %s as a prerequisite of %s", pre.getName(), o.getCourse().getName()))
                );
    		}
        }
    }

    private void checkExamTimeConflicts(List<Offering> offerings) {
        for (int i = 0; i < offerings.size(); i++)
        {
            for (int j = i + 1; j < offerings.size(); j++)
            {
                Offering o = offerings.get(i);
                Offering o2 = offerings.get(j);
                if (o.hasExamTimeConflict(o2))
                    violations.add(new EnrollmentRulesViolationException(
                        String.format("Two offerings %s and %s have the same exam time", o, o2))
                    );             
            }
        }
    }

    private void checkEnrollDuplication(List<Offering> offerings) {
        for (int i = 0; i < offerings.size(); i++)
        {
            for (int j = i + 1; j < offerings.size(); j++)
            {
                Offering o = offerings.get(i);
                Offering o2 = offerings.get(j);
                if (o.getCourse().equals(o2.getCourse()))
                    violations.add(new EnrollmentRulesViolationException(
                        String.format("%s is requested to be taken twice", o.getCourse().getName()))
                    );
            }
        }
    }

    private void checkAllowedUnitsCount(Student s, List<Offering> offerings) {
		int unitsRequested = 0;
		for (Offering o : offerings)
			unitsRequested += o.getCourse().getUnits();

		if ((s.getGpa() < LIMITED_GPA && unitsRequested > LIMITED_MAX_ALLOWED_UNITS) ||
				(s.getGpa() < MIN_A_GPA && unitsRequested > BELOW_A_MAX_ALLOWED_UNITS) ||
				(unitsRequested > MAX_ALLOWED_UNITS))
			violations.add(new EnrollmentRulesViolationException(
                String.format("Number of units (%d) requested does not match GPA of %f", unitsRequested, s.getGpa()))
            );
    }
}
