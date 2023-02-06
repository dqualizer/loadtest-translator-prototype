package dq.dqlang.mapping;

import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

@Getter
@ToString
public class ServerInfo {

    private String host;
    private String environment;
}