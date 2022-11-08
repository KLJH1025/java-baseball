package baseball.controller;

import baseball.domain.Computer;
import baseball.domain.User;
import baseball.view.View;


import static baseball.controller.Exception.validateAskRestartOrExitInput;
import static baseball.controller.Exception.validateUserInput;

public class Controller {
    static int NUMBER_LENGTH = 3;
    static int GAME_RESTART = 1;
    static int GAME_EXIT = 2;
    static int NUMBER_RANGE_START = 1;
    static int NUMBER_RANGE_END = 9;

    static String USER_CHOICE = "게임을 새로 시작하려면 " + GAME_RESTART + ", 종료하려면 " + GAME_EXIT + "를 입력하세요";



    public void run(){
        Computer computer = new Computer();
        User user = new User();

        gameStartSetting(computer);

        startGame(computer, user);

        View.printGameOverMessage();
    }
    private void gameStartSetting(Computer computer){
        View.gameStartMessage();
        computer.setRandomNumber(NUMBER_RANGE_START, NUMBER_RANGE_END);
    }

    private void startGame(Computer computer, User user){
        boolean gameContinue;
        do{
            gameContinue = playGameOnce(computer, user);
        }while(gameContinue);
    }
    private boolean playGameOnce(Computer computer, User user){
        String input = View.inputUserNumber();
        validateUserInput(input);
        user.saveUserInput(input);
        int strikeNum = countStrike(computer, user);
        if(strikeNum == NUMBER_LENGTH){
            boolean ask = askRestartOrExit(computer, user);
            if(ask){
                computer.setRandomNumber(NUMBER_RANGE_START, NUMBER_RANGE_END);
                return true;
            }
            else{
                return false;
            }
        }
        else{
            int ballNum = countBall(computer, user);
            View.printBallAndStrike(strikeNum, ballNum);
            return true;
        }
    }

    private boolean askRestartOrExit(Computer computer, User user) {

        View.correctAnswer(NUMBER_LENGTH);

        String input = View.askRestartOrExit(USER_CHOICE);
        validateAskRestartOrExitInput(input);
        if(Integer.parseInt(input) == GAME_EXIT)
            return false;
        else if(Integer.parseInt(input) == GAME_RESTART)
            return true;
        else
            throw new IllegalArgumentException("잘못된 입력입니다.");
    }

    private int countStrike(Computer computer, User user) {
        int count = 0;
        for(int i=0; i<NUMBER_LENGTH; i++){
            if(computer.numbers.get(i) == user.numbers.get(i))
                count++;
        }
        return count;
    }
    private int countBall(Computer computer, User user){
        int count = 0;
        for(int i=0; i<NUMBER_LENGTH; i++){
            if(computer.numbers.get(i) != user.numbers.get(i) && computer.numbers.contains(user.numbers.get(i)))
                count++;
        }
        return count;
    }

}