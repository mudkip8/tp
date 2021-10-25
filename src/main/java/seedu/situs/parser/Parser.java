package seedu.situs.parser;

import seedu.situs.Situs;
import seedu.situs.command.*;
import seedu.situs.exceptions.SitusException;
import seedu.situs.ingredients.Ingredient;


import java.time.LocalDate;
import java.time.format.DateTimeParseException;


public class Parser {
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_ADD = "add";
    private static final String COMMAND_SUBTRACT = "subtract";
    private static final String COMMAND_DELETE = "delete";
    private static final String COMMAND_UPDATE = "update";
    private static final String COMMAND_HELP = "help";
    private static final String COMMAND_EXIT = "exit";
    private static final String COMMAND_DATE = "date";
    private static final String COMMAND_EXPIRE = "expire";
    private static final String COMMAND_FIND = "find";
    private static final String COMMAND_ALERTS = "alerts";
    private static final String COMMAND_SET_THRESHOLD = "set";

    private static final String INVALID_COMMAND_MESSAGE = "Invalid command!";
    private static final String DELETE_ERROR_MESSAGE = "Nothing to remove!";
    private static final String NUMBER_FORMAT_ERROR_MESSAGE = "Invalid number format!";
    private static final String NOT_FOUND_MESSAGE = "Ingredient not found!";
    private static final String INCORRECT_PARAMETERS_MESSAGE = "The number of parameters is wrong!";
    private static final String EXPIRY_FORMAT_ERROR_MESSAGE = "Invalid expiry date format!"
            + '\n' + "Please key in the expiry date in the format dd/mm/yyyy!";
    private static final String INVALID_ALERT_TYPE_MESSAGE = "Not an alert type!";

    private static final String SPACE_SEPARATOR = " ";
    private static final String EMPTY_STRING = "";
    private static final String DELIMITER = "n/|a/|e/";

    private static final int ADD_COMMAND_ARGUMENT_COUNT = 4;
    private static final int SUBTRACT_COMMAND_ARGUMENT_COUNT = 3;
    private static final int UPDATE_COMMAND_ARGUMENT_COUNT = 4;


    public static boolean isExit(String command) {
        return (command.equals(COMMAND_EXIT));
    }

    /**
     * Sends the input to the relevant command parser.
     *
     * @param command The user input String
     * @return the output message
     * @throws SitusException when there is an issue with the input
     */
    public static String parse(String command) throws SitusException {

        String[] words = command.split(SPACE_SEPARATOR, 2);

        switch (words[0]) {
        case COMMAND_LIST:
            return parseListCommand();
        case COMMAND_ADD:
            return parseAddCommand(command);
        case COMMAND_SUBTRACT:
            return parseSubtractCommand(command);
        case COMMAND_DELETE:
            return parseDeleteCommand(command);
        case COMMAND_UPDATE:
            return parseUpdateCommand(command);
        case COMMAND_DATE:
            return parseDateCommand(command);
        case COMMAND_HELP:
            return parseHelpCommand();
        case COMMAND_EXPIRE:
            return parseExpireCommand(command);
        case COMMAND_FIND:
            return parseFindCommand(command);
        case COMMAND_ALERTS:
            return parseAlertsCommand(command);
        case COMMAND_SET_THRESHOLD:
            return parseSetCommand(command);
        case COMMAND_EXIT:
            return "";
        default:
            return INVALID_COMMAND_MESSAGE;
        }
    }

    /**
     * Parses and executes the {@code find} command.
     *
     * @param command The user input String
     * @return Search results for entered keywords
     * @throws SitusException If no keywords are entered
     */
    private static String parseFindCommand(String command) throws SitusException {
        String[] keywords = command.replace(COMMAND_FIND, "").trim().split(SPACE_SEPARATOR);

        assert (keywords != null);

        for (int i = 0; i < keywords.length; i++) {
            if (keywords[i].isEmpty()) {
                throw new SitusException(INCORRECT_PARAMETERS_MESSAGE);
            }
            keywords[i] = keywords[i].trim();
        }

        String resultMsg = "";
        for (int i = 0; i < keywords.length; i++) {
            String keyword = keywords[i];
            if (i > 0) {
                resultMsg += "\n";
            }
            resultMsg += new FindCommand(keyword).run();
        }
        return resultMsg;
    }

    /**
     * Calls and executes the {@code help} command.
     *
     * @return a message summarising all possible commands recognised by SITUS
     */
    private static String parseHelpCommand() {
        return new HelpCommand().run();
    }

    /**
     * Parses and executes the {@code update} command.
     *
     * @param command 1 line of user input
     * @return Ingredient updated message
     */
    private static String parseUpdateCommand(String command) throws SitusException {
        String[] details = command.split(DELIMITER);

        if (details.length != UPDATE_COMMAND_ARGUMENT_COUNT) {
            throw new SitusException(INCORRECT_PARAMETERS_MESSAGE);
        }

        assert (details.length == UPDATE_COMMAND_ARGUMENT_COUNT);

        for (int i = 1; i < UPDATE_COMMAND_ARGUMENT_COUNT; i++) {
            details[i] = details[i].trim();
            if (details[i].equals(EMPTY_STRING)) {
                throw new SitusException(INCORRECT_PARAMETERS_MESSAGE);
            }
        }

        try {
            String ingredientName = details[1];
            double ingredientAmount = Double.parseDouble(details[2]);
            LocalDate ingredientExpiry = Ingredient.stringToDate(details[3]);

            Ingredient updatedIngredient =
                    new Ingredient(ingredientName, ingredientAmount,  ingredientExpiry);
            String resultMsg = new UpdateCommand(updatedIngredient).run();

            if (resultMsg.equals(EMPTY_STRING)) {
                resultMsg = NOT_FOUND_MESSAGE;
            }
            return resultMsg;
        } catch (NumberFormatException e) {
            throw new SitusException(NUMBER_FORMAT_ERROR_MESSAGE);
        } catch (DateTimeParseException e) {
            throw new SitusException(EXPIRY_FORMAT_ERROR_MESSAGE);
        }
    }

    /**
     * Parses and executes the {@code add} command.
     *
     * @param command The user input String
     * @return Ingredient added message
     */
    private static String parseAddCommand(String command) throws SitusException {
        String[] details = command.split(DELIMITER);

        if (details.length != ADD_COMMAND_ARGUMENT_COUNT) {
            throw new SitusException(INCORRECT_PARAMETERS_MESSAGE);
        }

        assert (details.length == ADD_COMMAND_ARGUMENT_COUNT);

        for (int i = 1; i < ADD_COMMAND_ARGUMENT_COUNT; i++) {
            details[i] = details[i].trim();
            if (details[i].equals(EMPTY_STRING)) {
                throw new SitusException(INCORRECT_PARAMETERS_MESSAGE);
            }
        }

        try {
            String ingredientName = details[1];
            double ingredientAmount = Double.parseDouble(details[2]);
            LocalDate ingredientExpiry = Ingredient.stringToDate(details[3]);

            Ingredient newIngredient = new Ingredient(ingredientName, ingredientAmount,
                     ingredientExpiry);
            return new AddCommand(newIngredient).run();
        } catch (NumberFormatException e) {
            throw new SitusException(NUMBER_FORMAT_ERROR_MESSAGE);
        } catch (DateTimeParseException e) {
            throw new SitusException(EXPIRY_FORMAT_ERROR_MESSAGE);
        }
    }

    private static String parseSubtractCommand(String command) throws SitusException {
        String[] details = command.split("n/|a/");

        if (details.length != SUBTRACT_COMMAND_ARGUMENT_COUNT) {
            throw new SitusException(INCORRECT_PARAMETERS_MESSAGE);
        }

        assert (details.length == SUBTRACT_COMMAND_ARGUMENT_COUNT);

        for (int i = 1; i < SUBTRACT_COMMAND_ARGUMENT_COUNT; i++) {
            details[i] = details[i].trim();
            if (details[i].equals(EMPTY_STRING)) {
                throw new SitusException(INCORRECT_PARAMETERS_MESSAGE);
            }
        }

        String ingredientName = details[1];
        double subtractAmount = Double.parseDouble(details[2]);
        return new SubtractCommand(ingredientName, subtractAmount).run();
    }




    /**
     * Calls and executes the {@code list} command.
     *
     * @return List of ingredients
     * @throws SitusException if trying to access non-existing ingredients
     */
    private static String parseListCommand() throws SitusException {
        return new ListCommand().run();
    }

    /**
     * Calls and executes the {@code delete} command.
     *
     * @param command The user input String
     * @return Ingredient Deleted Message
     * @throws SitusException if trying to access non-existing ingredients
     */
    private static String parseDeleteCommand(String command) throws SitusException {
        String[] details = command.split("n/|e/");

        if (details.length != 3) {
            throw new SitusException(INCORRECT_PARAMETERS_MESSAGE);
        }


        for (int i = 1; i < details.length; i++) {
            details[i] = details[i].trim();
            if (details[i].equals(EMPTY_STRING)) {
                throw new SitusException(INCORRECT_PARAMETERS_MESSAGE);
            }
        }

        return new DeleteCommand(details[1], details[2]).run();
    }

    /**
     * Parses and executes the {@code date} command.
     *
     * @param command The user's input string
     * @return Date changed success message
     * @throws SitusException if the date format is incorrect
     */
    private static String parseDateCommand(String command) throws SitusException {
        String detail = command.substring(COMMAND_DATE.length()).trim();

        return new DateCommand(detail).run();
    }

    /**
     * Parses and executes the {@code expire} command.
     *
     * @param command The user's input string
     * @return List of ingredients expiring by the specified date
     * @throws SitusException if the date format is incorrect
     */
    private static String parseExpireCommand(String command) throws SitusException {
        String detail = command.substring(COMMAND_EXPIRE.length()).trim();
        try {
            LocalDate expireBeforeDate = Ingredient.stringToDate(detail);
            return new ExpireCommand(expireBeforeDate).run();
        } catch (DateTimeParseException e) {
            throw new SitusException(EXPIRY_FORMAT_ERROR_MESSAGE);
        }
    }

    /**
     * Parses and executes the {@code alerts} command.
     *
     * @param command The user's input string
     * @return The list of ingredients for each alert type
     * @throws SitusException if alert type, date format or amount format is incorrect
     */
    private static String parseAlertsCommand(String command) throws SitusException {
        String detail = command.substring(COMMAND_ALERTS.length()).trim();
        switch (detail) {
        case "all":
            return new AlertCommand().run();
        case "expiry":
            return new AlertExpiringSoonCommand().run();
        case "stock":
            return new AlertLowStockCommand().run();
        default:
            throw new SitusException(INVALID_ALERT_TYPE_MESSAGE);
        }
    }

    /**
     * Parses and executes the {@code set} command.
     *
     * @param command The user's input string
     * @return threshold successfully set message
     * @throws SitusException if threshold type, date format or amount format is incorrect
     */
    private static String parseSetCommand(String command) throws SitusException {
        String[] details = command.split(" ", 3);
        try {
            switch (details[1].trim()) {
            case "expiry":
                AlertExpiringSoonCommand.setExpiryThreshold(Long.parseLong(details[2].trim()));
                return "Successfully set expiry threshold to " + details[2].trim() + " days";
            case "stock":
                AlertLowStockCommand.setLowStockThreshold(Double.parseDouble(details[2].trim()));
                return "Successfully set low stock threshold to " + details[2].trim() + " kg";
            default:
                throw new SitusException("Invalid Input");
            }
        } catch (NumberFormatException e) {
            throw new SitusException(NUMBER_FORMAT_ERROR_MESSAGE);
        }
    }
}
