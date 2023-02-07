package dq.exception;

public class UnknownResponseTimeTerm extends RuntimeException {

    public UnknownResponseTimeTerm(String term) {
        super("Unknown reponse time term: " + term);
    }
}