package seedu.task.storage;

import com.google.common.eventbus.Subscribe;

import seedu.task.commons.core.ComponentManager;
import seedu.task.commons.core.LogsCenter;
import seedu.task.commons.events.model.TaskBookChangedEvent;
import seedu.task.commons.events.storage.DataSavingExceptionEvent;
import seedu.task.commons.exceptions.DataConversionException;
import seedu.task.model.ReadOnlyTaskBook;
import seedu.task.model.UserPrefs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Manages storage of AddressBook data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private TaskListStorage taskListStorage;
    private UserPrefsStorage userPrefsStorage;


    public StorageManager(TaskListStorage taskListStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.taskListStorage = taskListStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    public StorageManager(String taskListFilePath, String userPrefsFilePath) {
        this(new XmlTaskListStorage(taskListFilePath), new JsonUserPrefsStorage(userPrefsFilePath));
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ AddressBook methods ==============================

    @Override
    public String getTaskListFilePath() {
        return taskListStorage.getTaskListFilePath();
    }

    @Override
    public Optional<ReadOnlyTaskBook> readTaskList() throws DataConversionException, IOException {
        return readTaskList(taskListStorage.getTaskListFilePath());
    }

    @Override
    public Optional<ReadOnlyTaskBook> readTaskList(String filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return taskListStorage.readTaskList(filePath);
    }

    @Override
    public void saveTaskList(ReadOnlyTaskBook taskList) throws IOException {
        saveTaskList(taskList, taskListStorage.getTaskListFilePath());
    }

    @Override
    public void saveTaskList(ReadOnlyTaskBook taskList, String filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        taskListStorage.saveTaskList(taskList, filePath);
    }


    @Override
    @Subscribe
    public void handleTaskListChangedEvent(TaskBookChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveTaskList(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

}