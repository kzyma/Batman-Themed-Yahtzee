/*
 * Yahtzee GUI-based game that supports multiple human and AI players, and a
 * high score system for recording and viewing past top score. Official
 * game rules from http://www.hasbro.com/common/instruct/Yahtzee.pdf are 
 * followed.
 * Class: CSC421
 * Project 2
 * Professor: Dr. Spiegels
 * 
 * @author: Ken Zyma
 * @version: 1.0.0
 * @since: 1.6
 */ 

/**
 * Interface provides common methods for any callback routine.
 * <p>This is typically used to wrap a 'function pointer' for remote calling or
 * distributed listening.
 */
public interface CallbackEvent {
    /**
     * Fired when event is performed.
     */
    public void eventPerformed();
}
