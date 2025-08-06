import java.time.LocalDate;

public class TestTask {
    public static void main(String[] args) {
        // Create a new task
        Category cat = new Category("Work");

        //create task
        Task task = new Task("Finish Report", "Complete the quarterly report", LocalDate.of(2025, 12, 31), "High", cat);

        // Print task details
        System.out.println("Before completion:");
        System.out.println(task);

        task.setCompleted(true); // Mark task as completed
        System.out.println("After completion:");
        System.out.println(task);

    }    
}