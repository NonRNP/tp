package seedu.kolinux.timetable;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.kolinux.exceptions.KolinuxException;
import seedu.kolinux.module.ModuleDb;
import seedu.kolinux.module.ModuleList;
import seedu.kolinux.timetable.lesson.Lesson;
import seedu.kolinux.timetable.subcommand.Subcommand;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static seedu.kolinux.timetable.lesson.Lesson.days;
import static seedu.kolinux.timetable.lesson.Lesson.getIndex;
import static seedu.kolinux.timetable.lesson.Lesson.schoolHours;


public class TimetableTest {

    private static final String[] VALID_ADD_ARGUMENTS = new String[] {"CS1231", "TUT", "monday", "1200", "1300"};
    private static final String[] INVALID_ADD_TIMING = new String[] {"CS1231", "TUT", "monday", "1300", "1200"};
    private static final String[] INACCESSIBLE_ADD_PERIOD = new String[] {"CS1010", "TUT", "monday", "1200", "1300"};
    private static final String[] INVALID_DELETE_ARGUMENT = new String[] {"CS1010", "LESSON", "monday"};
    private static final String[] UPDATE_LESSON_ARGUMENTS = new String[] {"CS1231", "TUT", "monday", "tuesday", "1500"};
    private static final ModuleDb moduleDb = new ModuleDb();
    private ModuleList moduleList = new ModuleList();
    private Timetable timetable = new Timetable(moduleList);


    @BeforeAll
    public static void initModuleDb() {
        moduleDb.initModuleDb();
    }

    @BeforeEach
    public void clear() {
        moduleList.clear();
    }

    @Test
    public void addLessonToTimetable_validInput_lessonAdded() throws KolinuxException {
        timetable.clearTimetable();
        Lesson lesson = new Lesson(VALID_ADD_ARGUMENTS);
        timetable.addSubcommand.addLessonToTimetable(lesson);
        assertEquals(timetable.timetableData[getIndex("1200", schoolHours)][getIndex("monday", days)],
                "CS1231 TUT");
        timetable.clearTimetable();
    }

    @Test
    public void addLessonToTimetable_invalidTiming_lessonNotAdded() {
        try {
            timetable.clearTimetable();
            Lesson lesson = new Lesson(INVALID_ADD_TIMING);
            timetable.addSubcommand.addLessonToTimetable(lesson);
            timetable.clearTimetable();
        } catch (KolinuxException exception) {
            assertEquals(Subcommand.INVALID_ADD_FORMAT, exception.getMessage());
        }
    }

    @Test
    public void addLessonToTimetable_inaccessiblePeriod_lessonNotAdded() {
        try {
            timetable.clearTimetable();
            Lesson firstLesson = new Lesson(VALID_ADD_ARGUMENTS);
            Lesson secondLesson = new Lesson(INACCESSIBLE_ADD_PERIOD);
            timetable.addSubcommand.addLessonToTimetable(firstLesson);
            timetable.addSubcommand.addLessonToTimetable(secondLesson);
            timetable.clearTimetable();
        } catch (KolinuxException exception) {
            assertEquals(Subcommand.INACCESSIBLE_PERIOD, exception.getMessage());
        }
    }

    @Test
    public void inputLesson_validLesson_lessonAdded() throws KolinuxException {
        timetable.clearTimetable();
        moduleList.storeModuleByCode("CS1231", moduleDb);
        timetable.executeAddSubCommand(VALID_ADD_ARGUMENTS);
        assertEquals("CS1231 TUT",
                timetable.timetableData[getIndex("1200", schoolHours)][getIndex("monday", days)]);
        timetable.clearTimetable();
    }

    @Test
    public void inputLesson_lessonNotInModuleList_lessonNotAdded() {
        try {
            timetable.clearTimetable();
            timetable.executeAddSubCommand(VALID_ADD_ARGUMENTS);
            timetable.clearTimetable();
        } catch (KolinuxException exception) {
            assertEquals("CS1231 not found in module list", exception.getMessage());
        }
    }

    @Test
    public void deleteLesson_validLesson_lessonDeleted() throws KolinuxException {
        timetable.clearTimetable();
        Lesson lesson = new Lesson(VALID_ADD_ARGUMENTS);
        timetable.addSubcommand.addLessonToTimetable(lesson);
        timetable.executeDeleteSubCommand(VALID_ADD_ARGUMENTS);
        assertFalse(timetable.addSubcommand.isLessonInTimetable("CS1010", "TUT", "monday"));
        timetable.clearTimetable();
    }

    @Test
    public void deleteLesson_lessonNotInTimetable_lessonNotDeleted() {
        try {
            timetable.clearTimetable();
            timetable.executeDeleteSubCommand(VALID_ADD_ARGUMENTS);
            timetable.clearTimetable();
        } catch (KolinuxException e) {
            assertEquals("CS1231 TUT" + Subcommand.MISSING_LESSON_TO_DELETE, e.getMessage());

        }
    }

    @Test
    public void deleteLesson_invalidLesson_lessonNotDeleted() {
        try {
            timetable.clearTimetable();
            timetable.executeDeleteSubCommand(INVALID_DELETE_ARGUMENT);
            timetable.clearTimetable();
        } catch (KolinuxException e) {
            assertEquals("CS1010 LESSON" + Subcommand.MISSING_LESSON_TO_DELETE, e.getMessage());
        }
    }

    @Test
    public void updateLesson_validLesson_lessonUpdated() throws KolinuxException {
        timetable.clearTimetable();
        moduleList.storeModuleByCode("CS1231", moduleDb);
        timetable.executeAddSubCommand(VALID_ADD_ARGUMENTS);
        timetable.executeUpdateSubCommand(UPDATE_LESSON_ARGUMENTS);
        assertFalse(timetable.addSubcommand.isLessonInTimetable("CS1231",
                "TUT", "monday"));
        assertTrue(timetable.addSubcommand.isLessonInTimetable("CS1231",
                "TUT", "tuesday"));
        timetable.clearTimetable();
    }

    @Test
    public void updateLesson_lessonNotFoundInTimetable_lessonNotUpdated() {
        try {
            timetable.clearTimetable();
            moduleList.storeModuleByCode("CS1231", moduleDb);
            timetable.executeUpdateSubCommand(UPDATE_LESSON_ARGUMENTS);
            timetable.clearTimetable();
        } catch (KolinuxException e) {
            assertEquals(Subcommand.MISSING_LESSON_TO_UPDATE, e.getMessage());
        }
    }

}