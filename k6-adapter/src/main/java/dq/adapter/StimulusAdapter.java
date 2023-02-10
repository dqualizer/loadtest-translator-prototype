package dq.adapter;

import dq.dqlang.constants.LoadTestConstants;
import dq.dqlang.constants.accuracy.ConstantLoad;
import dq.dqlang.constants.accuracy.LoadIncrease;
import dq.dqlang.constants.accuracy.LoadPeak;
import dq.dqlang.k6.options.*;
import dq.dqlang.loadtest.Stimulus;
import dq.exception.UnknownTermException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sound.sampled.Line;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class StimulusAdapter {

    @Autowired
    private ConstantsLoader constantsLoader;

    public Options adaptStimulus(Stimulus stimulus) {
        String loadProfile = stimulus.getLoadProfile();
        Scenario scenario;

        switch (loadProfile) {
            case "LOAD_PEAK" -> scenario = this.getLoadPeakScenario(stimulus);
            case "LOAD_INCREASE" -> scenario = this.getLoadIncreaseScenario(stimulus);
            case "CONSTANT_LOAD" -> scenario = this.getConstantLoadScenario(stimulus);
            default -> throw new UnknownTermException(loadProfile);
        }
        Scenarios scenarios = new Scenarios(scenario);
        Options options = new Options(scenarios);

        return options;
    }

    public Scenario getLoadPeakScenario(Stimulus stimulus) {
        LoadTestConstants constants = constantsLoader.load();
        LoadPeak loadPeak = constants.getAccuracy().getLoadPeak();

        String highestLoad = stimulus.getHighestLoad();
        int target;
        switch (highestLoad) {
            case "HIGH" -> target = loadPeak.getHigh();
            case "VERY_HIGH" -> target = loadPeak.getVeryHigh();
            case "EXTREMELY_HIGH" -> target = loadPeak.getExtremelyHigh();
            default -> throw new UnknownTermException(highestLoad);
        }

        String timeToHighestLoad = stimulus.getTimeToHighestLoad();
        String duration;
        switch (timeToHighestLoad) {
            case "SLOW" -> duration = loadPeak.getSlow();
            case "FAST" -> duration = loadPeak.getFast();
            case "VERY_FAST" -> duration = loadPeak.getVeryFast();
            default -> throw new UnknownTermException(timeToHighestLoad);
        }
        String coolDownDuration = loadPeak.getCoolDownDuration();

        Stage stage1 = new Stage(duration, target);
        Stage stage2 = new Stage(coolDownDuration, 0);

        LinkedHashSet<Stage> stages = new LinkedHashSet<>();
        stages.add(stage1);
        stages.add(stage2);
        Scenario scenario = new RampingScenario(stages);
        return scenario;
    }

    public Scenario getLoadIncreaseScenario(Stimulus stimulus) {
        LoadTestConstants constants = constantsLoader.load();
        LoadIncrease loadIncrease = constants.getAccuracy().getLoadIncrease();

        String typeOfIncrease = stimulus.getTypeOfIncrease();
        int exponent;
        switch (typeOfIncrease) {
            case "LINEAR" -> exponent = loadIncrease.getLinear();
            case "QUADRATIC" -> exponent = loadIncrease.getQuadratic();
            case "CUBIC" -> exponent = loadIncrease.getCubic();
            default -> throw new UnknownTermException(typeOfIncrease);
        }

        int endTarget = loadIncrease.getEndTarget();
        int startTarget = loadIncrease.getStartTarget();
        String stageDuration = loadIncrease.getStageDuration();

        LinkedHashSet<Stage> stages = this.getIncreasingStages(stageDuration, startTarget, endTarget, exponent);
        Scenario scenario = new RampingScenario(stages);
        return scenario;
    }

    private LinkedHashSet<Stage> getIncreasingStages(String stageDuration, int startTarget, int endTarget, int exponent) {
        LinkedHashSet<Stage> stages = new LinkedHashSet<>();
        int stageCounter = 1;
        int currentTarget = startTarget;

        while (currentTarget < endTarget) {
            currentTarget = startTarget * (int) Math.pow(stageCounter, exponent);
            Stage currentStage = new Stage(stageDuration, currentTarget);
            stages.add(currentStage);

            stageCounter++;
        }
        Stage lastStage = new Stage(stageDuration, 0);
        stages.add(lastStage);

        return stages;
    }

    public Scenario getConstantLoadScenario(Stimulus stimulus) {
        LoadTestConstants constants = constantsLoader.load();
        ConstantLoad constantLoad = constants.getAccuracy().getConstantLoad();

        String baseLoad = stimulus.getBaseLoad();
        int vus;
        switch (baseLoad) {
            case "LOW" -> vus = constantLoad.getLow();
            case "MEDIUM" -> vus = constantLoad.getMedium();
            case "HIGH" -> vus = constantLoad.getHigh();
            default -> throw new UnknownTermException(baseLoad);
        }

        int accuracy = stimulus.getAccuracy();
        int maxDuration = constantLoad.getMaxDuration();
        int minDuration = constantLoad.getMinDuration();

        int duration = (int)(maxDuration * (accuracy/100.0));
        int trueDuration = Math.max(minDuration, duration);

        Scenario scenario = new ConstantScenario(vus, trueDuration);
        return scenario;
    }
}