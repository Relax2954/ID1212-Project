/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.net;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.StringJoiner;
import java.util.concurrent.ForkJoinPool;
import Protocol.common.Constants;
import Protocol.common.MessageException;
import Protocol.common.MsgType;
import Protocol.common.MessageSplitter;
import java.util.logging.Level;
import java.util.logging.Logger;
import server.controller.Controller;

/**
 *
 * @author Relax2954
 */
/**
 * Handles all communication with one particular client.
 */
public class ClientHandler implements Runnable {

    private static final String INPUT_DELIMETER = " ";

    private final TheServer server;
    private final SocketChannel clientChannel;
    private final ByteBuffer msgFromClient = ByteBuffer.allocateDirect(Constants.MAX_MSG_LENGTH);
    private final MessageSplitter msgSplitter = new MessageSplitter();
    private String input = null;
    private int inputInt = 0;
    private boolean connected;
    private String tempor; //this is for just printiing out the current ___fc__
    ByteBuffer BufferedTempor;
    private String infomsg;
    ByteBuffer Buffinfomsg;
    ByteBuffer BuffMyTurn;
    ByteBuffer BufferedScore;
    ByteBuffer BuffCheckerString;

    Controller controller;

    /**
     * Creates a new instance, which will handle communication with one specific
     * client connected to the specified channel.
     *
     * @param clientChannel The socket to which this handler's client is
     * connected.
     */
    ClientHandler(TheServer server, SocketChannel clientChannel) {
        controller = new Controller();
        this.server = server;
        this.clientChannel = clientChannel;
        connected = true;

    }

    public ByteBuffer createBufferedMessage(String msg) {
        StringJoiner joiner = new StringJoiner(Constants.MSG_TYPE_DELIMETER);
        joiner.add(MsgType.NETWORKING.toString());
        joiner.add(msg);
        String messageWithLengthHeader = MessageSplitter.prependLengthHeader(joiner.toString());
        return ByteBuffer.wrap(messageWithLengthHeader.getBytes());
    }

    /**
     * The run loop handling all communication with the connected client.
     */
    @Override
    public void run() {
        while (msgSplitter.hasNext()) {
            try {
                Message msg = new Message(msgSplitter.nextMsg());
                switch (msg.msgType) {
                    case START:
                        String gameentry = msg.msgBody;
                        if (gameentry.toLowerCase().contains("game".toLowerCase())) {
                            BuffCheckerString = createBufferedMessage(controller.gamelogcheckerTable + "\n Please note that this is a regular tic tac toe. All regular rules apply. \n Now it is X turn to play.");
                            sendMsg(BuffCheckerString);
                            controller.res();
                        } else {
                            ByteBuffer BuffNotification = createBufferedMessage("Please start game or continue playing.");
                            sendMsg(BuffNotification);
                        }
                        break;
                    case INPUT:
                        input = msg.msgBody;
                        inputInt = Integer.parseInt(input);
                        if (controller.getWinner() != null || controller.isFinished() == 0) {
                            infomsg = "Please start a new game. The last one has finished.";
                            Buffinfomsg = createBufferedMessage(infomsg);
                            sendMsg(Buffinfomsg);
                            break;
                        }
                        tempor = controller.MidAction(inputInt);
                        BufferedTempor = createBufferedMessage(tempor);
                        sendMsg(BufferedTempor);
                        if (!controller.isWon() || controller.isFinished() != 0) {
                            BuffMyTurn = createBufferedMessage("Now it is " + controller.getCurrentPick() + " turn to play");
                            sendMsg(BuffMyTurn);
                        }
                        if (controller.isWon()) { //This if statement is here because I 
                            controller.scoreIncrement();
                            BufferedScore = createBufferedMessage(controller.getWinner()+" won." + " The total score: " + "Player 1: " + controller.score1 + " Player 2: " + controller.score2);
                            sendMsg(BufferedScore);
                        }
                        break;
                    case WRONGINPUT:
                        ByteBuffer BufferedWronginput = createBufferedMessage("Please start game or mark the desired field.");
                        sendMsg(BufferedWronginput);
                        break;
                    case DISCONNECT:
                        ByteBuffer BufferedDisconnect = createBufferedMessage("You are now disconnected.");
                        sendMsg(BufferedDisconnect);
                        disconnectClient();
                        break;
                    default:
                        throw new MessageException("Received corrupt message: " + msg);
                }
            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    /**
     * Sends the specified message to the connected client.
     *
     * @param msg The message to send.
     * @throws IOException If failed to send message.
     */
    void sendMsg(ByteBuffer msg) throws IOException {
        clientChannel.write(msg);
        if (msg.hasRemaining()) {
            throw new MessageException("Could not send message");
        }
    }

    /**
     * Reads a message from the connected client, then submits a task to the
     * default <code>ForkJoinPool</code>. That task which will handle the
     * received message.
     *
     * @throws IOException If failed to read message
     */
    void recvMsg() throws IOException {
        msgFromClient.clear();
        int numOfReadBytes;
        numOfReadBytes = clientChannel.read(msgFromClient);
        if (numOfReadBytes == -1) {
            throw new IOException("Client has closed connection.");
        }
        String recvdString = extractMessageFromBuffer();
        msgSplitter.appendRecvdString(recvdString);
        ForkJoinPool.commonPool().execute(this);
    }

    private String extractMessageFromBuffer() {
        msgFromClient.flip();
        byte[] bytes = new byte[msgFromClient.remaining()];
        msgFromClient.get(bytes);
        return new String(bytes);
    }

    /**
     * Closes this instance's client connection.
     *
     * @throws IOException If failed to close connection.
     */
    void disconnectClient() throws IOException {
        clientChannel.close();
    }

    private static class Message {

        private MsgType msgType;
        private String msgBody;
        private String receivedString;

        private Message(String receivedString) {
            parse(receivedString);
            this.receivedString = receivedString;
        }

        private void parse(String strToParse) {
            try {
                String[] msgTokens = strToParse.split(Constants.MSG_TYPE_DELIMETER);
                msgType = MsgType.valueOf(msgTokens[Constants.MSG_TYPE_INDEX].toUpperCase());
                if (hasBody(msgTokens)) {
                    msgBody = msgTokens[Constants.MSG_BODY_INDEX].trim();
                }
            } catch (Throwable throwable) {
                throw new MessageException(throwable);
            }
        }

        private boolean hasBody(String[] msgTokens) {
            return msgTokens.length > 1;
        }
    }
}
