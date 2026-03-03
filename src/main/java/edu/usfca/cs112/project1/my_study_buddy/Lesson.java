package edu.usfca.cs112.project1.my_study_buddy;

import java.util.Scanner;
import java.io.Serializable;

public class Lesson implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private transient Model myModel; 
	private String topic;
	private transient Scanner scanner;
	private boolean completed;
	private int quiz_completion; // raw scores

	public Lesson(String topic, int quiz_completion) { 
		this.myModel = new Model(); 
		this.topic = topic;  
		this.scanner = new Scanner(System.in);
		this.completed = false;
		this.quiz_completion = quiz_completion;
	}
	
	public boolean isCompleted() {
		return completed;
	}
	
	public void markCompleted() {
		this.completed = true;
	}
	
	public String getPerformance() {
        return String.format("%.2f", (quiz_completion / 6.0) * 100.0);
    }
    
    public String getTopic() {
        return topic;
    }
	
	public void doLesson() {
		System.out.println("Your next testing topic is: " + this.topic.toUpperCase());
		enter_continue();
		
		System.out.println("Overview: \n" + myModel.generate("Create a short overview for " + this.topic));
		enter_continue();
		
		System.out.println("Lecture: \n" + myModel.generate("Provide a substantial lecture regarding " + this.topic));
		enter_continue();
		
		System.out.println("Do you have any clarifying questions? Type NO if not.");
		String userInput = scanner.nextLine();
		
		while (!userInput.equalsIgnoreCase("NO")) {
			String response = myModel.generate("In relation to " + this.topic + ": " + userInput);
			System.out.println(response);
			
			System.out.println("Do you have any more questions? Type NO if not.");
			userInput = scanner.nextLine();
		}
		enter_continue();
		
		doQuiz();
		markCompleted();
	}
	
	private void enter_continue() {
		System.out.println("Press enter to continue.");
		while (!scanner.nextLine().isEmpty()) {
	        //
	    }
		System.out.println("Please be patient as the next section is loading...");
	}
	
	private void doQuiz() {
		int score = 0;
		String guess;
		String question;
		String answer;
		
		System.out.println("Quiz Time!");
		for (int i = 0; i < 6; i++) {
			question = myModel.generate("Ask a new singular short answer question regarding " + this.topic);
			System.out.println("Question " + (i + 1) + ": " + question + "\nEnter your answer or type 'SKIP' to skip:");
			
			answer = myModel.generate("Tell me the answer in one sentence: " + question);
			guess = scanner.nextLine();

			if (guess.equalsIgnoreCase("SKIP")) {
				System.out.println("Question skipped. \nThe answer was: " + answer);
				continue;
			}
			
			boolean correct = Boolean.valueOf(myModel.generate("The question you provided was '" + question + "' with the answer '" + answer + "', would you consider this an equal answer to said question: '" + guess + "' if correct only type 'true', if not type 'false'"));
			
			if (correct) {
                System.out.println("Correct! The answer was: " + answer);
                score++;
            } else {
                System.out.println("Incorrect. The correct answer was: " + answer);
            }
		}
		
		this.quiz_completion = score;
        System.out.println("Your final score is " + score + " out of 6.");
        System.out.println("Your performance: " + getPerformance() + "%");
	}
	
	public int getQuiz_completion() {
		return quiz_completion;
	}

	public void setQuiz_completion(int quiz_completion) {
		this.quiz_completion = quiz_completion;
	}
	
	public void reinitialize () {
		scanner = new Scanner(System.in);
		myModel = new Model();
	}

}
