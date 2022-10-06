package com.github.nsbazhenov.skytec.data.status;

/**
 * List of errors that occur.
 *
 * @author Bazhenov Nikita
 *
 */
public class Error {
    public static final String ERROR_OCCURRED = "Error occurred: ";
    public static final String TRANSACTION_ROLLED_BACK = "Transaction is being rolled back: ";
    public static final String PLAYER_HAS_NO_MONEY = "The player does not have enough money";
    public static final String ERROR_ADD_GOLD = "Couldn't get the clan to add gold";
    public static final String CLAN_HAS_NO_MONEY = "The clan does not have enough money";
    public static final String ERROR_TAKE_GOLD = "Couldn't get the player to add gold";
    public static final String ERROR_CREATING_EVENT = "Creating audit event failed";
    public static final String ERROR_CREATING_DB = "The database was not created, error - {}";
    public static final String NO_ERROR = "Successfully";
}
