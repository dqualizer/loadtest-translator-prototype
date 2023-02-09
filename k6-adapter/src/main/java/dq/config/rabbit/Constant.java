package dq.config.rabbit;

/**
 * Constant values necessary for RabbitMQ
 */
public class Constant {

    public final static String LOADTEST_QUEUE = "loadtest";
    public final static String K6_QUEUE = "k6";

    public final static String LOADTEST_EXCHANGE = "translator-loadtest";
    public final static String K6_EXCHANGE = "loadtest-k6";

    public final static String GET_KEY = "get";
    public final static String POST_KEY = "post";
}