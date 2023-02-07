package dq.dqlang.k6;

import dq.dqlang.k6.options.Options;
import dq.dqlang.k6.request.Request;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class K6LoadTest {

    private int repetition;
    private Options options;
    private Request request;
}