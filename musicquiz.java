/* *****************************************
AUTHOR Saeeda Shukor
This program runs a short multiple choice answer quiz about famous English rock bands
- Only three users can take part in the quiz
- For each question, the user is given 3 attempts to get the question right
- Once the user answers all 5 questions, their total score is calculated
- The score of all 3 users are printed out from the highest to the lowest score
******************************************** */
import java.util.Scanner;
import java.util.*;
import java.io.*;
import java.lang.Exception;
import java.nio.file.Files;
import java.nio.file.Paths;
class musicquiz {

    // Number of users taking the quiz
    final static int HOWMANYUSERS = 3;

    public static void main(String[] p) throws IOException
    {
        // Stores each user record into an array
        User [] users = new User [HOWMANYUSERS];

        // Stores the question and answer into Questions array
        Question[] questions = new Question[5];
        String [][] savedScores;

        // Asks the user if they wish to load the scores from the previous save file
        if (askToReloadSave()) {
            BufferedReader inStream = new BufferedReader(new FileReader("musicquiz.csv"));
            String nextword = inStream.readLine();

            // Repeatedly check the current word and write it to the output file if not null
            while (nextword != null)
            {
                System.out.println(nextword);
                nextword = inStream.readLine();
            }
            inStream.close();

            System.exit(0);

        }

        else {

            users = createUsers();
            questions = createQuestions();

            for (int k = 0; k < 3; k++) {

                int score = 0;
                final int total_score = questions.length*20; // total score of the quiz is /100
                final int NUMBER_OF_QUESTIONS = 5; // total number of questions in the quiz

                // A loop to control the number of questions asked
                for (int i = 0; i < NUMBER_OF_QUESTIONS; i++) {

                    System.out.println(getName(users[k])+ " please answer the following question: ");
                    String answer = input(questions[i].question);

                    // A boolean expression that states the correct answer as true and incorrect answer as false
                    boolean ans;
                    if (answer.equals(getAnswer(questions[i]))) {
                        ans = true;
                    }
                    else {
                        ans = false;
                    }

                    // When the user gives the correct answer
                    // Score increases and a text response is printed out for answering correctly
                    while (ans == true) {
                        score = score + 20;
                        printCorrectResponse();
                        break;
                    }

                    // When the user gives incorrect answer
                    // A text response is printed out for answering incorrectly
                    // The user is given a total of three attempts to answer the question correctly before the loop breaks
                    int MAX_ATTEMPTS = 1;
                    while (ans == false) {

                        printIncorrectResponse();
                        printNewAttemptResponse();
                        MAX_ATTEMPTS = MAX_ATTEMPTS + 1;
                        answer = input(questions[i].question);

                        // If the user manages to answer correctly, the loop breaks
                        if (answer.equals(getAnswer(questions[i]))) {
                            score = score + 20;
                            printCorrectResponse();
                            break;
                        }

                        // If the user does not manage to answer correctly within 3 attempts, the loop breaks
                        else if (MAX_ATTEMPTS == 3) {
                            printIncorrectResponse();
                            printLastAttemptResponse();
                            break;
                        }

                    }

                }

                // Sets the mark scored by the user in the quiz
                users[k] = setMarks(users[k], score);

                // Calculates the total score for the specific user and prints it out
                System.out.println(getName(users[k]) + ", you have scored " + score + "/" + total_score + "!" );
            }

        }

        // Uses a bubble sort to sort the users according to the highest score
        users = bubblesort(users);

        // Saves the file input
        saveFile("musicquiz.csv", users);

        // Prints the name of the user and the score they received in the quiz
        System.out.println("Prnting out all user records:");
        for (int j = 0; j < HOWMANYUSERS; j++) {
            System.out.println(printDetails(users[j]));
        }

        System.exit(0);

    }

    /* Create an ADT called Question which stores the question and answer

    */
    public static Question questionBank (Question q, String giveQuestion, String giveAnswer) {
        q.question = giveQuestion;
        q.ans = giveAnswer;
        return q;
    }

    // Uses getter method to retrieve answer
    public static String getAnswer (Question q) {
        return q.ans;
    }


    /* Creates an array to store the records of the questions and answers

     */
    public static Question[] createQuestions() {

        Question[] questions = new Question[5];
        Question question = new Question();

        // Stores each question into a new record along with the correct answer
        Question q1 = new Question();
        q1 = questionBank(q1, "1. Where was the band DON BROCO formed?\n(a) Sheffield  |  (b) Liverpool  |  (c) Bedford  |  (d) Leeds", "c");

        // Stores the records into an array
        questions[0] = q1;

        Question q2 = new Question();
        q2 = questionBank(q2, "2. Which Radiohead album had been cited by critics and musicians as one of the greatest album of all time?\n(a) OK Computer  |  (b) In Rainbows  |  (c) Amnesiac  |  (d) A Moon Shaped Pool", "a");

        questions[1] = q2;

        Question q3 = new Question();
        q3 = questionBank(q3, "3. Which song was NOT part of Architects' newest album 'Holy Hell'?\n(a) Death Is Not Defeat  |  (b) Hereafter  |  (c) Doomsday  |  (d) Heartburn", "d");

        questions[2] = q3;

        Question q4 = new Question();
        q4 = questionBank(q4, "4. When was the band alt-J formed?\n(a) 2007  |  (b) 2005  |  (c) 2010  |  (d) 2002", "a");

        questions[3] = q4;

        Question q5 = new Question();
        q5 = questionBank(q5, "5. Who is the lead singer of Arctic Monkeys?\n(a) Jamie Cook  |  (b) Alex Turner  |  (c) Matt Helders  |  (d) Nick O'Malley", "b");

        questions[4] = q5;

        return questions;
    }

    /* Create an ADT called User which stores their name and their quiz score

     */
    public static User createUser (String username, int quizmarks) {
        User u = new User ();
        u.name = username;
        u.marks = quizmarks;
        return u;
    }

    // Uses getter method to retrieve name of the user
    public static String getName (User u) {
        return u.name;
    }

    // Uses getter method to retrieve quiz marks
    public static int getMark (User u) {
        return u.marks;
    }

    // Uses setter method to set the quiz marks of the user
    public static User setMarks (User u, int qmark) {
        u.marks = qmark;
        return u;
    }

    /* Creates an array to store the records of the users

     */
    public static User [] createUsers() {
        User[] users = new User[3];

        for (int k = 0; k < HOWMANYUSERS; k++) {
            String nextname = input("What is your name?"); // input name of user
            int nextscore = 0; // score of each user

            User u = createUser(nextname, nextscore); // creates a record of the user
            users[k] = u; // stores the user record into the array
        }

        return users;
    }


    /* Print details in a string format

     */
    public static String printDetails (User u) {
        String message = getName(u) + " - " + getMark(u) + "/100";
        return message;
    }

    /* User input to the given question

     */
    public static String input(String message) {
        Scanner scanner = new Scanner(System.in);
        String ans;
        System.out.println(message);
        ans = scanner.nextLine();
        return ans;
    }

    public static int inputInt(String message) {

        return Integer.parseInt(input(message));
    }

    /* Prints out response when user gives correct answer

     */
    public static void printCorrectResponse() {

        System.out.println("You're right!");
    }

    /* Prints out response when user gives incorrect answer

     */
    public static void printIncorrectResponse() {

        System.out.println("Incorrect answer!");
    }

    /* Prints out text that tells the user they may attempt the question again

     */
    public static void printNewAttemptResponse() {

        System.out.println("Please try again.");
    }

    /* Lets the user know that they do not have any attempts left

     */
    public static void printLastAttemptResponse() {

        System.out.println("I'm sorry, but that was your last attempt.");
    }


    /* Bubble sort to arrange the user records from the highest score to the lowest score

     */
    public static User[] bubblesort (User [] users) {
        int pass = 0;
        while (pass <= 2) {
            for (int i = 0; i <= 1; i++) {
                if (users[i].marks < users[i+1].marks) {
                    int swapmarks = users[i+1].marks;
                    String swapname = users[i+1].name;

                    users[i+1].marks = users[i].marks;
                    users[i+1].name = users[i].name;

                    users[i].marks = swapmarks;
                    users[i].name = swapname;
                }

            }
            pass += 1;
        }

        return users;
    }

    /* Uses file writer to save the scores received by each user

     */
    public static void saveFile(String filename, User [] users)
    {
        try {
            PrintWriter outputStream = new PrintWriter(new FileWriter(filename));

            for (int x = 0; x < users.length; x++) {
                outputStream.write("" + users[x].name + " - ");
                outputStream.write("" + users[x].marks + "/100");

                outputStream.write("\n");
            }

            outputStream.close();
        }

        catch (IOException e) {
            e.printStackTrace();
        }

    }


    /* Asks the user if they wish to load the previous scores

     */
    public static boolean askToReloadSave() {

        String ans = input("Would you like to load the scores from the previous quiz?");

        if ((ans.equals("Yes")) || (ans.equals("yes")) || (ans.equals("y")) || (ans.equals("Y"))) {
            return true;
        }

        else {
            return false;
        }

    }


} // END class musicQuiz


class Question {
    String question; // stores question into the question record
    String ans; // stores answer into the question record

} // END class Question

class User {
    String name; // stores name into user record
    int marks; // stores marks into user record

} // END class User