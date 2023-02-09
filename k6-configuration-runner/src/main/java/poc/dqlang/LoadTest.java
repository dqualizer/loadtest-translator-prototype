package poc.dqlang;

import poc.dqlang.options.Options;
import poc.dqlang.request.Request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class LoadTest {

    private int repetition;
    private Options options;
    private Request request;
}