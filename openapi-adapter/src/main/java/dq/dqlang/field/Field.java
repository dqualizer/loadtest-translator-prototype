package dq.dqlang.field;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Field {

    private LinkedHashSet<FieldItem> items;
}