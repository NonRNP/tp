package seedu.duke.commands;

public class HelpCommand extends Command {

    @Override
    public CommandResult executeCommand() {
        String helpMessage = "Here are the list of commands:\n"
                + "1. cap MC_AND_GRADES  - Calculates the total cap for the semester\n"
                + "2. view MODULE_CODE - View the module details\n"
                + "3. bus routes - Check for a NUS bus route from stop to another\n"
                + "4. planner add DESCRIPTION/DATE/START_TIME/END_TIME - Add an event to your schedule\n"
                + "5. help - View this menu again\n"
                + "6. bye - Exit Kolinux";
        return new CommandResult(helpMessage);
    }
}
