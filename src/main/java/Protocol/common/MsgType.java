package Protocol.common;

/**
 *
 * @author Relax2954
 */
public enum MsgType {
    
    //start the actual game
    START,
    //startgame check
    INPUT,
    //The information that the server returns about the certain input
    NETWORKING,
    //client sends wrong input
    WRONGINPUT,
    //disconnect
    DISCONNECT

}
