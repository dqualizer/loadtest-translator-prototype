package dq.util;

import dq.dqlang.loadtest.LoadTestConfig;
import org.springframework.stereotype.Component;

/**
 * Class to replace placeholder constants with actual values
 */
@Component
public class ConstantReplacer {

    public LoadTestConfig replace(LoadTestConfig loadTest) {
        //TODO Implement
        return loadTest;
    }
}