package edu.usfca.cs112.project1.my_study_buddy;

import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.EOFException;

public class Course { 
	// Contains the Lessons completed so far in the order they were taken. 
	// Might be empty, for example if the user has never done a Lesson before. 

	private ArrayList<Lesson> lessons; 
	// Always contains the full list of topics, loaded from file. This list defines the proper sequence of the Course. 
	
	private ArrayList<String> topics; 
	// Construct a Course by loading (deserializing) already finished Lessons into the lessons field 
	// and reading all topics from file into the topics field
	
	private Scanner scanner;
	
	public Course(String topicsFile, String lessonsFile) throws ClassNotFoundException, IOException { 
		setLessons(new ArrayList<>()); 
		setTopics(new ArrayList<>());
		loadLessonsFromFile(lessonsFile); // a Course instance method you would implement 
		loadTopicsFromFile(topicsFile); // a Course instance method you would implement
	}
	
	// deserializing
	public void loadLessonsFromFile(String lessonsFile) throws FileNotFoundException, IOException, ClassNotFoundException, EOFException {	
		try (FileInputStream fis = new FileInputStream(lessonsFile);
			ObjectInputStream ois = new ObjectInputStream(fis)) {
		    
			while (true) {
		        try {
		        	Lesson l = (Lesson) ois.readObject();
		        	l.reinitialize();
		        	lessons.add(l);
		        } catch (EOFException e) {
		            break;
		        }
		        
		    }
		} catch (EOFException e) {
			return;
		}
	}
	 
	// serializing
	public void saveLessonsToFile(String lessonsFile) throws FileNotFoundException, IOException, ClassNotFoundException {
		try {FileOutputStream fos = new FileOutputStream(lessonsFile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
				
			for (Lesson lesson : lessons) {
				oos.writeObject(lesson);
			}
			
			oos.close();
			
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}	

	public void loadTopicsFromFile(String topicsFile) {
		File topics_file = new File(topicsFile);
		try {
			scanner = new Scanner(topics_file);
			while (scanner.hasNextLine()) {
				String topic = scanner.nextLine();
				topics.add(topic);	
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Lesson nextLesson() {
        for (int i = 0; i < topics.size(); i++) {
            if (i < lessons.size()) {
                Lesson lesson = lessons.get(i);
                if (lesson == null || !lesson.isCompleted()) {
                    if (lesson == null) {
                        lesson = new Lesson(topics.get(i), 0);
                        lessons.set(i, lesson);
                    }
                    return lesson;
                }
            } else {
                Lesson lesson = new Lesson(topics.get(i), 0);
                lessons.add(lesson);
                return lesson;
            }
        }
        
        System.out.println("All lessons completed. Starting from the beginning...");
        Lesson lesson = new Lesson(topics.get(0), 0);
        if (!lessons.isEmpty()) {
            lessons.set(0, lesson);
        } else {
            lessons.add(lesson);
        }
        return lesson;
    }

	public Lesson selectLesson(Scanner scanner, Course course) {
        System.out.println("Select a lesson by number:");
        for (int i = 0; i < topics.size(); i++) {
            System.out.println((i + 1) + ": " + topics.get(i));
        }
        int select = -1;
        
        try {
            select = Integer.valueOf(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a valid lesson number.");
            return null;
        }
        if (select < 1 || select > topics.size()) {
            System.out.println("Invalid lesson number.");
            return null;
        }
        
        Lesson lesson;
        int index = select - 1;
        
        if (index < lessons.size() && lessons.get(index) != null) {
            lesson = lessons.get(index);
            if (lesson.isCompleted()) {
                System.out.println("Lesson already completed. Do you want to redo the lesson? (Y/N)");
                String response = scanner.nextLine();
                
                if (response.equalsIgnoreCase("Y")) {
                    lesson = new Lesson(topics.get(index), 0);
                    lessons.set(index, lesson);
                } else {
                	System.out.println("Returning to the menu...");
                	return null;
                }
            }
        } else {
            lesson = new Lesson(topics.get(index), 0);
            
            while (lessons.size() < index) {
                lessons.add(null);
            }
            if (index < lessons.size()) {
                lessons.set(index, lesson);
            } else {
                lessons.add(lesson);
            }
        }
        return lesson;
    }
	
	public void evaluateLearning(String input, Course c) {
		if (lessons.isEmpty()) {
            System.out.println("No lessons have been completed yet.");
            return;
        }
        
        double best_score = -1;
        double worst_score = 101;
        Lesson best_lesson = null;
        Lesson worst_lesson = null;
        double total = 0;
        int count = 0;
        
        for (Lesson lesson : lessons) {
            if (lesson != null && lesson.isCompleted()) {
                double performance = Double.valueOf(lesson.getPerformance());
                total += performance;
                count++;
                if (performance > best_score) {
                    best_score = performance;
                    best_lesson = lesson;
                }
                if (performance < worst_score) {
                    worst_score = performance;
                    worst_lesson = lesson;
                }
            }
        }
        
        if (count > 0) {
            double average = total / count;
            System.out.println("Performance Analysis:");
            System.out.println("Best Lesson: " + (best_lesson != null ? best_lesson.getTopic() : "N/A") + " with " + best_score + "%");
            System.out.println("Worst Lesson: " + (worst_lesson != null ? worst_lesson.getTopic() : "N/A") + " with " + worst_score + "%");
            System.out.println("Average Lesson Performance: " + average + "%");
        } else {
            System.out.println("No completed lessons to evaluate.");
        }
	}
	
///////////////
	
	public ArrayList<Lesson> getLessons() {
		return lessons;
	}

	public void setLessons(ArrayList<Lesson> lessons) {
		this.lessons = lessons;
	}

	public ArrayList<String> getTopics() {
		return topics;
	}

	public void setTopics(ArrayList<String> topics) {
		this.topics = topics;
	}
}
