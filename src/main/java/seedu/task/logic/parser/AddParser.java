//@@author A0161247J
package seedu.task.logic.parser;

import static seedu.task.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.task.commons.exceptions.IllegalValueException;
import seedu.task.commons.util.DateUtil;
import seedu.task.logic.commands.AddCommand;
import seedu.task.logic.commands.Command;
import seedu.task.logic.commands.IncorrectCommand;

/**
 * Parser class used to parse a add command
 */
public class AddParser extends Parser {
	private final Pattern FLOATING_ARGS_FORMAT = Pattern.compile("\\s*(?<name>.+)\\s*");
	private final Pattern DEADLINE_ARGS_FORMAT = Pattern.compile("\\s*(?<name>.+)\\s*(?<endDate>\\d{2}-\\d{2}-\\d{4})\\s*(?<endTime>\\d{2}:\\d{2})?\\s*");
	private final Pattern EVENT_ARGS_FORMAT = Pattern.compile("\\s*(?<name>.+)\\s+(?<startDate>\\d{2}-\\d{2}-\\d{4})\\s*(?<startTime>\\d{2}:\\d{2})?\\s+(?<endDate>\\d{2}-\\d{2}-\\d{4})\\s*(?<endTime>\\d{2}:\\d{2})?\\s*");
	
	@Override
	public Command parseCommand(String args) {
		Command toReturn = null;
		boolean hasException = false;
		
		try {
			if (isEventCommand(args)) {
				toReturn = createEventTask(args);
			} else if (isDeadlineCommand(args)) {
				toReturn = createDeadlineTask(args);
			} else if (isFloatingCommand(args)) {
				toReturn = createFloatingTask(args);
			} else {
				throw new IllegalArgumentException();
			}
		} catch (NullPointerException e) {
        	hasException = true;
        } catch (ParseException e) {
        	hasException = true;
        } catch (IndexOutOfBoundsException e) {
        	hasException = true;
        } catch (IllegalArgumentException e) {
        	hasException = true;
        } catch (IllegalValueException e) {
        	hasException = true;
        } catch (DateTimeParseException e) {
            hasException = true;
        }
		
		if (hasException) {
        	toReturn = new IncorrectCommand(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, 
                    		AddCommand.MESSAGE_USAGE));
        }
		
		return toReturn;
	}
	
	private boolean isDeadlineCommand(String args) throws NullPointerException {
		final Matcher matcher = DEADLINE_ARGS_FORMAT.matcher(args);
		return matcher.matches();
	}
	
	private boolean isEventCommand(String args) throws NullPointerException {
		final Matcher matcher = EVENT_ARGS_FORMAT.matcher(args);
		return matcher.matches();
	}
	
	private boolean isFloatingCommand(String args) throws NullPointerException {
		final Matcher matcher = FLOATING_ARGS_FORMAT.matcher(args);
		return matcher.matches();
	}
	
	/**
	 * Creates a EditCommand for a DeadlineTask given a list of arguments expects args to have the form
	 * "[NAME] 00-00-0000"
	 * 
	 * @throws ParseException 
	 * @throws IllegalArgumentException
	 * @throws IllegalValueException 
	 */
	private Command createDeadlineTask(String args) throws IllegalArgumentException, ParseException, IllegalValueException, DateTimeParseException {
		Matcher matcher = DEADLINE_ARGS_FORMAT.matcher(args);
		
		if (!matcher.matches()) {
			throw new IllegalArgumentException();
		}
		
		String name = matcher.group("name");
		String endDateString = matcher.group("endDate");
		String endTimeString = matcher.group("endTime");
		
		LocalDateTime endDate = (endTimeString == null) ? 
			DateUtil.parseStringToLocalDate(endDateString) :
			DateUtil.parseStringToLocalDateTime(endDateString + " " + endTimeString);
		
        return new AddCommand(name, endDate);
	}
	
	/**
	 * Creates an EditCommand for an EventTask given a string argument that has the form
	 * "[NAME] 00-00-0000 [00:00] 00-00-0000" [00:00]
	 * 
	 * @throws ParseException
	 * @throws IllegalArgumentException 
	 * @throws IllegalValueException 
	 */
	private Command createEventTask(String args) throws ParseException, IllegalArgumentException, IllegalValueException, DateTimeParseException {
		Matcher matcher = EVENT_ARGS_FORMAT.matcher(args);
		
		if (!matcher.matches()) {
			throw new IllegalArgumentException();
		}
		
		String name = matcher.group("name");
		String startDateString = matcher.group("startDate");
		String endDateString = matcher.group("endDate");
		
		String startTimeString = matcher.group("startTime");
		String endTimeString = matcher.group("endTime");
		
		LocalDateTime startDate = (startTimeString == null) ? 
				DateUtil.parseStringToLocalDate(startDateString) :
				DateUtil.parseStringToLocalDateTime(startDateString + " " + startTimeString);
		
		LocalDateTime endDate = (endTimeString == null) ? 
				DateUtil.parseStringToLocalDate(endDateString) :
				DateUtil.parseStringToLocalDateTime(endDateString + " " + endTimeString);
		
		if (startDate.isAfter(endDate)) {
			throw new IllegalArgumentException();
		}
				
        return new AddCommand(name, startDate, endDate);
	}
	
	/**
	 * Creates an AddCommand for a Task given an index and a name
	 * "[INDEX] thisisanewname"
	 * 
	 * @throws ParseException 
	 * @throws IllegalArgumentException
	 * @throws IllegalValueException 
	 */
	private Command createFloatingTask(String args) throws IllegalArgumentException, ParseException, IllegalValueException {
		Matcher matcher = FLOATING_ARGS_FORMAT.matcher(args);
		
		if (!matcher.matches()) {
			throw new IllegalArgumentException();
		}
		
		String name = matcher.group("name").trim();
		
        return new AddCommand(name);
	}
}
