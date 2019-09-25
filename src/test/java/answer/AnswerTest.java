package answer;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class AnswerTest {
    private Answer answer = new Answer(100, "message");

    @Test
    public void failureAnswer() {
        assertThat(Answer.failureAnswer("bad")).isEqualTo(new Answer(400, "bad"));
    }

    @Test
    public void getStatusCode() {
        assertThat(answer.getStatusCode()).isEqualTo(100);
    }

    @Test
    public void getComment() {
        assertThat(answer.getComment()).isEqualTo("message");
    }

    @Test
    public void equalsContract() {
        EqualsVerifier.forClass(Answer.class).verify();
    }
}