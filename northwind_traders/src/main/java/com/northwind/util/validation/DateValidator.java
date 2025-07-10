package com.northwind.util.validation;

import com.northwind.util.jsf.GrowlUtil;
import com.northwind.util.constants.messages.DateMessages;
import com.northwind.util.constants.messages.GrowlTitles;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

/**
 * Utility class for validating date-related inputs.
 * <p>
 * Ensures valid birth dates and prevents future dates when not allowed.
 * </p>
 */
@ApplicationScoped
public class DateValidator {

    private static final int MIN_AGE = 18;
    private static final int MAX_AGE = 70;

    /**
     * Validates whether the given birth date falls within an acceptable age range.
     *
     * @param birthDate the date to validate
     * @return true if valid, false otherwise
     */
    public boolean isValidBirthDate(LocalDate birthDate) {
        if (birthDate == null) {
            return false;
        }

        LocalDate today = LocalDate.now();
        LocalDate minDate = today.minusYears(MAX_AGE);
        LocalDate maxDate = today.minusYears(MIN_AGE);

        if (birthDate.isBefore(minDate) || birthDate.isAfter(maxDate)) {
            GrowlUtil.warning(GrowlTitles.WARN_TITLE,
                    String.format(DateMessages.WARN_INVALID_AGE, MIN_AGE, MAX_AGE)
            );
            return false;
        }

        return true;
    }

    /**
     * Validates whether the given date is not in the future.
     *
     * @param date the date to check
     * @return true if the date is today or in the past, false otherwise
     */
    public boolean isFutureDate(Date date) {
        if (date == null) {
            return false;
        }

        LocalDate selectedDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (selectedDate.isAfter(LocalDate.now())) {
            GrowlUtil.warning(GrowlTitles.WARN_TITLE, DateMessages.WARN_FUTURE_DATE
            );
            return false;
        }

        return true;
    }

    /**
     * Checks if the end date is before the start date.
     * <p>
     * Useful for validating date ranges where the end must be equal to or after the start.
     *
     * @param start the starting date
     * @param end   the ending date
     * @return true if end is before start, false otherwise
     */
    public boolean isEndDateBeforeStartDate(LocalDate start, LocalDate end) {
        if (start == null || end == null) return false;
        return end.isBefore(start);
    }

    /**
     * Checks if the given start and end dates are exactly the same.
     * <p>
     * Prevents queries or operations with meaningless or invalid single-day ranges.
     *
     * @param start the starting date
     * @param end   the ending date
     * @return true if both dates are equal, false otherwise
     */
    public boolean areDatesEqual(LocalDate start, LocalDate end) {
        if (start == null || end == null) return false;
        return start.isEqual(end);
    }
}

