package org.example;

import java.util.ArrayList;
import java.util.List;

public class NodeMovementData {

    // holds ammount of steps that get us to a final position,
    // before entering the looping phase
    List<Long> stepsInFinalPosition;
    Long loopSize; //number of steps to complete the movement loop
    Long loopStep; //number of steps after which the loop starts
    // holds number of steps counting from after reaching the loopStep and before ending the loop
    // where we reach a final position
    List<Long> recurrentStepsInFinalPosition;

    int indexOutOfLoop = 0;
    long numberOfLoops = 0;
    int indexInLoop = 0;

    Long currentPosition;


    public NodeMovementData(List<Long> stepsInFinalPosition, Long loopSize, Long loopStep) {
        this.stepsInFinalPosition = new ArrayList<>();
        stepsInFinalPosition.stream()
                .filter(step -> step < loopStep)
                .forEach(step -> this.stepsInFinalPosition.add(step));
        this.loopSize = loopSize;
        this.loopStep = loopStep;
        this.recurrentStepsInFinalPosition = new ArrayList<>();
        stepsInFinalPosition.stream()
                .filter(step -> step >= loopStep)
                .forEach(step -> this.recurrentStepsInFinalPosition.add(step - loopStep));
    }

    public void getNextFinalPositionStep() {
        if(indexOutOfLoop < stepsInFinalPosition.size()) {
            Long position = stepsInFinalPosition.get(indexOutOfLoop);
            indexOutOfLoop++;
            currentPosition = position;
        } else {
            Long position = recurrentStepsInFinalPosition.get(indexInLoop) + loopStep + numberOfLoops * loopSize;
            indexInLoop++;
            if (indexInLoop == recurrentStepsInFinalPosition.size()) {
                indexInLoop = 0;
                numberOfLoops++;
            }
            currentPosition = position;
        }
    }

    public Long getCurrentPosition() {
        return currentPosition;
    }
}
