package dq.dqlang.k6;

import dq.dqlang.k6.options.Options;
import dq.dqlang.k6.request.Request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class K6LoadTest {

    private int repetition;
    private Options options;
    private Request request;
}