package dq.dqlang.field;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.LinkedHashMap;

@Getter
@ToString
public class Field extends LinkedHashMap<String, FieldItem> {

    public Field addPathItem(String name, FieldItem item) {
        this.put(name, item);
        return this;
    }
}