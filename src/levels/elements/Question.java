package levels.elements;

public class Question {
    public final String question, positiveAnswer, negativeAnswer;
    public final int value;

    public Question(String question, String positiveAnswer, String negativeAnswer, int value) {
        this.question = question;
        this.positiveAnswer = positiveAnswer;
        this.negativeAnswer = negativeAnswer;
        this.value = value;
    }
}
