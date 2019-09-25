package answer;

import com.google.errorprone.annotations.Immutable;

import java.util.Objects;

@Immutable
public class Answer {
    private int statusCode;
    private String comment;

    public Answer(int statusCode, String comment) {
        this.statusCode = statusCode;
        this.comment = comment;
    }

    public Answer() {
    }

    public static Answer failureAnswer(String message) {
        return new Answer(400, message);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getComment() {
        return comment;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof Answer)) return false;
        Answer answer = (Answer) o;
        return getStatusCode() == answer.getStatusCode() &&
                Objects.equals(getComment(), answer.getComment());
    }

    @Override
    public final int hashCode() {
        return Objects.hash(getStatusCode(), getComment());
    }
}