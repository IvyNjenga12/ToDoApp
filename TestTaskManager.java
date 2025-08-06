import java.time.LocalDate;
import java.util.List;

public class TestTaskManager {
    public static void main(String[] args) {
        // 1. Setup categories
        Category catGUI = new Category("GUI");
        Category catBackend = new Category("Backend");
        Category catDocs = new Category("Documentation");

        // 2. Create a TaskManager
        TaskManager manager = new TaskManager();

        // 3. Add tasks
        manager.addTask(new Task("Design UI", "Create main window", LocalDate.now(), "High", catGUI));
        manager.addTask(new Task("Connect DB", "Set up JDBC connection", LocalDate.now().plusDays(1), "Medium", catBackend));
        manager.addTask(new Task("Write Report", "Project documentation", LocalDate.now().plusDays(5), "Low", catDocs));
        manager.addTask(new Task("Fix Login Bug", "Resolve login crash", LocalDate.now().minusDays(1), "High", catBackend));

        // Mark one as completed
        Task taskToComplete = manager.getAllTasks().get(1); // e.g., "Connect DB"
        taskToComplete.setCompleted(true);

        // 4. Show all tasks
        System.out.println("\n📋 All Tasks:");
        printList(manager.getAllTasks());

        // 5. Show completed tasks
        System.out.println("\n✅ Completed Tasks:");
        printList(manager.getCompletedTasks());

        // 6. Filter by category
        System.out.println("\n📂 Tasks in category 'Backend':");
        printList(manager.getTasksByCategory("Backend"));

        // 7. Filter by priority
        System.out.println("\n🔥 High Priority Tasks:");
        printList(manager.getTasksByPriority("High"));

        // 8. Tasks due before tomorrow
        System.out.println("\n📅 Tasks due before tomorrow:");
        printList(manager.getTasksDueBefore(LocalDate.now().plusDays(1)));

        // 9. Tasks due today
        System.out.println("\n🕒 Tasks due today:");
        printList(manager.getTasksDueToday());

        // 10. Tasks due this week
        System.out.println("\n📆 Tasks due this week:");
        printList(manager.getTasksDueThisWeek());
    }

    // Utility method to print any list of tasks
    public static void printList(List<Task> tasks) {
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
        } else {
            for (Task task : tasks) {
                System.out.println(task);
            }
        }
    }
}
